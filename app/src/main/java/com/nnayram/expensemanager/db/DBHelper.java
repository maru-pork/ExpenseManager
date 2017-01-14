package com.nnayram.expensemanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nnayram.expensemanager.model.Account;
import com.nnayram.expensemanager.model.AccountDistribution;
import com.nnayram.expensemanager.model.AccountTransaction;
import com.nnayram.expensemanager.service.AccountRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rufo on 1/14/2017.
 */
public class DBHelper extends SQLiteOpenHelper implements AccountRepository {

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
                DBContract.AccountDistribution.COLUMN_AMOUNT + " REAL NOT NULL," +
                DBContract.AccountDistribution.COLUMN_DESCRIPTION + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("LOG_TAG", "Updating database from version " + oldVersion  + " to " + newVersion + " which will destroy all data.");
        // drop table if upgraded
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Account.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.AccountTransaction.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.AccountDistribution.TABLE_NAME);
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
    public long addTransaction(long accountId, String type, String description, BigDecimal amount) {
        return 0;
    }

    @Override
    public long addDistribution(long accountId, String description, BigDecimal amount) {
        return 0;
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
        return null;
    }

    @Override
    public List<AccountDistribution> getDistributions(long accountId) {
        return null;
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
    public BigDecimal getTotal(long accountId) {
        return null;
    }

    private Account constructAccountFrCursor(Cursor cursor) {
        Account account = new Account();
        account.setId(cursor.getLong(cursor.getColumnIndex(DBContract.Account._ID)));
        account.setType(cursor.getString(cursor.getColumnIndex(DBContract.Account.COLUMN_TYPE)));
        account.setDescription(cursor.getString(cursor.getColumnIndex(DBContract.Account.COLUMN_DESCRIPTION)));
        return account;
    }
}
