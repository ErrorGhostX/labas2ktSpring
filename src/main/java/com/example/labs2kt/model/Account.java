package com.example.labs2kt.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Table("ACCOUNT")
public class Account {

    @Id
    private Long id;

    @NotNull(message = "Номер счета не может быть пустым.")
    private String accountNumber;

    @NotNull(message = "ID типа счета не может быть пустым.")
    @Min(value = 1, message = "ID типа счета должно быть больше нуля.")
    private Long typeId;

    @NotNull(message = "ID банка не может быть пустым.")
    @Min(value = 1, message = "ID банка должно быть больше нуля.")
    private Long bankId;

    @NotNull(message = "ID договора не может быть пустым.")
    @Min(value = 1, message = "ID договора должно быть больше нуля.")
    private Long agreementId;
}
