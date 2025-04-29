package com.example.labs2kt.controller;

import com.example.labs2kt.model.Agreement;
import com.example.labs2kt.repository.AgreementRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/agreements")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementRepository agreementRepository;
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public String listAgreements(Model model) {
        model.addAttribute("agreements", agreementRepository.findAll());
        model.addAttribute("agreement", new Agreement());
        return "agreement";
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public String saveAgreement(@Valid @ModelAttribute("agreement") Agreement agreement, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("agreements", agreementRepository.findAll());
            return "agreement";
        }

        if (agreement.getId() != null) {
            // Обновление существующего соглашения
            Agreement existingAgreement = agreementRepository.findById(agreement.getId()).orElseThrow(() -> new IllegalArgumentException("Неверный ID договора"));
            existingAgreement.setNumber(agreement.getNumber());
            existingAgreement.setDateOpen(agreement.getDateOpen());
            existingAgreement.setDateClose(agreement.getDateClose());
            existingAgreement.setNote(agreement.getNote());

            agreementRepository.save(existingAgreement);
        } else {
            // Сохранение нового соглашения
            agreementRepository.save(agreement);
        }

        return "redirect:/agreements";
    }

    @GetMapping("/edit/{id}")
    public String editAgreement(@PathVariable Long id, Model model) {
        Agreement agreement = agreementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный ID договора: " + id));
        model.addAttribute("agreement", agreement);
        return "agreement";  // Вернем ту же форму с предзаполненными данными
    }

    @GetMapping("/delete/{id}")
    public String deleteAgreement(@PathVariable Long id) {
        agreementRepository.deleteById(id);
        return "redirect:/agreements";
    }
}
