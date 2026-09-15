package com.ems.budget_service.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetResponse(
        Long id,
        Long userId,
        Long categoryId,
        BigDecimal monthlyLimit,
        Integer month,
        Integer year,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
