package com.nnayram.expensemanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.core.AccountAdapter;
import com.nnayram.expensemanager.core.AccountModel;
import com.nnayram.expensemanager.core.ExpenseApplication;
import com.nnayram.expensemanager.model.Account;
import com.nnayram.expensemanager.view.DialogAddAccount;

import java.util.ArrayList;

/**
 * Created by Rufo on 1/17/2017.
 * Src: http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private ListView accountListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        super.initializeToolbar();
        super.setCurrentContext(this);
        init();
    }

    private void init() {
        accountListView = (ListView) findViewById(R.id.lv_accounts);

        refreshAccountAdapter();

        // setup listeners
        findViewById(R.id.btn_account_add).setOnClickListener(this);
    }

    private void refreshAccountAdapter() {
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        for (Account account : ExpenseApplication.getInstance().getDbReader().getAccounts()) {
            accountModels.add(new AccountModel(account.getDescription(), account.getType()));
        }
        accountListView.setAdapter(new AccountAdapter(this, R.layout.account_info, accountModels));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_account_add:
                new DialogAddAccount(this) {
                    @Override
                    public void addAccount(String type, String name) {
                        try {
                            ExpenseApplication.getInstance().getDbReader().addAccount(type, name);
                            refreshAccountAdapter();
                            dismiss();
                        } catch (Exception e) {
                            Toast.makeText(AccountActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }.show();
                break;
        }
    }
}
