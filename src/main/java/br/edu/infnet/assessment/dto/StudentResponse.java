package br.edu.infnet.assessment.dto;

import java.time.OffsetDateTime;

public record StudentResponse(
        Long id,
        String name,
        String cpf,
        String email,
        String phone,
        String address,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt
) {}
