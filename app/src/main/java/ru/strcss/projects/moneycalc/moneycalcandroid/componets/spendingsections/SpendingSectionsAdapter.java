package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

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
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection.AddEditSpendingSectionActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage;

public class SpendingSectionsAdapter extends RecyclerView.Adapter<SpendingSectionsAdapter.SpendingSectionViewHolder> {

    private Context mContext;
    private List<SpendingSection> sectionList;
    private SpendingSectionsContract.Presenter presenter;

    private SparseIntArray logoStorage = DrawableStorage.getSpendingSectionLogoStorage();

    public static class SpendingSectionViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView logo, menu;
        TextView name, finance;

        SpendingSectionViewHolder(View view) {
            super(view);
            cv = view.findViewById(R.id.spending_section_cardview);
            logo = view.findViewById(R.id.spending_section_logo);
            menu = view.findViewById(R.id.spending_section_menu);
            name = view.findViewById(R.id.spending_section_name);
            finance = view.findViewById(R.id.spending_section_finance);
        }
    }

    public SpendingSectionsAdapter(Context mContext, List<SpendingSection> sectionList, SpendingSectionsContract.Presenter presenter) {
        this.mContext = mContext;
        this.sectionList = sectionList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public SpendingSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spendingsection_card, parent, false);

        return new SpendingSectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SpendingSectionViewHolder holder, int position) {
        SpendingSection spendingSection = sectionList.get(position);
        holder.name.setText(spendingSection.getName());
        holder.finance.setText(String.valueOf(spendingSection.getBudget()));

        if (spendingSection.getLogoId() != null) {
            holder.logo.setImageResource(logoStorage.get(spendingSection.getLogoId(), 0));
        }

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menu, holder.getAdapterPosition());
            }
        });
    }

    public void updateList(List<SpendingSection> sectionList) {
        this.sectionList.clear();
        this.sectionList.addAll(sectionList);
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
    class SpendingSectionMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int adapterPosition;

        public SpendingSectionMenuItemClickListener(int adapterPosition) {
            this.adapterPosition = adapterPosition;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.spendingsection_menu_update:
                    Intent intent = new Intent(mContext, AddEditSpendingSectionActivity.class);
                    intent.putExtra(AppConstants.SPENDING_SECTION, sectionList.get(adapterPosition));

                    mContext.startActivity(intent);
                    return true;

                case R.id.spendingsection_menu_delete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                    alertDialogBuilder.setTitle(R.string.spending_section_delete)
                            .setMessage(R.string.are_you_sure)
                            .setIcon(R.drawable.ic_warning_red_24dp)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    presenter.deleteSpendingSection(sectionList.get(adapterPosition)
                                            .getSectionId());
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
        return sectionList.size();
    }

}
