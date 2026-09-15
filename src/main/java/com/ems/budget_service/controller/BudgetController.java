package com.ems.budget_service.controller;

import com.ems.budget_service.model.dto.BudgetResponse;
import com.ems.budget_service.model.dto.CreateBudgetRequest;
import com.ems.budget_service.model.dto.UpdateBudgetRequest;
import com.ems.budget_service.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateBudgetRequest request) {

        BudgetResponse response = budgetService.createBudget(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        List<BudgetResponse> budgets = budgetService.getBudgets(userId, month, year);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudgetById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {

        BudgetResponse budget = budgetService.getBudgetById(id, userId);
        return ResponseEntity.ok(budget);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateBudgetRequest request) {

        BudgetResponse updatedBudget = budgetService.updateBudget(id, userId, request);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {

        budgetService.deleteBudget(id, userId);
        return ResponseEntity.noContent().build();
    }
}
