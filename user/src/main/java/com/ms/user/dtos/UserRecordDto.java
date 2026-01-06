package com.ms.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UserRecordDto(@NotBlank String name,
                            @NotBlank @Email String email,
                            @NotBlank @CPF(message = "Enter a valid CPF") String CPF) {
}
