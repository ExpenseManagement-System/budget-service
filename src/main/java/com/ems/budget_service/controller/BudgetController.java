package com.ems.budget_service.controller;

import com.ems.budget_service.model.dto.BudgetResponse;
import com.ems.budget_service.model.dto.CreateBudgetRequest;
import com.ems.budget_service.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
