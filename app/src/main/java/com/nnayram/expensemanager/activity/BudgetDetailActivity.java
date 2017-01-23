package com.nnayram.expensemanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.core.ExpenseApplication;
import com.nnayram.expensemanager.core.Pageable;
import com.nnayram.expensemanager.model.Budget;
import com.nnayram.expensemanager.model.BudgetDetail;
import com.nnayram.expensemanager.util.DateUtil;
import com.nnayram.expensemanager.view.DialogAddBudgetDetail;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rufo on 1/23/2017.
 */
public class BudgetDetailActivity extends BaseActivity implements View.OnClickListener {

    private Budget budget;

    private TextView tvBalance;

    private TextView tvPageCount;
    private TableLayout tblMain;
    private TableRow trTranHeader, trTranRow;
    private TextView tvTranRowID, tvTranRow;
    private LinearLayout lytLineSeparator;

    private Pageable<BudgetDetail> pageableBudgetDetail;
    private List<BudgetDetail> budgetDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_detail);

        Long budgetId = getIntent().getLongExtra("BUDGET_ID", 0L);
        budget = ExpenseApplication.getInstance().getDbReader().getBudget(Long.valueOf(budgetId));

        setTitle(budget.getDescription().toUpperCase());
        super.initializeToolbar();
        super.setCurrentContext(this);
        init();
    }

    private void init() {
        tblMain = (TableLayout) findViewById(R.id.tbl_budget_details);
        trTranHeader = (TableRow) findViewById(R.id.tr_budget_tranHeader);
        tvTranRowID = (TextView) findViewById(R.id.tv_budget_tranRow);
        tvBalance = (TextView) findViewById(R.id.tv_budget_balance);
        tvPageCount = (TextView) findViewById(R.id.tv_budget_tranPageCount);

        findViewById(R.id.btn_budget_delete).setOnClickListener(this);
        findViewById(R.id.btn_budget_add_detail).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_previous).setOnClickListener(this);

        refreshDisplay();
    }

    private void refreshDisplay() {
        budget = ExpenseApplication.getInstance().getDbReader().getBudget(Long.valueOf(budget.getId()));
        tvBalance.setText(budget.getFormattedTotalAmount());

        budgetDetailList = new ArrayList<>();
        budgetDetailList.addAll(ExpenseApplication.getInstance().getDbReader().getBudgetDetails(budget.getId()));

        pageableBudgetDetail = new Pageable<>(budgetDetailList);
        pageableBudgetDetail.setPageSize(10);
        pageableBudgetDetail.setPage(1);

        tvPageCount.setText(getString(R.string.page_of, pageableBudgetDetail.getPage(), pageableBudgetDetail.getMaxPages()));

        refreshMainTable();
    }

    private void refreshMainTable() {
        tblMain.removeAllViews();
        tblMain.addView(trTranHeader);
        for (final BudgetDetail budgetDetail : pageableBudgetDetail.getListForPage()) {
            trTranRow = new TableRow(this);
            trTranRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            trTranRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BudgetDetailActivity.this);
                    alertDialogBuilder.setMessage("Are you sure you want to delete detail? " + budgetDetail.getDescription() + "["+ budgetDetail.getFormattedAmount() +"]");
                    alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ExpenseApplication.getInstance().getDbReader().deleteBudgetDetail(budgetDetail.getId());
                            refreshDisplay();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Close", null);
                    alertDialogBuilder.show();
                    return false;
                }
            });

            // Transaction Date
            tvTranRow = new TextView(this);
            tvTranRow.setLayoutParams(tvTranRowID.getLayoutParams());
            tvTranRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tvTranRow.setText(DateFormat.format(DateUtil.DATE_PATTERN, budgetDetail.getDate()));
            trTranRow.addView(tvTranRow);

            // Transaction Type
            tvTranRow = new TextView(this);
            tvTranRow.setLayoutParams(tvTranRowID.getLayoutParams());
            tvTranRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tvTranRow.setText(budgetDetail.getType());
            trTranRow.addView(tvTranRow);

            // Description
            tvTranRow = new TextView(this);
            tvTranRow.setLayoutParams(tvTranRowID.getLayoutParams());
            tvTranRow.setGravity(Gravity.LEFT);
            tvTranRow.setText(budgetDetail.getDescription());
            trTranRow.addView(tvTranRow);

            // Amount
            tvTranRow = new TextView(this);
            tvTranRow.setLayoutParams(tvTranRowID.getLayoutParams());
            tvTranRow.setGravity(Gravity.RIGHT);
            tvTranRow.setText(budgetDetail.getFormattedAmount());
            trTranRow.addView(tvTranRow);

            // Line Separator
            lytLineSeparator = new LinearLayout(this);
            lytLineSeparator.setOrientation(LinearLayout.VERTICAL);
            lytLineSeparator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            lytLineSeparator.setBackgroundColor(Color.parseColor("#5e7974"));

            tblMain.addView(trTranRow);
            tblMain.addView(lytLineSeparator);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                pageableBudgetDetail.setPage(pageableBudgetDetail.getNextPage());
                if (!budgetDetailList.isEmpty()) {
                    trTranRow.removeAllViews();
                }
                refreshMainTable();
                tvPageCount.setText(getString(R.string.page_of, pageableBudgetDetail.getPage(), pageableBudgetDetail.getMaxPages()));
                break;

            case R.id.btn_previous:
                pageableBudgetDetail.setPage(pageableBudgetDetail.getPreviousPage());
                if (!budgetDetailList.isEmpty()) {
                    trTranRow.removeAllViews();
                }
                refreshMainTable();
                tvPageCount.setText(getString(R.string.page_of, pageableBudgetDetail.getPage(), pageableBudgetDetail.getMaxPages()));
                break;

            case R.id.btn_budget_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to remove budget: " + budget.getDescription()  +"?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExpenseApplication.getInstance().getDbReader().deleteBudget(budget.getId());

                        Intent intent = new Intent(BudgetDetailActivity.this, BudgetActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialogBuilder.setNegativeButton("Close", null);
                alertDialogBuilder.show();
                break;

            case R.id.btn_budget_add_detail:
                new DialogAddBudgetDetail(this) {
                    @Override
                    public void setAddOnClickAction(BudgetDetail budgetDetail) {
                        try {
                            if (StringUtils.isEmpty(budgetDetail.getType())
                                    || StringUtils.isEmpty(budgetDetail.getDescription())
                                    || budgetDetail.getAmount().compareTo(BigDecimal.ZERO) == 0)
                                throw new IllegalArgumentException("Invalid input.");

                            ExpenseApplication.getInstance().getDbReader().addBudgetDetail(
                                    budget.getId(),
                                    budgetDetail.getDate(),
                                    budgetDetail.getType(),
                                    budgetDetail.getDescription(),
                                    budgetDetail.getAmount());
                            refreshDisplay();
                            dismiss();

                        } catch (Exception e) {
                            Toast.makeText(BudgetDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }.show();
                break;
        }

    }
}
