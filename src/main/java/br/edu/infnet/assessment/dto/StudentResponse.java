package br.edu.infnet.assessment.dto;

public record StudentResponse(
        Long id,
        String name,
        String cpf,
        String email,
        String phone,
        String address
) {}
