package com.nnayram.expensemanager.core;

/**
 * Created by Rufo on 1/23/2017.
 */
public class BudgetModel {

    private Long id;
    private String description;
    private String balance;

    public BudgetModel(Long id, String description, String balance) {
        this.id = id;
        this.description = description;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
