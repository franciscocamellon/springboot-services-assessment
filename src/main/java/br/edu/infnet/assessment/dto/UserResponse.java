package br.edu.infnet.assessment.dto;


public record UserResponse(
        Long id,
        String name,
        String email,
        Boolean active,
        String role
) {}
