package com.nnayram.expensemanager.core;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nnayram.expensemanager.R;

import java.util.ArrayList;

/**
 * Created by Rufo on 1/17/2017.
 */
public class AccountAdapter extends ArrayAdapter<AccountModel>{

    private Context m;
    private ArrayList<AccountModel> accountModels;

    public AccountAdapter(Context context, int resource, ArrayList<AccountModel> accountModels) {
        super(context, resource, accountModels);
        this.m = context;
        this.accountModels = new ArrayList<>(accountModels);
    }

    private class ViewHolder {
        TextView accountName;
        TextView balance;
        Button btnGo;
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
            holder.btnGo = (Button) convertView.findViewById(R.id.btn_account_view);
            convertView.setTag(holder);

            final String accountName = holder.accountName.getText().toString();
            holder.btnGo.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(m.getApplicationContext(), accountName, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AccountModel accountModel = accountModels.get(position);
        holder.accountName.setText(accountModel.getAccountName());
        holder.balance.setText(accountModel.getBalance());

        return convertView;
    }
}
