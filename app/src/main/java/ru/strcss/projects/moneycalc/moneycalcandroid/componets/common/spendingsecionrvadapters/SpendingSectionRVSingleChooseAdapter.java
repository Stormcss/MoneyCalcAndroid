package ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;

import static android.graphics.PorterDuff.Mode;

public class SpendingSectionRVSingleChooseAdapter extends BaseSpendingSectionRVAdapter<BaseSpendingSectionRVAdapter.ViewHolder> {
    private AtomicInteger selectedPosition;

    public SpendingSectionRVSingleChooseAdapter(Context context, List<SpendingSection> spendingSections, AtomicInteger selectedPosition) {
        super(context);
        this.spendingSectionsList = spendingSections;
        this.inflater = LayoutInflater.from(context);
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.addedittransaction_spendingsection_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseSpendingSectionRVAdapter.ViewHolder holder, final int position) {
        SpendingSection spendingSection = spendingSectionsList.get(position);
        holder.sectionName.setText(spendingSection.getName());
        if (spendingSection.getLogoId() != null) {
            holder.sectionLogo.setImageResource(logoStorage.get(spendingSection.getLogoId()));
        }

        holder.sectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition.set(position);
                holder.onClick(v);
                notifyDataSetChanged();
            }
        });

        if (selectedPosition.get() == position) {
            holder.sectionLayout.setBackgroundColor(colorPrimaryBright);
            holder.sectionName.setTextColor(Color.WHITE);
            holder.sectionLogo.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
        } else {
            holder.sectionLayout.setBackgroundColor(colorBackground);
            holder.sectionName.setTextColor(Color.BLACK);
            holder.sectionLogo.setColorFilter(null);
        }

    }

    @Override
    public int getItemCount() {
        return spendingSectionsList.size();
    }

    public SpendingSection getItem(int id) {
        return spendingSectionsList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}

