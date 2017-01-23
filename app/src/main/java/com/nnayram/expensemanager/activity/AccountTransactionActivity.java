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
import com.nnayram.expensemanager.model.Account;
import com.nnayram.expensemanager.model.AccountTransaction;
import com.nnayram.expensemanager.util.DateUtil;
import com.nnayram.expensemanager.util.NumberUtil;
import com.nnayram.expensemanager.view.DialogAddAccountTran;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rufo on 1/22/2017.
 */
public class AccountTransactionActivity extends BaseActivity implements View.OnClickListener {

    private Account account;

    private TextView tvBalance;

    private TextView tvPageCount;
    private TableLayout tblMain;
    private TableRow trTranHeader, trTranRow;
    private TextView tvTranRowID, tvTranRow;
    private LinearLayout lytLineSeparator;

    private Pageable<AccountTransaction> pageableAccount;
    private List<AccountTransaction> accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_tran);

        Long accountId = getIntent().getLongExtra("ACCOUNT_ID", 0L);
        account = ExpenseApplication.getInstance().getDbReader().getAccount(Long.valueOf(accountId));

        setTitle("Account : " + account.getDescription().toUpperCase());
        super.initializeToolbar();
        super.setCurrentContext(this);
        init();
    }

    private void init() {
        tblMain = (TableLayout) findViewById(R.id.tbl_account_trans);
        trTranHeader = (TableRow) findViewById(R.id.tr_acc_tranHeader);
        tvTranRowID = (TextView) findViewById(R.id.tv_acc_tranRow);
        tvBalance = (TextView) findViewById(R.id.tv_account_balance);
        tvPageCount = (TextView) findViewById(R.id.tv_acc_tranPageCount);

        // init listeners
        findViewById(R.id.btn_account_add_tran).setOnClickListener(this);
        findViewById(R.id.btn_account_delete).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_previous).setOnClickListener(this);

        // init date
        refreshDisplay();
    }

    private void refreshDisplay() {
        account = ExpenseApplication.getInstance().getDbReader().getAccount(account.getId());
        tvBalance.setText(NumberUtil.format().format(account.getTotalAmount()));

        accountList = new ArrayList<>();
        accountList.addAll(ExpenseApplication.getInstance().getDbReader().getTransactions(account.getId()));

        pageableAccount = new Pageable<>(accountList);
        pageableAccount.setPageSize(10);
        pageableAccount.setPage(1);

        tvPageCount.setText(getString(R.string.page_of, pageableAccount.getPage(), pageableAccount.getMaxPages()));

        refreshMainTable();
    }

    private void refreshMainTable() {
        tblMain.removeAllViews();
        tblMain.addView(trTranHeader);
        for (final AccountTransaction transaction : pageableAccount.getListForPage()) {
            trTranRow = new TableRow(this);
            trTranRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            trTranRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccountTransactionActivity.this);
                    alertDialogBuilder.setMessage("Transaction[" + transaction.getType() + "=" + transaction.getFormattedAmount()+"]" + " demands action. Choose one.");
                    alertDialogBuilder.setPositiveButton("Distribute", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ExpenseApplication.getInstance().getDbReader().deleteAccountTransaction(transaction.getId());
                            refreshDisplay();
                        }
                    });
                    alertDialogBuilder.show();
                    return false;
                }
            });

            // Transaction Date
            tvTranRow = new TextView(this);
            tvTranRow.setLayoutParams(tvTranRowID.getLayoutParams());
            tvTranRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tvTranRow.setText(DateFormat.format(DateUtil.DATE_PATTERN, transaction.getDate()));
            trTranRow.addView(tvTranRow);

            // Transaction Type
            tvTranRow = new TextView(this);
            tvTranRow.setLayoutParams(tvTranRowID.getLayoutParams());
            tvTranRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tvTranRow.setText(transaction.getType());
            trTranRow.addView(tvTranRow);

            // Description
            tvTranRow = new TextView(this);
            tvTranRow.setLayoutParams(tvTranRowID.getLayoutParams());
            tvTranRow.setGravity(Gravity.LEFT);
            tvTranRow.setText(transaction.getDescription());
            trTranRow.addView(tvTranRow);

            // Amount
            tvTranRow = new TextView(this);
            tvTranRow.setLayoutParams(tvTranRowID.getLayoutParams());
            tvTranRow.setGravity(Gravity.RIGHT);
            tvTranRow.setText(transaction.getFormattedAmount());
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
                pageableAccount.setPage(pageableAccount.getNextPage());
                if (!accountList.isEmpty()) {
                    trTranRow.removeAllViews();
                }
                refreshMainTable();
                tvPageCount.setText(getString(R.string.page_of, pageableAccount.getPage(), pageableAccount.getMaxPages()));
                break;

            case R.id.btn_previous:
                pageableAccount.setPage(pageableAccount.getPreviousPage());
                if (!accountList.isEmpty()) {
                    trTranRow.removeAllViews();
                }
                refreshMainTable();
                tvPageCount.setText(getString(R.string.page_of, pageableAccount.getPage(), pageableAccount.getMaxPages()));
                break;

            case R.id.btn_account_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to remove account: " + account.getDescription()  +"?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExpenseApplication.getInstance().getDbReader().deleteAccount(account.getId());

                        Intent intent = new Intent(AccountTransactionActivity.this, AccountActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialogBuilder.setNegativeButton("Close", null);
                alertDialogBuilder.show();
                break;

            case R.id.btn_account_add_tran:
                new DialogAddAccountTran(this){

                    @Override
                    public void setAddOnClickAction(AccountTransaction transaction) {
                        ExpenseApplication.getInstance().getDbReader().addTransaction(account.getId(), transaction.getDate(), transaction.getType(), transaction.getDescription(), transaction.getAmount());
                        dismiss();
                        refreshDisplay();
                    }
                }.show();
                break;


        }
    }
}
