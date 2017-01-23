package com.nnayram.expensemanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.nnayram.expensemanager.model.Account;
import com.nnayram.expensemanager.model.AccountDistribution;
import com.nnayram.expensemanager.model.AccountTranType;
import com.nnayram.expensemanager.model.AccountTransaction;
import com.nnayram.expensemanager.model.Budget;
import com.nnayram.expensemanager.model.BudgetDetail;
import com.nnayram.expensemanager.model.TranType;
import com.nnayram.expensemanager.service.AccountRepository;
import com.nnayram.expensemanager.service.BudgetRepository;
import com.nnayram.expensemanager.util.DateUtil;
import com.nnayram.expensemanager.util.NumberUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rufo on 1/14/2017.
 */
public class DBHelper extends SQLiteOpenHelper implements AccountRepository, BudgetRepository {

    private static boolean isTest;

    private static DBHelper sInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "expense_manager.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * constructor used for testing only
     * @param context
     * @param isTest
     */
    public DBHelper(Context context, boolean isTest) {
        super(context, null, null, DATABASE_VERSION);
        this.isTest = isTest;
    }

    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // for testing only
    public static synchronized DBHelper getInstanceForTest(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext(), true);
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // ACCOUNT
        db.execSQL("CREATE TABLE " + DBContract.Account.TABLE_NAME + " (" +
                DBContract.Account._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.Account.COLUMN_TYPE + " TEXT NOT NULL," +
                DBContract.Account.COLUMN_DESCRIPTION + " TEXT NOT NULL);");
        db.execSQL("CREATE TABLE " + DBContract.AccountTransaction.TABLE_NAME + " (" +
                DBContract.AccountTransaction._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.AccountTransaction.COLUMN_ACCOUNT + " INTEGER NOT NULL," +
                DBContract.AccountTransaction.COLUMN_DATE + " INTEGER NOT NULL," +
                DBContract.AccountTransaction.COLUMN_TYPE + " TEXT NOT NULL," +
                DBContract.AccountTransaction.COLUMN_AMOUNT + " REAL NOT NULL," +
                DBContract.AccountTransaction.COLUMN_DESCRIPTION + " TEXT NOT NULL);");
        db.execSQL("CREATE TABLE " + DBContract.AccountDistribution.TABLE_NAME + " (" +
                DBContract.AccountDistribution._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.AccountDistribution.COLUMN_ACCOUNT + " INTEGER NOT NULL," +
                DBContract.AccountDistribution.COLUMN_TRANSACTION + " INTEGER NOT NULL," +
                DBContract.AccountDistribution.COLUMN_AMOUNT + " REAL NOT NULL," +
                DBContract.AccountDistribution.COLUMN_DESCRIPTION + " TEXT NOT NULL);");
        // BUDGET
        db.execSQL("CREATE TABLE " + DBContract.Budget.TABLE_NAME + " (" +
                DBContract.Budget._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.Budget.COLUMN_DESCRIPTION + " TEXT NOT NULL);");
        db.execSQL("CREATE TABLE " + DBContract.BudgetDetail.TABLE_NAME + " (" +
                DBContract.BudgetDetail._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.BudgetDetail.COLUMN_BUDGET + " INTEGER NOT NULL," +
                DBContract.BudgetDetail.COLUMN_DATE + " INTEGER NOT NULL," +
                DBContract.BudgetDetail.COLUMN_TYPE + " TEXT NOT NULL," +
                DBContract.BudgetDetail.COLUMN_AMOUNT + " REAL NOT NULL," +
                DBContract.BudgetDetail.COLUMN_DESCRIPTION + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("LOG_TAG", "Updating database from version " + oldVersion  + " to " + newVersion + " which will destroy all data.");
        // drop table if upgraded
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Account.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.AccountTransaction.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.AccountDistribution.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Budget.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.BudgetDetail.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Closes database and cursor if they are not null.
     * @param sqLiteDatabase to be closed if not null
     * @param cursor         to be closed if not null
     */
    private synchronized void close(SQLiteDatabase sqLiteDatabase, Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
        close(sqLiteDatabase);
    }

    /**
     * Closes database if they are not null.
     * @param sqLiteDatabase
     */
    private synchronized void close(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            if (!isTest) sqLiteDatabase.close();
        }
    }

    @Override
    public long addAccount(String type, String description) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DBContract.Account.COLUMN_TYPE, type);
            cv.put(DBContract.Account.COLUMN_DESCRIPTION, description);
            result = db.insert(DBContract.Account.TABLE_NAME, null, cv);

            if(result!=0) db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }

    @Override
    public long addTransaction(long accountId, Date date, String type, String description, BigDecimal amount) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DBContract.AccountTransaction.COLUMN_ACCOUNT, accountId);
            cv.put(DBContract.AccountTransaction.COLUMN_DATE, date.getTime());
            cv.put(DBContract.AccountTransaction.COLUMN_TYPE, type);
            cv.put(DBContract.AccountTransaction.COLUMN_DESCRIPTION, description);
            cv.put(DBContract.AccountTransaction.COLUMN_AMOUNT, amount.toPlainString());
            result = db.insert(DBContract.AccountTransaction.TABLE_NAME, null, cv);

            if(result!=0) db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }

    @Override
    public long addDistribution(long accountId, long transactionId, String description, BigDecimal amount) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DBContract.AccountDistribution.COLUMN_ACCOUNT, accountId);
            cv.put(DBContract.AccountDistribution.COLUMN_TRANSACTION, transactionId);
            cv.put(DBContract.AccountDistribution.COLUMN_DESCRIPTION, description);
            cv.put(DBContract.AccountDistribution.COLUMN_AMOUNT, amount.toPlainString());
            result = db.insert(DBContract.AccountDistribution.TABLE_NAME, null, cv);

            if(result!=0) db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }

    @Override
    public long deleteAccount(long id) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            String[] whereArgs = new String[]{String.valueOf(id)};
            result = db.delete(DBContract.Account.TABLE_NAME, DBContract.Account._ID+"=?", whereArgs);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }

    @Override
    public long deleteAccountTransaction(long id) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            String[] whereArgs = new String[]{String.valueOf(id)};
            result = db.delete(DBContract.AccountTransaction.TABLE_NAME, DBContract.AccountTransaction._ID+"=?", whereArgs);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }
    @Override
    public List<Account> getAccounts() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Account> accounts = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.query(DBContract.Account.TABLE_NAME, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                accounts.add(constructAccountFrCursor(cursor));
            }
        } finally {
            close(db, cursor);
        }
        return accounts;
    }

    @Override
    public List<Account> getAccounts(String type) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Account> accounts = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.Account.COLUMN_TYPE + "=?";
            String[] selectionArgs = new String[]{type};
            cursor = db.query(DBContract.Account.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                accounts.add(constructAccountFrCursor(cursor));
            }
        } finally {
            close(db, cursor);
        }
        return accounts;
    }

    @Override
    public List<AccountTransaction> getTransactions(long accountId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<AccountTransaction> transactions = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.AccountTransaction.COLUMN_ACCOUNT + "=?";
            String[] selectionArgs = new String[]{String.valueOf(accountId)};
            cursor = db.query(DBContract.AccountTransaction.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                transactions.add(constructTransactionFrCursor(cursor));
            }
        } finally {
            close(db, cursor);
        }
        return transactions;
    }

    @Override
    public List<AccountDistribution> getAccountDistributions(long accountId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<AccountDistribution> distributions = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.AccountDistribution.COLUMN_ACCOUNT + "=?";
            String[] selectionArgs = new String[]{String.valueOf(accountId)};
            cursor = db.query(DBContract.AccountDistribution.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                distributions.add(constructDistributionFrCursor(cursor));
            }
        } finally {
            close(db, cursor);
        }
        return distributions;
    }

    @Override
    public List<AccountDistribution> getTranDistributions(long tranId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<AccountDistribution> distributions = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.AccountDistribution.COLUMN_TRANSACTION + "=?";
            String[] selectionArgs = new String[]{String.valueOf(tranId)};
            cursor = db.query(DBContract.AccountDistribution.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                distributions.add(constructDistributionFrCursor(cursor));
            }
        } finally {
            close(db, cursor);
        }
        return distributions;
    }

    @Override
    public Account getAccount(long accountId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Account account = null;
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.Account._ID + "=?";
            String[] selectionArgs = new String[] {String.valueOf(accountId)};
            cursor = db.query(DBContract.Account.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                account = constructAccountFrCursor(cursor);
            }
        } finally {
            close(db, cursor);
        }
        return account;
    }

    @Override
    public AccountTransaction getTransaction(long tranId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        AccountTransaction transaction = null;
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.AccountTransaction._ID + "=?";
            String[] selectionArgs = new String[] {String.valueOf(tranId)};
            cursor = db.query(DBContract.AccountTransaction.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                transaction = constructTransactionFrCursor(cursor);
            }
        } finally {
            close(db, cursor);
        }
        return transaction;
    }

    @Override
    public AccountDistribution getDistribution(long distId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        AccountDistribution distribution = null;
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.AccountDistribution._ID + "=?";
            String[] selectionArgs = new String[] {String.valueOf(distId)};
            cursor = db.query(DBContract.AccountDistribution.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                distribution = constructDistributionFrCursor(cursor);
            }
        } finally {
            close(db, cursor);
        }
        return distribution;
    }

    @Override
    public BigDecimal getTotalAmount(long accountId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        BigDecimal result = null;
        try {
            db = this.getReadableDatabase();

            String totalDeposit = "totalDeposit";
            String totalWithdrawal = "totalWithdrawal";

            String queryTotalDeposit = "SELECT COALESCE(SUM("+ DBContract.AccountTransaction.COLUMN_AMOUNT +"), 0) as " + totalDeposit + " FROM " + DBContract.AccountTransaction.TABLE_NAME +
                    " WHERE " + DBContract.AccountTransaction.COLUMN_TYPE + " IN('" + TextUtils.join("','", AccountTranType.getDepositType()) + "')" +
                    " AND "+ DBContract.AccountTransaction.COLUMN_ACCOUNT + " = " + accountId;
            String queryTotalWithdrawal = "SELECT COALESCE(SUM("+ DBContract.AccountTransaction.COLUMN_AMOUNT +"), 0) as " + totalWithdrawal + " FROM " + DBContract.AccountTransaction.TABLE_NAME +
                    " WHERE " + DBContract.AccountTransaction.COLUMN_TYPE + " IN('" + TextUtils.join("','", AccountTranType.getWithdrawalType()) + "')" +
                    " AND "+ DBContract.AccountTransaction.COLUMN_ACCOUNT + " = " + accountId;

            String col = "totalDeposit - totalWithdrawal";
            String[] cols = new String[]{col};
            String from = "(" + queryTotalDeposit + ") d,(" + queryTotalWithdrawal + ") w";
            String query = SQLiteQueryBuilder.buildQueryString(false, from, cols, null, null, null, null, null);

            /**
             * http://sqlfiddle.com/
             * SELECT totalCredit - totalPayment FROM
             *  (SELECT COALESCE(SUM(accTran_amount), 0) as totalDeposit FROM account_transaction WHERE accTran_type IN('DEPOSIT','PROFIT') AND acc_id = {accountId}) c,
             *  (SELECT COALESCE(SUM(accTran_amount), 0) as totalWithdrawal FROM account_transaction WHERE accTran_type IN('WITHDRAWAL', 'LOSS') AND acc_id = {accountId}) p
             */
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                result = NumberUtil.getBigDecimalIfExists(cursor.getFloat(cursor.getColumnIndex(col)));
            }

        }  finally {
            close(db, cursor);
        }
        return result;
    }

    private Account constructAccountFrCursor(Cursor cursor) {
        Account account = new Account();
        account.setId(cursor.getLong(cursor.getColumnIndex(DBContract.Account._ID)));
        account.setType(cursor.getString(cursor.getColumnIndex(DBContract.Account.COLUMN_TYPE)));
        account.setDescription(cursor.getString(cursor.getColumnIndex(DBContract.Account.COLUMN_DESCRIPTION)));

        account.setTotalAmount(getTotalAmount(account.getId()));
        return account;
    }

    private AccountTransaction constructTransactionFrCursor(Cursor cursor) {
        Account account = new Account();
        account.setId(cursor.getLong(cursor.getColumnIndex(DBContract.AccountTransaction.COLUMN_ACCOUNT)));

        AccountTransaction transaction = new AccountTransaction();
        transaction.setId(cursor.getLong(cursor.getColumnIndex(DBContract.AccountTransaction._ID)));
        transaction.setAccount(account);
        transaction.setDate(DateUtil.getDateOrThrow(cursor.getLong(cursor.getColumnIndex(DBContract.AccountTransaction.COLUMN_DATE))));
        transaction.setType(cursor.getString(cursor.getColumnIndex(DBContract.AccountTransaction.COLUMN_TYPE)));
        transaction.setDescription(cursor.getString(cursor.getColumnIndex(DBContract.AccountTransaction.COLUMN_DESCRIPTION)));
        transaction.setAmount(NumberUtil.getBigDecimalOrThrow(cursor.getFloat(cursor.getColumnIndex(DBContract.AccountTransaction.COLUMN_AMOUNT))));
        return transaction;
    }

    private AccountDistribution constructDistributionFrCursor(Cursor cursor) {
        Account account = new Account();
        account.setId(cursor.getLong(cursor.getColumnIndex(DBContract.AccountDistribution.COLUMN_ACCOUNT)));

        AccountTransaction transaction = new AccountTransaction();
        transaction.setId(cursor.getLong(cursor.getColumnIndex(DBContract.AccountDistribution.COLUMN_TRANSACTION)));

        AccountDistribution distribution = new AccountDistribution();
        distribution.setId(cursor.getLong(cursor.getColumnIndex(DBContract.AccountDistribution._ID)));
        distribution.setAccount(account);
        distribution.setAccountTransaction(transaction);
        distribution.setDescription(cursor.getString(cursor.getColumnIndex(DBContract.AccountDistribution.COLUMN_DESCRIPTION)));
        distribution.setAmount(NumberUtil.getBigDecimalOrThrow(cursor.getFloat(cursor.getColumnIndex(DBContract.AccountDistribution.COLUMN_AMOUNT))));
        return distribution;
    }

    /**
     * Budget
      */

    @Override
    public long addBudget(String description) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DBContract.Budget.COLUMN_DESCRIPTION, description);
            result = db.insert(DBContract.Budget.TABLE_NAME, null, cv);

            if(result!=0) db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }

    @Override
    public long addBudgetDetail(long budgetId, Date date, String type, String description, BigDecimal amount) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DBContract.BudgetDetail.COLUMN_BUDGET, budgetId);
            cv.put(DBContract.BudgetDetail.COLUMN_DATE, date.getTime());
            cv.put(DBContract.BudgetDetail.COLUMN_TYPE, type);
            cv.put(DBContract.BudgetDetail.COLUMN_DESCRIPTION, description);
            cv.put(DBContract.BudgetDetail.COLUMN_AMOUNT, amount.toPlainString());
            result = db.insert(DBContract.BudgetDetail.TABLE_NAME, null, cv);

            if(result!=0) db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }

    @Override
    public long deleteBudget(long id) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            String[] whereArgs = new String[]{String.valueOf(id)};

            // delete first BudgetDetail
            db.delete(DBContract.BudgetDetail.TABLE_NAME, DBContract.BudgetDetail.COLUMN_BUDGET+"=?", whereArgs);
            // then delete Budget
            result = db.delete(DBContract.Budget.TABLE_NAME, DBContract.Budget._ID+"=?", whereArgs);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }

    @Override
    public long deleteBudgetDetail(long id) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            String[] whereArgs = new String[]{String.valueOf(id)};
            result = db.delete(DBContract.BudgetDetail.TABLE_NAME, DBContract.BudgetDetail._ID+"=?", whereArgs);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            close(db);
        }
        return result;
    }

    @Override
    public List<Budget> getBudgets() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Budget> budgets = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.query(DBContract.Budget.TABLE_NAME, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                budgets.add(constructBudgetFrCursor(cursor));
            }
        } finally {
            close(db, cursor);
        }
        return budgets;
    }

    @Override
    public List<BudgetDetail> getBudgetDetails(long budgetId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<BudgetDetail> budgetDetails = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.BudgetDetail.COLUMN_BUDGET + "=?";
            String[] selectionArgs = new String[] {String.valueOf(budgetId)};
            cursor = db.query(DBContract.BudgetDetail.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                budgetDetails.add(constructBudgetDetailFrCursor(cursor));
            }
        } finally {
            close(db, cursor);
        }
        return budgetDetails;
    }

    @Override
    public Budget getBudget(long id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Budget budget = null;
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.Budget._ID + "=?";
            String[] selectionArgs = new String[] {String.valueOf(id)};
            cursor = db.query(DBContract.Budget.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                budget = constructBudgetFrCursor(cursor);
            }
        } finally {
            close(db, cursor);
        }
        return budget;
    }

    @Override
    public BudgetDetail getBudgetDetail(long id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        BudgetDetail budgetDetail = null;
        try {
            db = this.getReadableDatabase();
            String selection = DBContract.BudgetDetail._ID + "=?";
            String[] selectionArgs = new String[] {String.valueOf(id)};
            cursor = db.query(DBContract.BudgetDetail.TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                budgetDetail = constructBudgetDetailFrCursor(cursor);
            }
        } finally {
            close(db, cursor);
        }
        return budgetDetail;
    }

    @Override
    public BigDecimal getTotalBudgetAmount(long budgetId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        BigDecimal result = null;
        try {
            db = this.getReadableDatabase();

            String totalPlus = "totalPlus";
            String totalMinus = "totalMinus";

            String queryTotalPlus = "SELECT COALESCE(SUM("+ DBContract.BudgetDetail.COLUMN_AMOUNT +"), 0) as " + totalPlus + " FROM " + DBContract.BudgetDetail.TABLE_NAME +
                    " WHERE " + DBContract.BudgetDetail.COLUMN_TYPE + "='" + TranType.PLUS.name() + "'" +
                    " AND "+ DBContract.BudgetDetail.COLUMN_BUDGET + "=" + budgetId;
            String queryTotalMinus = "SELECT COALESCE(SUM("+ DBContract.BudgetDetail.COLUMN_AMOUNT +"), 0) as " + totalMinus + " FROM " + DBContract.BudgetDetail.TABLE_NAME +
                    " WHERE " + DBContract.BudgetDetail.COLUMN_TYPE + "='" + TranType.MINUS.name() + "'" +
                    " AND "+ DBContract.BudgetDetail.COLUMN_BUDGET + "=" + budgetId;

            String col = totalPlus + "-" + totalMinus;
            String[] cols = new String[]{col};
            String from = "(" + queryTotalPlus + ") d,(" + queryTotalMinus + ") w";
            String query = SQLiteQueryBuilder.buildQueryString(false, from, cols, null, null, null, null, null);

            /**
             * http://sqlfiddle.com/
             * SELECT totalPlus - totalMinus FROM
             *  (SELECT COALESCE(SUM(budget_amount), 0) as totalDeposit FROM budget_dtl WHERE budget_type = 'PLUS' AND budget_id = {budgetId}) c,
             *  (SELECT COALESCE(SUM(budget_amount), 0) as totalWithdrawal FROM budget_dtl WHERE budget_type = 'MINUS' AND budget_id = {budgetId}) p
             */
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                result = NumberUtil.getBigDecimalIfExists(cursor.getFloat(cursor.getColumnIndex(col)));
            }

        }  finally {
            close(db, cursor);
        }
        return result;
    }

    private Budget constructBudgetFrCursor(Cursor cursor) {
        Budget budget = new Budget();
        budget.setId(cursor.getLong(cursor.getColumnIndex(DBContract.Budget._ID)));
        budget.setDescription(cursor.getString(cursor.getColumnIndex(DBContract.Budget.COLUMN_DESCRIPTION)));

        budget.setTotalAmount(getTotalBudgetAmount(budget.getId()));

        return budget;
    }

    private BudgetDetail constructBudgetDetailFrCursor(Cursor cursor) {
        Budget budget = new Budget();
        budget.setId(cursor.getLong(cursor.getColumnIndex(DBContract.BudgetDetail.COLUMN_BUDGET)));

        BudgetDetail budgetDetail = new BudgetDetail();
        budgetDetail.setId(cursor.getLong(cursor.getColumnIndex(DBContract.BudgetDetail._ID)));
        budgetDetail.setBudget(budget);
        budgetDetail.setDate(DateUtil.getDateOrThrow(cursor.getLong(cursor.getColumnIndex(DBContract.BudgetDetail.COLUMN_DATE))));
        budgetDetail.setType(cursor.getString(cursor.getColumnIndex(DBContract.BudgetDetail.COLUMN_TYPE)));
        budgetDetail.setDescription(cursor.getString(cursor.getColumnIndex(DBContract.BudgetDetail.COLUMN_DESCRIPTION)));
        budgetDetail.setAmount(NumberUtil.getBigDecimalOrThrow(cursor.getFloat(cursor.getColumnIndex(DBContract.BudgetDetail.COLUMN_AMOUNT))));
        return budgetDetail;
    }
}
