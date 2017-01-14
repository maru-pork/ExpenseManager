package com.nnayram.expensemanager.model;

import java.util.List;

/**
 * Created by Rufo on 1/14/2017.
 */
public class Account {

    private Long id;
    private String type;
    private String description;
    private List<AccountTransaction> transactions;
    private List<AccountDistribution> distributions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<AccountTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountTransaction> transactions) {
        this.transactions = transactions;
    }

    public List<AccountDistribution> getDistributions() {
        return distributions;
    }

    public void setDistributions(List<AccountDistribution> distributions) {
        this.distributions = distributions;
    }
}
