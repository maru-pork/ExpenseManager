package com.nnayram.expensemanager.core;

/**
 * Created by Rufo on 1/17/2017.
 */
public class AccountModel {

    private String accountName;
    private String balance;

    public AccountModel(String accountName, String balance) {
        this.accountName = accountName;
        this.balance = balance;
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
