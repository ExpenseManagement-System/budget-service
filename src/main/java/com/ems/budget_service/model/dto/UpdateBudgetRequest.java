package com.ems.budget_service.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdateBudgetRequest(
        @NotNull(message = "Monthly limit is required")
        @Positive(message = "Monthly limit must be greater than zero")
        BigDecimal monthlyLimit
) {}
