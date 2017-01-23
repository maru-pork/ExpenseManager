package com.nnayram.expensemanager.core;

/**
 * Created by Rufo on 1/17/2017.
 */
public class AccountModel {

    private Long id;
    private String accountName;
    private String balance;

    public AccountModel(Long id, String accountName, String balance) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
