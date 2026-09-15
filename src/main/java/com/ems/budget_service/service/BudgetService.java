package com.ems.budget_service.service;

import com.ems.budget_service.exception.BudgetAlreadyExistsException;
import com.ems.budget_service.exception.ResourceNotFoundException;
import com.ems.budget_service.model.dto.BudgetResponse;
import com.ems.budget_service.model.dto.CreateBudgetRequest;
import com.ems.budget_service.model.dto.UpdateBudgetRequest;
import com.ems.budget_service.model.entity.Budget;
import com.ems.budget_service.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<BudgetResponse> getBudgets(Long userId, Integer month, Integer year) {
        List<Budget> budgets;

        if (month != null && year != null) {
            budgets = budgetRepository.findByUserIdAndMonthAndYear(userId, month, year);
        } else {
            budgets = budgetRepository.findByUserId(userId);
        }

        return budgets.stream()
                .map(this::mapToResponse)
                .toList();
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

    @Transactional(readOnly = true)
    public BudgetResponse getBudgetById(Long id, Long userId) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Budget with id %d not found for user %d", id, userId)
                ));

        return mapToResponse(budget);
    }

    @Transactional
    public BudgetResponse updateBudget(Long id, Long userId, UpdateBudgetRequest request) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Budget with id %d not found for user %d", id, userId)
                ));

        budget.setMonthlyLimit(request.monthlyLimit());

        // Saving updated entity; @UpdateTimestamp automatically updates updated_at field
        Budget updatedBudget = budgetRepository.save(budget);
        return mapToResponse(updatedBudget);
    }

    @Transactional
    public void deleteBudget(Long id, Long userId) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Budget with id %d not found for user %d", id, userId)
                ));

        budgetRepository.delete(budget);
    }
}