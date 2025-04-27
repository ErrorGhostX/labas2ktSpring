package com.example.labs2kt.controller;

import com.example.labs2kt.model.TypeAccount;
import com.example.labs2kt.repository.TypeAccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/typeaccounts")
@RequiredArgsConstructor
public class TypeAccountController {

    private final TypeAccountRepository typeAccountRepository;

    @GetMapping
    public String listTypeAccounts(Model model) {
        model.addAttribute("typeaccounts", typeAccountRepository.findAll());
        model.addAttribute("typeaccount", new TypeAccount());
        return "typeaccount";
    }

    @PostMapping
    public String saveTypeAccount(@Valid @ModelAttribute("typeaccount") TypeAccount typeAccount, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("typeaccounts", typeAccountRepository.findAll());
            return "typeaccount";
        }

        if (typeAccount.getId() != null) {
            // Обновление существующего типа счета
            TypeAccount existingTypeAccount = typeAccountRepository.findById(typeAccount.getId()).orElseThrow(() -> new IllegalArgumentException("Неверный ID типа счета"));
            existingTypeAccount.setTypeAccount(typeAccount.getTypeAccount());

            typeAccountRepository.save(existingTypeAccount);
        } else {
            // Сохранение нового типа счета
            typeAccountRepository.save(typeAccount);
        }

        return "redirect:/typeaccounts";
    }

    @GetMapping("/edit/{id}")
    public String editTypeAccount(@PathVariable Long id, Model model) {
        TypeAccount typeAccount = typeAccountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный ID типа счета: " + id));
        model.addAttribute("typeaccount", typeAccount);
        return "typeaccount";  // Вернем ту же форму с предзаполненными данными
    }

    @GetMapping("/delete/{id}")
    public String deleteTypeAccount(@PathVariable Long id) {
        typeAccountRepository.deleteById(id);
        return "redirect:/typeaccounts";
    }
}
