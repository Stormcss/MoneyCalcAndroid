package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

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
import ru.strcss.projects.moneycalc.enitities.SpendingSection;

public class SpendingSectionsAdapter extends RecyclerView.Adapter<SpendingSectionsAdapter.SpendingSectionViewHolder> {

    private Context mContext;
    private List<SpendingSection> sectionList;

    public static class SpendingSectionViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView logo, menu;
        TextView name, finance;

        SpendingSectionViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.spending_section_cardview);
            logo = (ImageView) view.findViewById(R.id.spending_section_logo);
            menu = (ImageView) view.findViewById(R.id.spending_section_menu);
            name = (TextView) view.findViewById(R.id.spending_section_name);
            finance = (TextView) view.findViewById(R.id.spending_section_finance);
        }
    }

    public SpendingSectionsAdapter(Context mContext, List<SpendingSection> sectionList) {
        this.mContext = mContext;
        this.sectionList = sectionList;
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
        holder.finance.setText("" + spendingSection.getBudget());

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menu);
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
        return sectionList.size();
    }

}
