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
import com.nnayram.expensemanager.model.BudgetDetail;
import com.nnayram.expensemanager.model.TranType;
import com.nnayram.expensemanager.util.DateUtil;
import com.nnayram.expensemanager.util.NumberUtil;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Rufo on 1/23/2017.
 */
public abstract class DialogAddBudgetDetail extends Dialog implements View.OnClickListener {

    private Activity m;

    private EditText etBudgetDate;
    private Spinner spBudgetType;
    private EditText etBudgetDescription;
    private EditText etBudgetAmount;

    public DialogAddBudgetDetail(Activity activity) {
        super(activity);
        this.m = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_budget_add_detail);

        etBudgetDate = (EditText) findViewById(R.id.et_budget_Date);
        spBudgetType = (Spinner) findViewById(R.id.sp_budget_type);
        etBudgetDescription = (EditText) findViewById(R.id.et_budget_description);
        etBudgetAmount = (EditText) findViewById(R.id.et_budget_amount);

        etBudgetDate.setInputType(InputType.TYPE_NULL);
        etBudgetDate.setText(DateFormat.format(DateUtil.DATE_PATTERN, Calendar.getInstance().getTime()));
        spBudgetType.setAdapter(new ArrayAdapter<>(m, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(TranType.values())));

        etBudgetDate.setOnClickListener(this);
        findViewById(R.id.btn_budget_add).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_budget_Date:
                final Calendar currentCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        m,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                currentCalendar.set(Calendar.YEAR, year);
                                currentCalendar.set(Calendar.MONTH, monthOfYear);
                                currentCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                etBudgetDate.setText(DateFormat.format(DateUtil.DATE_PATTERN, currentCalendar.getTime()));
                            }
                        },
                        currentCalendar.get(Calendar.YEAR),
                        currentCalendar.get(Calendar.MONTH),
                        currentCalendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
                break;
            case R.id.btn_budget_add:
                BudgetDetail budgetDetail = new BudgetDetail();
                budgetDetail.setDate(DateUtil.convertString(String.valueOf(etBudgetDate.getText())));
                budgetDetail.setType(String.valueOf(spBudgetType.getSelectedItem()));
                budgetDetail.setDescription(String.valueOf(etBudgetDescription.getText()));
                budgetDetail.setAmount(NumberUtil.getBigDecimalIfExists(String.valueOf(etBudgetAmount.getText())));

                setAddOnClickAction(budgetDetail);
                break;
        }
    }

    public abstract void setAddOnClickAction(BudgetDetail budgetDetail);
}
