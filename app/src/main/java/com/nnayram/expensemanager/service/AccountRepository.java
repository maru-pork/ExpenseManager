package com.nnayram.expensemanager.service;

import com.nnayram.expensemanager.model.Account;
import com.nnayram.expensemanager.model.AccountDistribution;
import com.nnayram.expensemanager.model.AccountTransaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Rufo on 1/14/2017.
 */
public interface AccountRepository {

    long addAccount(String type, String description) throws Exception;

    long addTransaction(long accountId, String type, String description, BigDecimal amount) throws Exception;

    long addDistribution(long accountId, String description, BigDecimal amount) throws Exception;

    List<Account> getAccounts();

    List<Account> getAccounts(String type);

    List<AccountTransaction> getTransactions(long accountId);

    List<AccountDistribution> getDistributions(long accountId);

    Account getAccount(long accountId);

    BigDecimal getTotal(long accountId);

}
