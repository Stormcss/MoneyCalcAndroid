package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTION;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.setViewVisibility;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getLogoIdBySectionId;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> /*implements HistoryAdapter.ItemClickListener*/ {

    private Context mContext;
    private List<TransactionLegacy> transactionList;
    private HistoryContract.Presenter historyPresenter;

    private View checkedRvItem;

    private final SparseIntArray logoStorage = DrawableStorage.getSpendingSectionLogoStorage();
    private final DataStorage dataStorage;

    public HistoryAdapter(Context mContext, HistoryContract.Presenter historyPresenter, List<TransactionLegacy> transactionList, DataStorage dataStorage) {
        this.mContext = mContext;
        this.historyPresenter = historyPresenter;
        this.transactionList = transactionList;
        this.dataStorage = dataStorage;
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
        TransactionLegacy transaction = transactionList.get(position);
        holder.title.setText(transaction.getTitle());
        holder.description.setText(transaction.getDescription());
        holder.date.setText(DatesUtils.formatDateToPretty(transaction.getDate()));
        holder.sum.setText(String.valueOf(transaction.getSum()));

        Integer sectionLogoId = getLogoIdBySectionId(dataStorage.getSpendingSections(), transaction.getSectionId());
        if (sectionLogoId != null) {
            holder.logo.setImageResource(logoStorage.get(sectionLogoId));
        }

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menu, holder.getAdapterPosition());
            }
        });

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedRvItem != null) {
                    if (checkedRvItem == holder.description) {
                        setViewVisibility(checkedRvItem, false);
                        checkedRvItem = null;
                    } else {
                        setViewVisibility(holder.description, true);
                        setViewVisibility(checkedRvItem, false);
                        checkedRvItem = holder.description;
                    }
                } else {
                    setViewVisibility(holder.description, true);
                    checkedRvItem = holder.description;
                }
            }
        });
        setViewVisibility(holder.description, false);
    }

    public void updateList(List<TransactionLegacy> transactions) {
        this.transactionList.clear();
        this.transactionList.addAll(transactions);
        notifyDataSetChanged();
    }

    /**
     * Showing popup menu when tapping on settings icon
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
                    intent.putExtra(TRANSACTION, transactionList.get(adapterPosition));

                    mContext.startActivity(intent);

                    return true;
                case R.id.spendingsection_menu_delete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                    alertDialogBuilder.setTitle(R.string.transaction_delete_title)
                            .setMessage(getMergedDescription(mContext, transactionList.get(adapterPosition)))
                            .setIcon(R.drawable.ic_warning_red_24dp)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    historyPresenter.deleteTransaction(transactionList.get(adapterPosition).getId());
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

    private String getMergedDescription(Context context, TransactionLegacy transaction) {
        StringBuilder stringBuilder = new StringBuilder(context.getText(R.string.transaction_delete_part1));
        stringBuilder.append(" ")
                .append(transaction.getSum())
                .append(" ")
                .append(context.getText(R.string.transaction_delete_part3));
        if (transaction.getDescription() != null && !transaction.getDescription().isEmpty()) {
            stringBuilder.append(" ")
                    .append(context.getText(R.string.transaction_delete_part4))
                    .append(" \"")
                    .append(transaction.getDescription())
                    .append("\"");
        }
        return stringBuilder.append("?").toString();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView logo, menu;

        TextView sum, date, title, description;

        HistoryViewHolder(View view) {
            super(view);
            cv = view.findViewById(R.id.transaction_cardview);
            logo = view.findViewById(R.id.transaction_spending_section_logo);
            menu = view.findViewById(R.id.transaction_menu);
            sum = view.findViewById(R.id.transaction_sum);
            date = view.findViewById(R.id.transaction_date);
            title = view.findViewById(R.id.transaction_title);
            description = view.findViewById(R.id.transaction_desc);
        }
    }
}
