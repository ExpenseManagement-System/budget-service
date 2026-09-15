package com.ems.budget_service.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateBudgetRequest(
        @NotNull(message = "Category ID is required")
        Long categoryId,

        @NotNull(message = "Monthly limit is required")
        @Positive(message = "Monthly limit must be greater than zero")
        BigDecimal monthlyLimit,

        @NotNull(message = "Month is required")
        @Min(value = 1, message = "Month must be between 1 and 12")
        @Max(value = 12, message = "Month must be between 1 and 12")
        Integer month,

        @NotNull(message = "Year is required")
        @Min(value = 2000, message = "Year must be valid")
        Integer year
) {}
