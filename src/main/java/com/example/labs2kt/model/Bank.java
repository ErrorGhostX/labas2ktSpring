package com.example.labs2kt.model;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Table("BANK")
public class Bank {

    @Id
    private Long id;

    @NotBlank(message = "Полное название банка не может быть пустым.")
    private String nameFull;

    @NotBlank(message = "Краткое название банка не может быть пустым.")
    private String nameShort;

    @NotBlank(message = "ИНН не может быть пустым.")
    @Pattern(regexp = "^[0-9]{10}$", message = "ИНН должен состоять из 10 цифр.")
    private String inn;

    @NotBlank(message = "БИК не может быть пустым.")
    @Pattern(regexp = "^[0-9]{9}$", message = "БИК должен состоять из 9 цифр.")
    private String bik;

    @NotBlank(message = "Корсчет не может быть пустым.")
    private String corAccount;

    @NotBlank(message = "Номер счета не может быть пустым.")
    private String account;

    @NotBlank(message = "Город не может быть пустым.")
    private String city;
}
