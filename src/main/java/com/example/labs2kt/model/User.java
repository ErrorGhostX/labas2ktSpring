package com.example.labs2kt.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("USERS") // Имя таблицы в БД
public class User {

    @Id
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым.")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым.")
    private String password;

    @Email(message = "Введите корректный email.")
    @NotBlank(message = "Email не может быть пустым.")
    private String email;

    private String role = "USER"; // по умолчанию роль USER
}
