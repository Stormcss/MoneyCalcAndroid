package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addtransaction;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;

public class SpendingSectionArrayAdapter extends ArrayAdapter<SpendingSection> {
    private Context context;
    private List<SpendingSection> spendingSections;
    private int layoutResourceId;

    public SpendingSectionArrayAdapter(Context context, int layoutResourceId, List<SpendingSection> spendingSections) {
        super(context, layoutResourceId, spendingSections);
        this.spendingSections = spendingSections;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SpendingSectionHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new SpendingSectionHolder();
            holder.tvName = row.findViewById(R.id.ae_transaction_section_name);

            row.setTag(holder);
        } else {
            holder = (SpendingSectionHolder) row.getTag();
        }
        SpendingSection spendingSection = spendingSections.get(position);

        holder.tvName.setText(spendingSection.getName());
        return row;
    }

    static class SpendingSectionHolder {
        TextView tvName;
    }
}

