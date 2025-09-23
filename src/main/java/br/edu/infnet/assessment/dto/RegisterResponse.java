package br.edu.infnet.assessment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterResponse(
    @NotBlank @Size(max = 120) String name,
    @NotBlank @Email @Size(max = 160) String email,
    @NotBlank @Size(min = 6, max = 100) String password
){}
