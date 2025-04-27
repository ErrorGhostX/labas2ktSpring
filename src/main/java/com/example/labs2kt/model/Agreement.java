package com.example.labs2kt.model;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;


import java.time.LocalDate;

@Data
@Table("AGREEMENT")
public class Agreement {

    @Id
    private Long id;

    @NotBlank(message = "Номер договора не может быть пустым.")
    private String number;

    @NotNull(message = "Дата открытия не может быть пустой.")
    private LocalDate dateOpen;

    private LocalDate dateClose;

    @Size(max = 500, message = "Примечание не должно превышать 500 символов.")
    private String note;
}
