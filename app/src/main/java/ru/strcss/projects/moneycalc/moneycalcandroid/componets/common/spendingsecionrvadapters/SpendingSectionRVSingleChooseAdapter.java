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
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;

import static android.graphics.PorterDuff.Mode;

public class SpendingSectionRVSingleChooseAdapter extends BaseSpendingSectionRVAdapter<BaseSpendingSectionRVAdapter.ViewHolder> {
    private AtomicInteger selectedPosition;

    public SpendingSectionRVSingleChooseAdapter(Context context, List<SpendingSection> spendingSections, AtomicInteger selectedPosition) {
        super(context);
        this.setSpendingSectionsList(spendingSections);
        this.setInflater(LayoutInflater.from(context));
        this.selectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.addedittransaction_spendingsection_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseSpendingSectionRVAdapter.ViewHolder holder, final int position) {
        SpendingSection spendingSection = getSpendingSectionsList().get(position);
        holder.getSectionName().setText(spendingSection.getName());
        if (spendingSection.getLogoId() != null) {
            holder.getSectionLogo().setImageResource(DrawableStorage.getSpendingSectionLogoStorage().get(spendingSection.getLogoId()));
        }

        holder.getSectionLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition.set(position);
                holder.onClick(v);
                notifyDataSetChanged();
            }
        });

        if (selectedPosition.get() == position) {
            holder.getSectionLayout().setBackgroundColor(getColorPrimaryBright());
            holder.getSectionName().setTextColor(Color.WHITE);
            holder.getSectionLogo().setColorFilter(Color.WHITE, Mode.SRC_ATOP);
        } else {
            holder.getSectionLayout().setBackgroundColor(getColorBackground());
            holder.getSectionName().setTextColor(Color.BLACK);
            holder.getSectionLogo().setColorFilter(null);
        }

    }

    @Override
    public int getItemCount() {
        return getSpendingSectionsList().size();
    }

    public SpendingSection getItem(int id) {
        return getSpendingSectionsList().get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.setMClickListener(itemClickListener);
    }
}

