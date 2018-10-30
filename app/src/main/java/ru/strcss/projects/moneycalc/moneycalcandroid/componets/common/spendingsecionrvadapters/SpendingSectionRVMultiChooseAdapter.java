package ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Set;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;

import static android.graphics.PorterDuff.Mode;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getSpendingSectionInnerIdByPosition;

public class SpendingSectionRVMultiChooseAdapter extends BaseSpendingSectionRVAdapter<BaseSpendingSectionRVAdapter.ViewHolder> {
    private Set<Integer> selectedSpendingSections;

    public SpendingSectionRVMultiChooseAdapter(Context context, List<SpendingSection> spendingSections, Set<Integer> selectedSpendingSections) {
        super(context);
        this.spendingSectionsList = spendingSections;
        this.inflater = LayoutInflater.from(context);
        this.selectedSpendingSections = selectedSpendingSections;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.addedittransaction_spendingsection_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseSpendingSectionRVAdapter.ViewHolder holder, final int position) {
        final SpendingSection spendingSection = spendingSectionsList.get(position);
        holder.sectionName.setText(spendingSection.getName());
        if (spendingSection.getLogoId() != null) {
            holder.sectionLogo.setImageResource(logoStorage.get(spendingSection.getLogoId()));
        }

        holder.sectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("adapter item! = " + position);
                Integer sectionId = getSpendingSectionInnerIdByPosition(spendingSectionsList, position);
                boolean isPositionNew = selectedSpendingSections.add(sectionId);
                if (!isPositionNew)
                    selectedSpendingSections.remove(sectionId);
                holder.onClick(v);
                notifyDataSetChanged();
            }
        });

        if (isPositionSelectedInSet(position, spendingSectionsList, selectedSpendingSections)) {
            holder.sectionLayout.setBackgroundColor(colorPrimaryBright);
            holder.sectionName.setTextColor(Color.WHITE);
            holder.sectionLogo.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
        } else {
            holder.sectionLayout.setBackgroundColor(colorBackground);
            holder.sectionName.setTextColor(Color.BLACK);
            holder.sectionLogo.setColorFilter(null);
        }
    }

    private boolean isPositionSelectedInSet(int position, List<SpendingSection> sectionList,
                                            Set<Integer> selectedSectionsIds) {
        Integer sectionId = getSpendingSectionInnerIdByPosition(sectionList, position);
        for (Integer selectedSectionId : selectedSectionsIds) {
            if (selectedSectionId.equals(sectionId))
                return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return spendingSectionsList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}

