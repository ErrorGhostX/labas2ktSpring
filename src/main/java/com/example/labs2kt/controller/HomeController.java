package com.example.labs2kt.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
