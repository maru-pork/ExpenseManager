package com.nnayram.expensemanager;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.nnayram.expensemanager.db.DBHelper;
import com.nnayram.expensemanager.model.Account;
import com.nnayram.expensemanager.model.AccountType;
import com.nnayram.expensemanager.service.AccountRepository;

import junit.framework.Assert;

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

        List<Account> accounts = repository.getAccounts();
        Assert.assertEquals(2, accounts.size());

        accounts = repository.getAccounts(AccountType.SAVINGS.name());
        Assert.assertEquals(1, accounts.size());

        accounts = repository.getAccounts(AccountType.INVESTMENT.name());
        Assert.assertEquals(1, accounts.size());

        assertAccount(repository.getAccount(id1), AccountType.SAVINGS.name(), "BDO");
        assertAccount(repository.getAccount(id2), AccountType.INVESTMENT.name(), "FarmOn");
    }

    private void assertAccount(Account account, String type, String description) {
        Assert.assertEquals(type, account.getType());
        Assert.assertEquals(description, account.getDescription());
    }
}
