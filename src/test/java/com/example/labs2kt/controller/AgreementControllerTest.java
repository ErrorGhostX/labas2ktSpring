package com.example.labs2kt.controller;

import com.example.labs2kt.model.Agreement;
import com.example.labs2kt.repository.AgreementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgreementController.class)
@Import(AgreementControllerTest.MockConfig.class)
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "admin", roles = "ADMIN") // Пользователь с ролью "ADMIN"
class AgreementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgreementRepository agreementRepository;

    // Конфигурация для мока репозитория Agreement
    @TestConfiguration
    static class MockConfig {
        @Bean
        public AgreementRepository agreementRepository() {
            return mock(AgreementRepository.class);
        }
    }

    // Тестирование отображения списка договоров и пустой формы
    @Test
    @DisplayName("GET /agreements — должен отображать список и пустую форму")
    void testListAgreements() throws Exception {
        Agreement a1 = new Agreement();
        a1.setId(1L);
        a1.setNumber("A-100");
        a1.setDateOpen(LocalDate.of(2025, 1, 1));
        when(agreementRepository.findAll()).thenReturn(Arrays.asList(a1)); // Загружаем один договор в репозиторий

        mockMvc.perform(get("/agreements"))
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andExpect(view().name("agreement")) // Ожидаем представление "agreement"
                .andExpect(model().attributeExists("agreements")) // Ожидаем атрибут "agreements" в модели
                .andExpect(model().attributeExists("agreement")); // Ожидаем атрибут "agreement" в модели

        verify(agreementRepository, times(1)).findAll(); // Проверяем, что findAll() был вызван один раз
    }

    // Тестирование успешного сохранения нового договора
    @Test
    @DisplayName("POST /agreements — корректная форма должна сохранять и редиректить")
    void testSaveNewAgreement() throws Exception {
        mockMvc.perform(post("/agreements")
                        .with(csrf())  // CSRF защита
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("number", "B-200") // Номер договора
                        .param("dateOpen", "2025-02-02") // Дата открытия
                        .param("dateClose", "") // Пустая дата закрытия
                        .param("note", "Test note")) // Примечание
                .andExpect(status().is3xxRedirection())  // Ожидаем редирект
                .andExpect(redirectedUrl("/agreements")); // Ожидаем редирект на /agreements

        ArgumentCaptor<Agreement> captor = ArgumentCaptor.forClass(Agreement.class);
        verify(agreementRepository).save(captor.capture()); // Проверяем, что метод save был вызван

        Agreement saved = captor.getValue(); // Получаем сохраненный объект
        assertThat(saved.getNumber()).isEqualTo("B-200"); // Проверяем, что номер договора сохранен правильно
        assertThat(saved.getDateOpen()).isEqualTo(LocalDate.of(2025, 2, 2)); // Проверяем дату открытия
        assertThat(saved.getNote()).isEqualTo("Test note"); // Проверяем примечание
    }

    // Тестирование формы с ошибками валидации
    @Test
    @DisplayName("POST /agreements — некорректная форма должна возвращать ошибки и оставаться на форме")
    void testSaveAgreementValidationError() throws Exception {
        mockMvc.perform(post("/agreements")
                        .with(csrf())  // CSRF защита
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("number", "") // Пустой номер
                        .param("dateOpen", "")) // Пустая дата открытия
                .andExpect(status().isOk())  // Ожидаем статус 200
                .andExpect(view().name("agreement")) // Ожидаем представление "agreement"
                .andExpect(model().attributeHasFieldErrors("agreement", "number", "dateOpen")) // Проверяем ошибки на полях
                .andExpect(model().attributeExists("agreements")); // Проверяем, что атрибут "agreements" существует

        verifyNoInteractions(agreementRepository); // Проверяем, что метод save() не был вызван
    }

    // Тестирование редактирования существующего договора
    @Test
    @DisplayName("GET /agreements/edit/{id} — существующий ID должен заполнять форму")
    void testEditAgreement() throws Exception {
        Agreement existing = new Agreement();
        existing.setId(42L); // Устанавливаем ID существующего договора
        existing.setNumber("Z-42");
        existing.setDateOpen(LocalDate.of(2020, 5, 5));
        when(agreementRepository.findById(42L)).thenReturn(Optional.of(existing)); // Возвращаем существующий договор из репозитория

        mockMvc.perform(get("/agreements/edit/42"))
                .andExpect(status().isOk())  // Ожидаем статус 200
                .andExpect(view().name("agreement")) // Ожидаем представление "agreement"
                .andExpect(model().attribute("agreement", existing)); // Проверяем, что модель содержит договор

        verify(agreementRepository).findById(42L); // Проверяем, что метод findById был вызван
    }

    // Тестирование удаления договора
    @Test
    @DisplayName("GET /agreements/delete/{id} — должен удалить и редиректить")
    void testDeleteAgreement() throws Exception {
        mockMvc.perform(get("/agreements/delete/1"))
                .andExpect(status().is3xxRedirection())  // Ожидаем редирект
                .andExpect(redirectedUrl("/agreements")); // Ожидаем редирект на /agreements

        verify(agreementRepository).deleteById(1L); // Проверяем, что метод deleteById был вызван
    }
}
