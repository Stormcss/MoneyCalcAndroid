package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.Transaction;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private List<Transaction> transactionList;

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView logo, menu;
        TextView name, sum, date;

        HistoryViewHolder(View view) {
            super(view);
            cv = view.findViewById(R.id.transaction_cardview);
            logo = view.findViewById(R.id.transaction_spending_section_logo);
            menu = view.findViewById(R.id.transaction_menu);
            name = view.findViewById(R.id.transaction_name);
            sum = view.findViewById(R.id.transaction_sum);
            date = view.findViewById(R.id.transaction_date);
        }
    }

    public HistoryAdapter(Context mContext, List<Transaction> transactionList) {
        this.mContext = mContext;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_card, parent, false);

        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.name.setText(transaction.getDescription());
        holder.date.setText(transaction.getDate());
        holder.sum.setText(String.valueOf(transaction.getSum()));

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menu);
            }
        });
    }

    public void updateList(List<Transaction> transactions) {
        this.transactionList.clear();
        this.transactionList.addAll(transactions);
        notifyDataSetChanged();
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_spending_section, popup.getMenu());
        popup.setOnMenuItemClickListener(new SpendingSectionMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class SpendingSectionMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public SpendingSectionMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.spendingsection_menu_update:
                    Toast.makeText(mContext, "Update", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.spendingsection_menu_delete:
                    Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

}
