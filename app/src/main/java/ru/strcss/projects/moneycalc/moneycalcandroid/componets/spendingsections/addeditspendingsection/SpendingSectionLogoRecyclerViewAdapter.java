package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.concurrent.atomic.AtomicInteger;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage;

import static android.graphics.PorterDuff.Mode;

public class SpendingSectionLogoRecyclerViewAdapter extends RecyclerView.Adapter<SpendingSectionLogoRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private SparseIntArray logoStorage = DrawableStorage.getSpendingSectionLogoStorage();
    private ItemClickListener mClickListener;

    private AtomicInteger selectedPosition;

    private int colorPrimaryBright;
    private int colorBackground;

    public SpendingSectionLogoRecyclerViewAdapter(Context context, AtomicInteger selectedPosition) {
        this.inflater = LayoutInflater.from(context);
        this.selectedPosition = selectedPosition;

        colorPrimaryBright = ResourcesCompat.getColor(context.getResources(), R.color.colorPrimaryBright, null);
        colorBackground = ResourcesCompat.getColor(context.getResources(), R.color.colorBackground, null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.addeditspendingsection_spendingsection_logo_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//        int logoId = ;
//        System.out.println("position = " + position);
//        System.out.println("logoId = " + logoId);
//        Drawable drawable = holder.itemView.getContext().getResources().getDrawable(logoId);
//        System.out.println("drawable = " + drawable);
//        holder.sectionLogo.setImageDrawable(drawable);
        holder.sectionLogo.setImageResource(logoStorage.get(position, 0));

        holder.sectionLayoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition.set(position);
                holder.onClick(v);
                notifyDataSetChanged();
            }
        });

        if (selectedPosition.get() == position) {
            holder.sectionLayoutLayout.setBackgroundColor(colorPrimaryBright);
            holder.sectionLogo.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
        } else {
            holder.sectionLayoutLayout.setBackgroundColor(colorBackground);
            holder.sectionLogo.setColorFilter(null);
        }

    }

    @Override
    public int getItemCount() {
        return logoStorage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RelativeLayout sectionLayoutLayout;
        private ImageView sectionLogo;

        ViewHolder(View itemView) {
            super(itemView);
            sectionLogo = itemView.findViewById(R.id.ae_spendingsection_section_logo);
            sectionLayoutLayout = itemView.findViewById(R.id.ae_spendingsection_section_layout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

//    public int getPositionByLogoId(int logoId){
//        return logoStorage.get(logoId, 0);
//    }

    int getItem(int id) {
        return logoStorage.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

