package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;

import static android.graphics.PorterDuff.Mode;

public class SpendingSectionRecyclerViewAdapter extends RecyclerView.Adapter<SpendingSectionRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<SpendingSection> spendingSections;
    private ItemClickListener mClickListener;

    private AtomicInteger selectedPosition;

    private int colorPrimaryBright;
    private int colorBackground;

    public SpendingSectionRecyclerViewAdapter(Context context, List<SpendingSection> spendingSections, AtomicInteger selectedPosition) {
        this.spendingSections = spendingSections;
        this.inflater = LayoutInflater.from(context);
        this.selectedPosition = selectedPosition;

        colorPrimaryBright = ResourcesCompat.getColor(context.getResources(), R.color.colorPrimaryBright, null);
        colorBackground = ResourcesCompat.getColor(context.getResources(), R.color.colorBackground, null);
    }

    public int getPositionBySpendingSectionInnerId(int sectionInnerId) {
        if (spendingSections != null) {
            for (int i = 0; i < spendingSections.size(); i++) {
                if (spendingSections.get(i).getSectionId() == sectionInnerId)
                    return i;
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.addedittransaction_spendingsection_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        SpendingSection spendingSection = spendingSections.get(position);
        holder.sectionName.setText(spendingSection.getName());
        // TODO: 17.09.2018 set image here

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
        return spendingSections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView sectionName;
        private RelativeLayout sectionLayout;
        private ImageView sectionLogo;

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

    SpendingSection getItem(int id) {
        return spendingSections.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

