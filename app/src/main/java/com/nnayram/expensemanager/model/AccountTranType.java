package com.nnayram.expensemanager.model;

/**
 * Created by Rufo on 1/14/2017.
 */
public enum AccountTranType {
    DEPOSIT, WITHDRAWAL, PROFIT, LOSS;

    public static String[] getDepositType() {
        return new String[]{DEPOSIT.name(), PROFIT.name()};
    }

    public static String[] getWithdrawalType() {
        return new String[]{WITHDRAWAL.name(), LOSS.name()};
    }
}
