package com.nnayram.expensemanager.model;

import com.nnayram.expensemanager.util.NumberUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Rufo on 1/23/2017.
 */
public class BudgetDetail {

    private Long id;
    private Date date;
    private String type;
    private String description;
    private BigDecimal amount;
    private Budget budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return NumberUtil.format().format(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}
