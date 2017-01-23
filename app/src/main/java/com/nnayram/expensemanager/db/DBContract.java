package com.nnayram.expensemanager.db;

import android.provider.BaseColumns;

/**
 * Created by Rufo on 1/14/2017.
 */
public class DBContract {

    private DBContract() {

    }

    // ACCOUNT
    public static abstract class Account implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_TYPE = "acc_type";
        public static final String COLUMN_DESCRIPTION = "acc_desc";
    }

    public static abstract class AccountTransaction implements BaseColumns {
        public static final String TABLE_NAME = "account_transaction";
        public static final String COLUMN_ACCOUNT = "acc_id";
        public static final String COLUMN_DATE = "accTran_date";
        public static final String COLUMN_TYPE = "accTran_type";
        public static final String COLUMN_DESCRIPTION = "accTran_desc";
        public static final String COLUMN_AMOUNT = "accTran_amount";
    }

    public static abstract class AccountDistribution implements BaseColumns {
        public static final String TABLE_NAME = "account_distribution";
        public static final String COLUMN_ACCOUNT = "acc_id";
        public static final String COLUMN_TRANSACTION = "accTran_id";
        public static final String COLUMN_DESCRIPTION = "accDist_desc";
        public static final String COLUMN_AMOUNT = "accDist_amount";
    }

    // BUDGET
    public static abstract class Budget implements BaseColumns {
        public static final String TABLE_NAME = "budget";
        public static final String COLUMN_DESCRIPTION = "budget_desc";
    }

    public static abstract class BudgetDetail implements BaseColumns {
        public static final String TABLE_NAME = "budget_dtl";
        public static final String COLUMN_BUDGET = "budget_id";
        public static final String COLUMN_DATE = "budget_date";
        public static final String COLUMN_TYPE = "budget_type";
        public static final String COLUMN_DESCRIPTION = "budget_desc";
        public static final String COLUMN_AMOUNT = "budget_amount";
    }
}
