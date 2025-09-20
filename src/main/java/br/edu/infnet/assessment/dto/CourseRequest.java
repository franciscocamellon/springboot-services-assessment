package br.edu.infnet.assessment.dto;
import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank String name,
        @NotBlank String code
) {}
