package com.nnayram.expensemanager.model;

import java.math.BigDecimal;

/**
 * Created by Rufo on 1/14/2017.
 */
public class AccountDistribution {

    private Long id;
    private String description;
    private BigDecimal amount;
    private Account account;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

