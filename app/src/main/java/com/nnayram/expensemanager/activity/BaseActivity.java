package com.nnayram.expensemanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.core.ExpenseApplication;

/**
 * Created by Rufo on 1/14/2017.
 */
public class BaseActivity extends AppCompatActivity {

    ExpenseApplication expenseApplication = ExpenseApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCurrentContext(Context context) {
        expenseApplication.setCurrentContext(context);
    }

    public void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExpenseApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExpenseApplication.activityPaused();
    }


}
