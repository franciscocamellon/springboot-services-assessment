package br.edu.infnet.assessment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudentRequest(
        @NotBlank String name,
        @NotBlank @Size(min=11,max=14) String cpf,
        @NotBlank String email,
        @NotBlank String phone,
        @NotBlank String address
) {}
