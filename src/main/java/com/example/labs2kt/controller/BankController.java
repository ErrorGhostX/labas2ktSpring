package com.example.labs2kt.controller;

import com.example.labs2kt.model.Bank;
import com.example.labs2kt.repository.BankRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankRepository bankRepository;
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public String listBanks(Model model) {
        model.addAttribute("banks", bankRepository.findAll());
        model.addAttribute("bank", new Bank());
        return "bank";
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public String saveBank(@Valid @ModelAttribute("bank") Bank bank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("banks", bankRepository.findAll());
            return "bank";
        }

        if (bank.getId() != null) {
            // Обновление существующего банка
            Bank existingBank = bankRepository.findById(bank.getId()).orElseThrow(() -> new IllegalArgumentException("Неверный ID банка"));
            existingBank.setNameFull(bank.getNameFull());
            existingBank.setNameShort(bank.getNameShort());
            existingBank.setInn(bank.getInn());
            existingBank.setBik(bank.getBik());
            existingBank.setCorAccount(bank.getCorAccount());
            existingBank.setAccount(bank.getAccount());
            existingBank.setCity(bank.getCity());

            bankRepository.save(existingBank);
        } else {
            // Сохранение нового банка
            bankRepository.save(bank);
        }

        return "redirect:/banks";
    }

    @GetMapping("/edit/{id}")
    public String editBank(@PathVariable Long id, Model model) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный ID банка: " + id));
        model.addAttribute("bank", bank);
        return "bank";  // Вернем ту же форму с предзаполненными данными
    }

    @GetMapping("/delete/{id}")
    public String deleteBank(@PathVariable Long id) {
        bankRepository.deleteById(id);
        return "redirect:/banks";
    }
}
