package br.edu.infnet.assessment.dto;

import java.time.OffsetDateTime;

public record CourseResponse(
        Long id,
        String name,
        String code,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt
) {}
