package com.example.labs2kt.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Table("TYPE_ACCOUNT")
public class TypeAccount {

    @Id
    private Long id;

    @NotBlank(message = "Тип счета не может быть пустым.")
    private String typeAccount;
}
