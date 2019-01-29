package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.sectiontabs;

import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage;

/**
 * Created by Stormcss
 * Date: 28.01.2019
 */
public class TabAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    @Getter
    private List<TabHolder> data = new ArrayList<>();
    private final ListView listView;
    private OnItemClickListener listener;
    private SparseIntArray logoStorage = DrawableStorage.getSpendingSectionLogoStorage();

    private int currentSelected = 0;

    public TabAdapter(ListView listView, OnItemClickListener listener) {
        this.listView = listView;
        this.listener = listener;

        listView.setOnItemClickListener(this);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TabHolder getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Currently not using viewHolder pattern cause there aren't too many tabs in the demo project.

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_section_tab_item, viewGroup, false);
        }

        TextView tabTitle = view.findViewById(R.id.home_tab_title);
        ImageView tabLogo = view.findViewById(R.id.home_tab_spending_section_logo);
        tabTitle.setText(getItem(i).getSectionName());

        if (getItem(i).getLogoId() != null)
            tabLogo.setImageResource(logoStorage.get(getItem(i).getLogoId(), 0));

        if (i == currentSelected) {
            setTextViewToSelected(view, tabTitle);
        } else {
            setTextViewToUnSelected(view, tabTitle);
        }

        return view;
    }

    /**
     * Return item view at the given position or null if position is not visible.
     */
    public View getViewByPosition(int pos) {
        if (listView == null) {
            return null;
        }
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return null;
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void setTextViewToSelected(View tab, TextView tabTitle) {
        tab.setBackgroundResource(R.color.indigoA100);
        tabTitle.setTextColor(Color.WHITE);
    }

    private void setTextViewToUnSelected(View tab, TextView tabTitle) {
        tab.setBackgroundResource(R.color.indigo100);
        tabTitle.setTextColor(Color.GRAY);
    }

    private void select(int position) {
        if (currentSelected >= 0) {
            deselect(currentSelected);
        }

        View targetView = getViewByPosition(position);
        if (targetView != null) {
            setTextViewToSelected(targetView, (TextView) (targetView.findViewById(R.id.home_tab_title)));
        }

        if (listener != null) {
            listener.selectItem(position);
        }

        currentSelected = position;

    }

    private void deselect(int position) {
        if (getViewByPosition(position) != null) {
            View targetView = getViewByPosition(position);
            if (targetView != null) {
                setTextViewToUnSelected(targetView, (TextView) (targetView.findViewById(R.id.home_tab_title)));
            }
        }

        currentSelected = -1;
    }

    // OnClick Events

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        select(i);
    }

    public void OnItemClickListener(TabAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setCurrentSelected(int i) {
        select(i);
    }

    public interface OnItemClickListener {
        void selectItem(int position);
    }

}
