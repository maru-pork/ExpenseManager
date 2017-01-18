package com.nnayram.expensemanager.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.model.AccountType;

import java.util.Arrays;

/**
 * Created by Rufo on 1/18/2017.
 */
public abstract class DialogAddAccount extends Dialog implements View.OnClickListener {
    private Activity m;

    private Spinner spAccountType;
    private EditText etAccountName;

    public DialogAddAccount(Activity activity) {
        super(activity);
        this.m = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_account_add);

        spAccountType = (Spinner) findViewById(R.id.sp_account_type);
        etAccountName = (EditText) findViewById(R.id.et_account_name);

        spAccountType.setAdapter(new ArrayAdapter<>(m, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(AccountType.values())));

        findViewById(R.id.btn_account_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_account_add:
                // TODO add validation here
                Toast.makeText(m, spAccountType.getSelectedItem() + " " + etAccountName.getText(), Toast.LENGTH_SHORT).show();
                addAccount(spAccountType.getSelectedItem().toString(), etAccountName.getText().toString());
                break;

        }
    }

    public abstract void addAccount(String type, String name);
}
