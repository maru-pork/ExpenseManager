package com.nnayram.expensemanager.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.model.AccountTranType;
import com.nnayram.expensemanager.model.AccountTransaction;
import com.nnayram.expensemanager.util.DateUtil;
import com.nnayram.expensemanager.util.NumberUtil;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Rufo on 1/22/2017.
 */
public abstract class DialogAddAccountTran extends Dialog implements View.OnClickListener {

    private Activity m;

    private EditText etAccTranDate;
    private Spinner spAccTranType;
    private EditText etAccTranDescription;
    private EditText etAccTranAmount;

    public DialogAddAccountTran(Activity activity) {
        super(activity);
        this.m = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_account_add_tran);

        etAccTranDate = (EditText) findViewById(R.id.et_accTran_Date);
        spAccTranType = (Spinner) findViewById(R.id.sp_accTran_type);
        etAccTranDescription = (EditText) findViewById(R.id.et_accTran_description);
        etAccTranAmount = (EditText) findViewById(R.id.et_accTran_amount);

        etAccTranDate.setInputType(InputType.TYPE_NULL);
        etAccTranDate.setText(DateFormat.format(DateUtil.DATE_PATTERN, Calendar.getInstance().getTime()));
        spAccTranType.setAdapter(new ArrayAdapter<>(m, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(AccountTranType.values())));

        //init listeners
        etAccTranDate.setOnClickListener(this);
        findViewById(R.id.btn_accTran_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_accTran_Date:
                final Calendar currentCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        m,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                currentCalendar.set(Calendar.YEAR, year);
                                currentCalendar.set(Calendar.MONTH, monthOfYear);
                                currentCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                etAccTranDate.setText(DateFormat.format(DateUtil.DATE_PATTERN, currentCalendar.getTime()));
                            }
                        },
                        currentCalendar.get(Calendar.YEAR),
                        currentCalendar.get(Calendar.MONTH),
                        currentCalendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
                break;

            case R.id.btn_accTran_add:
                AccountTransaction transaction = new AccountTransaction();
                transaction.setDate(DateUtil.convertString(String.valueOf(etAccTranDate.getText())));
                transaction.setType(String.valueOf(spAccTranType.getSelectedItem()));
                transaction.setDescription(String.valueOf(etAccTranDescription.getText()));
                transaction.setAmount(NumberUtil.getBigDecimalIfExists(String.valueOf(etAccTranAmount.getText())));

                setAddOnClickAction(transaction);
                break;
        }
    }

    public abstract void setAddOnClickAction(AccountTransaction transaction);
}
