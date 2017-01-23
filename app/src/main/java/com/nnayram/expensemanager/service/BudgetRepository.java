package com.nnayram.expensemanager.service;

import com.nnayram.expensemanager.model.Budget;
import com.nnayram.expensemanager.model.BudgetDetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Rufo on 1/23/2017.
 */
public interface BudgetRepository {

    long addBudget(String description);

    long addBudgetDetail(long budgetId, Date date, String type, String description, BigDecimal amount);

    long deleteBudget(long id);

    long deleteBudgetDetail(long id);

    List<Budget> getBudgets();

    List<BudgetDetail> getBudgetDetails(long budgetId);

    Budget getBudget(long id);

    BudgetDetail getBudgetDetail(long id);

    BigDecimal getTotalBudgetAmount(long budgetId);

}
