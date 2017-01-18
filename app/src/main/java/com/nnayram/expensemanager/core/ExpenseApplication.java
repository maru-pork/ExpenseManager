package com.nnayram.expensemanager.core;

import android.app.Application;
import android.content.Context;

import com.nnayram.expensemanager.db.DBHelper;

import java.util.Properties;

/**
 * Created by Rufo on 1/14/2017.
 */
public class ExpenseApplication extends Application {

    private static ExpenseApplication sInstance;
    private static boolean activityVisible;

    private Properties properties;
    private final String filename = "application.properties";

    private Context currentContext;
    private DBHelper dbReader;

    public synchronized static ExpenseApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        dbReader = DBHelper.getInstance(getBaseContext());
    }

    public DBHelper getDbReader() {
        return dbReader;
    }

    public Context getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}
