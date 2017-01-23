package com.nnayram.expensemanager;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.nnayram.expensemanager.db.DBHelper;
import com.nnayram.expensemanager.model.Account;
import com.nnayram.expensemanager.model.AccountDistribution;
import com.nnayram.expensemanager.model.AccountTranType;
import com.nnayram.expensemanager.model.AccountTransaction;
import com.nnayram.expensemanager.model.AccountType;
import com.nnayram.expensemanager.service.AccountRepository;

import junit.framework.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Rufo on 1/14/2017.
 */
public class AccountRepositoryTest extends AndroidTestCase {

    private AccountRepository repository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        repository = new DBHelper(mContext, true);
    }

    public void testCreateDB() {
        SQLiteDatabase db = new DBHelper(mContext, true).getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }

    public void testAddAccount() throws Exception {
        long id1 = repository.addAccount(AccountType.SAVINGS.name(), "BDO");
        long id2 = repository.addAccount(AccountType.INVESTMENT.name(), "FarmOn");
        long id3 = repository.addAccount(AccountType.CONTRIBUTIONS.name(), "SSS");

        Assert.assertEquals(3, repository.getAccounts().size());
        Assert.assertEquals(1, repository.getAccounts(AccountType.SAVINGS.name()).size());
        Assert.assertEquals(1, repository.getAccounts(AccountType.INVESTMENT.name()).size());
        Assert.assertEquals(1, repository.getAccounts(AccountType.CONTRIBUTIONS.name()).size());

        assertAccount(repository.getAccount(id1), AccountType.SAVINGS.name(), "BDO");
        assertAccount(repository.getAccount(id2), AccountType.INVESTMENT.name(), "FarmOn");
        assertAccount(repository.getAccount(id3), AccountType.CONTRIBUTIONS.name(), "SSS");
    }

    public void testAddTransaction()  throws Exception {
        long bdoAccountId = repository.addAccount(AccountType.SAVINGS.name(), "BDO");

        long depositTran = repository.addTransaction(bdoAccountId, new Date(), AccountTranType.DEPOSIT.name(), "Mckinley", new BigDecimal("1000.00"));
        long withdrawalTran = repository.addTransaction(bdoAccountId, new Date(), AccountTranType.WITHDRAWAL.name(), "SMNorth", new BigDecimal("500.00"));
        long interestTran = repository.addTransaction(bdoAccountId, new Date(), AccountTranType.PROFIT.name(), "Online", new BigDecimal("2.00"));
        long taxesTran = repository.addTransaction(bdoAccountId, new Date(), AccountTranType.LOSS.name(), "Online", new BigDecimal("2.00"));

        Assert.assertEquals(4, repository.getTransactions(bdoAccountId).size());
        assertTransaction(repository.getTransaction(depositTran), bdoAccountId, new Date(), AccountTranType.DEPOSIT.name(), "Mckinley", new BigDecimal("1000.00"));
        assertTransaction(repository.getTransaction(withdrawalTran), bdoAccountId, new Date(), AccountTranType.WITHDRAWAL.name(), "SMNorth", new BigDecimal("500.00"));
        assertTransaction(repository.getTransaction(interestTran), bdoAccountId, new Date(), AccountTranType.PROFIT.name(), "Online", new BigDecimal("2.00"));
        assertTransaction(repository.getTransaction(taxesTran), bdoAccountId, new Date(), AccountTranType.LOSS.name(), "Online", new BigDecimal("2.00"));
    }

    public void testAddDistribution() throws Exception {
        long bdoAccountId = repository.addAccount(AccountType.SAVINGS.name(), "BDO");

        // distributions are for AccountTranType.DEPOSIT and AccountTranType.INTEREST_EARNED only
        long depositTran = repository.addTransaction(bdoAccountId, new Date(), AccountTranType.DEPOSIT.name(), "initial amount", new BigDecimal("1033.00"));
        long withdrawalTran = repository.addTransaction(bdoAccountId, new Date(), AccountTranType.WITHDRAWAL.name(), "SMNorth", new BigDecimal("500.00"));

        long id1 = repository.addDistribution(bdoAccountId, depositTran, "Maintaining Balance", new BigDecimal("2000.00"));
        long id2 = repository.addDistribution(bdoAccountId, depositTran, "PhilamLife", new BigDecimal("2000.00"));
        long id3 = repository.addDistribution(bdoAccountId, depositTran, "SunLife", new BigDecimal("6000.00"));
        long id4 = repository.addDistribution(bdoAccountId, depositTran, "Interest Earned", new BigDecimal("33.00"));

        Assert.assertEquals(4, repository.getAccountDistributions(bdoAccountId).size());
        Assert.assertEquals(4, repository.getTranDistributions(depositTran).size());
        Assert.assertEquals(0, repository.getTranDistributions(withdrawalTran).size());

        assertDistribution(repository.getDistribution(id1), bdoAccountId, depositTran, "Maintaining Balance", new BigDecimal("2000.00"));
        assertDistribution(repository.getDistribution(id2), bdoAccountId, depositTran, "PhilamLife", new BigDecimal("2000.00"));
        assertDistribution(repository.getDistribution(id3), bdoAccountId, depositTran, "SunLife", new BigDecimal("6000.00"));
        assertDistribution(repository.getDistribution(id4), bdoAccountId, depositTran, "Interest Earned", new BigDecimal("33.00"));
    }

    public void testDeleteAccount() throws Exception {
        long accountId = repository.addAccount(AccountType.INVESTMENT.name(), "FarmOn");
        assertNotNull(repository.getAccount(accountId));

        repository.deleteAccount(accountId);
        assertNull(repository.getAccount(accountId));
    }

    public void testDeleteAccountTransaction() throws Exception {
        long accountId = repository.addAccount(AccountType.INVESTMENT.name(), "FarmOn");

        long depositTran = repository.addTransaction(accountId, new Date(), AccountTranType.DEPOSIT.name(), "Mckinley", new BigDecimal("1000.00"));
        assertNotNull(repository.getTransaction(depositTran));

        repository.deleteAccountTransaction(depositTran);
        assertNull(repository.getTransaction(depositTran));
    }

    public void test() throws Exception {
        long accountId = repository.addAccount(AccountType.INVESTMENT.name(), "FarmOn");
        assertAccount(repository.getAccount(accountId), new BigDecimal("0.00"));

        accountId = repository.addAccount(AccountType.SAVINGS.name(), "BDO");
        repository.addTransaction(accountId, new Date(), AccountTranType.DEPOSIT.name(), "Mckinley", new BigDecimal("1000.00"));
        assertAccount(repository.getAccount(accountId), new BigDecimal("1000.00"));
        repository.addTransaction(accountId, new Date(), AccountTranType.WITHDRAWAL.name(), "SMNorth", new BigDecimal("300.00"));
        assertAccount(repository.getAccount(accountId), new BigDecimal("700.00"));
        repository.addTransaction(accountId, new Date(), AccountTranType.PROFIT.name(), "Online", new BigDecimal("2.00"));
        assertAccount(repository.getAccount(accountId), new BigDecimal("702.00"));
        repository.addTransaction(accountId, new Date(), AccountTranType.LOSS.name(), "Online", new BigDecimal("2.00"));
        assertAccount(repository.getAccount(accountId), new BigDecimal("700.00"));

        assertAccount(repository.getAccounts());
        assertAccount(repository.getAccounts(AccountType.INVESTMENT.name()));
        assertAccount(repository.getAccounts(AccountType.SAVINGS.name()));
    }

    private void assertAccount(List<Account> accounts) {
        for (Account account : accounts) {
            switch (account.getDescription()) {
                case "FarmOn":
                    assertAccount(account, new BigDecimal("0.00"));
                    break;
                case  "BDO":
                    assertAccount(account, new BigDecimal("700.00"));
                    break;
            }
        }
    }

    private void assertAccount(Account account, BigDecimal amount) {
        assertNotNull(account);
        assertEquals(amount, account.getTotalAmount());
    }

    private void assertAccount(Account account, String type, String description) {
        assertNotNull(account.getId());
        assertEquals(type, account.getType());
        assertEquals(description, account.getDescription());
    }

    private void assertTransaction(AccountTransaction transaction, Long accountId, Date date, String type, String description, BigDecimal amount) {
        assertNotNull(transaction.getId());
        assertEquals(accountId, transaction.getAccount().getId());
        assertEquals(date.toString(), transaction.getDate().toString());
        assertEquals(type, transaction.getType());
        assertEquals(description, transaction.getDescription());
        assertEquals(amount, transaction.getAmount());
    }

    private void assertDistribution(AccountDistribution distribution, Long accountId, Long tranId, String description, BigDecimal amount) {
        assertNotNull(distribution.getId());
        assertEquals(accountId, distribution.getAccount().getId());
        assertEquals(tranId, distribution.getAccountTransaction().getId());
        assertEquals(description, distribution.getDescription());
        assertEquals(amount, distribution.getAmount());
    }
}
