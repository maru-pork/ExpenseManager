package com.nnayram.expensemanager.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.nnayram.expensemanager.R;


/**
 * Created by Rufo on 1/18/2017.
 */
public abstract class DialogAdd extends Dialog implements View.OnClickListener{
    private Activity m;

    private EditText etAccountName;

    public DialogAdd(Activity activity) {
        super(activity);
        this.m = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_budget_add);

        etAccountName = (EditText) findViewById(R.id.et_name);
        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                setAddOnClickAction(etAccountName.getText().toString());
                break;
        }
    }

    public abstract void setAddOnClickAction(String desc);
}
