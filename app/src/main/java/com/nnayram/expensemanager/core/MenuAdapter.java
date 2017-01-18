package com.nnayram.expensemanager.core;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nnayram.expensemanager.R;
import com.nnayram.expensemanager.activity.AccountActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rufo on 1/14/2017.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.CardViewHolder> {

    private List<MenuModel> menuModels = new ArrayList<>();
    private Context context;

    public MenuAdapter(List<MenuModel> menuModels) {
        this.menuModels = menuModels;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView description;
        ImageView iconId;

        public CardViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            this.cv = (CardView) itemView.findViewById(R.id.cv);
            this.title = (TextView) itemView.findViewById(R.id.card_title);
            this.description = (TextView) itemView.findViewById(R.id.card_description);
            this.iconId = (ImageView) itemView.findViewById(R.id.card_icon);

            cv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green));
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public MenuAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_menu, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.CardViewHolder cardViewHolder, int position) {
        cardViewHolder.description.setText(menuModels.get(position).getDescription());
        cardViewHolder.title.setText(menuModels.get(position).getTitle());
        cardViewHolder.iconId.setImageResource(menuModels.get(position).getIconId());

        switch (position) {
            case 0:

                break;
            case 1:
                cardViewHolder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AccountActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }
}
