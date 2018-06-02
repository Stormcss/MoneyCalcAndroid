package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import java.util.List;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.Transaction;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private List<Transaction> transactionList;
    private HistoryContract.Presenter historyPresenter;

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

    public HistoryAdapter(Context mContext, HistoryContract.Presenter historyPresenter, List<Transaction> transactionList) {
        this.mContext = mContext;
        this.historyPresenter = historyPresenter;
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
                showPopupMenu(holder.menu, holder.getAdapterPosition());
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
    private void showPopupMenu(View view, int adapterPosition) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_spending_section, popup.getMenu());
        popup.setOnMenuItemClickListener(new SpendingSectionMenuItemClickListener(adapterPosition));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    public class SpendingSectionMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int adapterPosition;

        public SpendingSectionMenuItemClickListener(int adapterPosition) {
            this.adapterPosition = adapterPosition;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.spendingsection_menu_update:
                    Intent intent = new Intent(mContext, AddEditTransactionActivity.class);
                    intent.putExtra("transaction", transactionList.get(adapterPosition));

                    mContext.startActivity(intent);

                    return true;
                case R.id.spendingsection_menu_delete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                    alertDialogBuilder.setTitle(R.string.transaction_delete_title)
                            .setMessage(R.string.are_you_sure)
                            .setIcon(R.drawable.ic_warning_red_24dp)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    historyPresenter.deleteTransaction(transactionList.get(adapterPosition).get_id());
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();

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
