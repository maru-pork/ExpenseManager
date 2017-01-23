package com.nnayram.expensemanager.core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.activity.BudgetDetailActivity;

import java.util.ArrayList;

/**
 * Created by Rufo on 1/23/2017.
 */
public class BudgetAdapter extends ArrayAdapter<BudgetModel> {
    private Context m;
    private ArrayList<BudgetModel> budgetModels;

    public BudgetAdapter(Context context, int resource, ArrayList<BudgetModel> budgetModels) {
        super(context, resource, budgetModels);
        this.m = context;
        this.budgetModels = new ArrayList<>(budgetModels);
    }

    private class ViewHolder {
        TextView accountName;
        TextView balance;
        Button btnView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) m.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.account_info, null);

            holder = new ViewHolder();
            holder.accountName = (TextView) convertView.findViewById(R.id.tv_account_name);
            holder.balance = (TextView) convertView.findViewById(R.id.tv_account_bal);
            holder.btnView = (Button) convertView.findViewById(R.id.btn_account_view);
            convertView.setTag(holder);

            final Long id = budgetModels.get(position).getId();
            holder.btnView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(m, BudgetDetailActivity.class);
                    intent.putExtra("BUDGET_ID", id);
                    m.startActivity(intent);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BudgetModel budgetModel = budgetModels.get(position);
        holder.accountName.setText(budgetModel.getDescription());
        holder.balance.setText(budgetModel.getBalance());

        return convertView;
    }

}
