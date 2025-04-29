package com.example.labs2kt.controller;

import com.example.labs2kt.model.Account;
import com.example.labs2kt.repository.AccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public String listAccounts(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("account", new Account());
        return "account";
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public String saveAccount(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("accounts", accountRepository.findAll());
            return "account";
        }

        if (account.getId() != null) {
            // Если id присутствует, пытаемся найти объект в базе данных
            Account existingAccount = accountRepository.findById(account.getId()).orElse(null);

            if (existingAccount != null) {
                // Если объект существует, обновляем его поля
                existingAccount.setAccountNumber(account.getAccountNumber());
                existingAccount.setTypeId(account.getTypeId());
                existingAccount.setBankId(account.getBankId());
                existingAccount.setAgreementId(account.getAgreementId());

                // Сохраняем обновленный объект
                accountRepository.save(existingAccount);
            } else {
                // Если не нашли такой записи, можно, например, вернуть ошибку
                model.addAttribute("error", "Account not found");
                return "account";
            }
        } else {
            // Если id нет, это новый объект, сохраняем его
            accountRepository.save(account);
        }

        return "redirect:/accounts";  // Перенаправление на список аккаунтов
    }


    @GetMapping("/edit/{id}")
    public String editAccount(@PathVariable Long id, Model model) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID счета: " + id));
        model.addAttribute("account", account);
        return "account";  // возвращаем ту же страницу с формой
    }


    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return "redirect:/accounts";
    }
}
