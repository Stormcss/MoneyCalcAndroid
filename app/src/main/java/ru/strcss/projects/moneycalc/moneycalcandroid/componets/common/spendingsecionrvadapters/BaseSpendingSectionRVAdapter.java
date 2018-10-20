package ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage;

public abstract class BaseSpendingSectionRVAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    ItemClickListener mClickListener;
    protected LayoutInflater inflater;
    protected List<SpendingSection> spendingSectionsList;
    SparseIntArray logoStorage = DrawableStorage.getSpendingSectionLogoStorage();

    protected int colorPrimaryBright;
    protected int colorBackground;

    BaseSpendingSectionRVAdapter(Context context) {
        colorPrimaryBright = ResourcesCompat.getColor(context.getResources(), R.color.colorPrimaryBright, null);
        colorBackground = ResourcesCompat.getColor(context.getResources(), R.color.colorBackground, null);
    }

//    public int getPositionBySpendingSectionInnerId(List<SpendingSection> spendingSectionsList, int sectionInnerId) {
//        if (spendingSectionsList != null) {
//            for (int i = 0; i < spendingSectionsList.size(); i++) {
//                if (spendingSectionsList.get(i).getSectionId() == sectionInnerId)
//                    return i;
//            }
//        }
//        return 0;
//    }
//
//    public Integer getSpendingSectionInnerIdByPosition(List<SpendingSection> spendingSectionsList, int position) {
//        if (spendingSectionsList != null) {
//            if (spendingSectionsList.size() > position)
//                return spendingSectionsList.get(position).getSectionId();
//        }
//        return null;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView sectionName;
        protected RelativeLayout sectionLayout;
        protected ImageView sectionLogo;

        ViewHolder(View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.ae_transaction_section_name);
            sectionLogo = itemView.findViewById(R.id.ae_transaction_section_logo);
            sectionLayout = itemView.findViewById(R.id.ae_transaction_section_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
