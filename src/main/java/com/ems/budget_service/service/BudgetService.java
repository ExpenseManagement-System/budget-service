package com.ems.budget_service.service;

import com.ems.budget_service.exception.BudgetAlreadyExistsException;
import com.ems.budget_service.model.dto.BudgetResponse;
import com.ems.budget_service.model.dto.CreateBudgetRequest;
import com.ems.budget_service.model.entity.Budget;
import com.ems.budget_service.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    @Transactional
    public BudgetResponse createBudget(Long userId, CreateBudgetRequest request) {
        // Enforce business rule: check duplicate before saving
        boolean exists = budgetRepository.existsByUserIdAndCategoryIdAndMonthAndYear(
                userId, request.categoryId(), request.month(), request.year()
        );

        if (exists) {
            throw new BudgetAlreadyExistsException(
                    String.format("A budget already exists for category %d in %d/%d",
                            request.categoryId(), request.month(), request.year())
            );
        }

        Budget budget = Budget.builder()
                .userId(userId)
                .categoryId(request.categoryId())
                .monthlyLimit(request.monthlyLimit())
                .month(request.month())
                .year(request.year())
                .build();

        Budget savedBudget = budgetRepository.save(budget);
        return mapToResponse(savedBudget);
    }

    private BudgetResponse mapToResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getUserId(),
                budget.getCategoryId(),
                budget.getMonthlyLimit(),
                budget.getMonth(),
                budget.getYear(),
                budget.getCreatedAt(),
                budget.getUpdatedAt()
        );
    }
}