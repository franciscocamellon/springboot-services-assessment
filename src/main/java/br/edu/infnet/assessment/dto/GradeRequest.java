package br.edu.infnet.assessment.dto;
import jakarta.validation.constraints.*;

public record GradeRequest(
        @NotNull @DecimalMin("0.0") @DecimalMax("10.0") Double grade
) {}
