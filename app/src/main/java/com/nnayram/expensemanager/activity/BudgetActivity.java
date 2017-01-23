package com.nnayram.expensemanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.core.BudgetAdapter;
import com.nnayram.expensemanager.core.BudgetModel;
import com.nnayram.expensemanager.core.ExpenseApplication;
import com.nnayram.expensemanager.model.Budget;
import com.nnayram.expensemanager.view.DialogAdd;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by Rufo on 1/23/2017.
 */
public class BudgetActivity extends BaseActivity implements View.OnClickListener {

    private ListView budgetListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_budget);
        super.initializeToolbar();
        super.setCurrentContext(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        budgetListView = (ListView) findViewById(R.id.lv_budgets);
        refreshBudgetAdapter();

        findViewById(R.id.btn_budget_add).setOnClickListener(this);
    }

    private void refreshBudgetAdapter() {
        ArrayList<BudgetModel> budgetModels = new ArrayList<>();
        for (Budget budget : ExpenseApplication.getInstance().getDbReader().getBudgets()) {
            budgetModels.add(new BudgetModel(budget.getId(), budget.getDescription(), budget.getFormattedTotalAmount()));
        }
        budgetListView.setAdapter(new BudgetAdapter(this, R.layout.account_info, budgetModels));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_budget_add:
                new DialogAdd(this) {
                    @Override
                    public void setAddOnClickAction(String desc) {
                        try {
                            if (StringUtils.isEmpty(desc))
                                throw new IllegalArgumentException("Invalid input");

                            ExpenseApplication.getInstance().getDbReader().addBudget(desc);
                            refreshBudgetAdapter();
                            dismiss();
                        } catch (Exception e) {
                            Toast.makeText(BudgetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }.show();
                break;
        }

    }
}
