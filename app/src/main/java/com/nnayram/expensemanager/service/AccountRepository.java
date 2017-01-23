package com.nnayram.expensemanager.service;

import com.nnayram.expensemanager.model.Account;
import com.nnayram.expensemanager.model.AccountDistribution;
import com.nnayram.expensemanager.model.AccountTransaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Rufo on 1/14/2017.
 */
public interface AccountRepository {

    long addAccount(String type, String description) throws Exception;

    long addTransaction(long accountId, Date date, String type, String description, BigDecimal amount) throws Exception;

    long addDistribution(long accountId, long transactionId, String description, BigDecimal amount) throws Exception;

    long deleteAccount(long id) throws Exception;

    long deleteAccountTransaction(long id) throws Exception;

    List<Account> getAccounts();

    List<Account> getAccounts(String type);

    List<AccountTransaction> getTransactions(long accountId);

    List<AccountDistribution> getAccountDistributions(long accountId);

    List<AccountDistribution> getTranDistributions(long tranId);

    Account getAccount(long accountId);

    AccountTransaction getTransaction(long tranId);

    AccountDistribution getDistribution(long distId);

    BigDecimal getTotalAmount(long accountId);

}
