package br.edu.infnet.assessment.dto;
import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
        @NotNull Long studentId,
        @NotNull Long courseId
) {}
