package com.nnayram.expensemanager.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.core.MenuAdapter;
import com.nnayram.expensemanager.core.MenuModel;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // needed to init toolbar and text
        super.initializeToolbar();
        super.setCurrentContext(this);

        RecyclerView rvRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter rvAdapter = new MenuAdapter(getCardViews());

        rvRecyclerView.setHasFixedSize(true);
        rvRecyclerView.setLayoutManager(rvLayoutManager);
        rvRecyclerView.setAdapter(rvAdapter);

    }

    private List<MenuModel> getCardViews() {
        List<MenuModel> menuModels = new ArrayList<>();
        menuModels.add(new MenuModel("Credit Card Manager", "Credit Card Transactions", R.drawable.ic_closed_caption_black_24dp));
        menuModels.add(new MenuModel("Account", "Account Summaries", R.drawable.ic_closed_caption_black_24dp));
        menuModels.add(new MenuModel("Budget", "Budget Summaries", R.drawable.ic_closed_caption_black_24dp));
        return menuModels;
    }
}
