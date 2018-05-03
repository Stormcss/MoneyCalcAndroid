package ru.strcss.projects.moneycalc.moneycalcandroid.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;

public class HomeStatsFragment extends Fragment implements HomeStatsContract.View {

    private static final String ARG_SECTION_ID = "section_id";
    private static final String ARG_TODAY_BALANCE = "today_balance";
    private static final String ARG_SUMMARY_BALANCE = "summary_balance";
    private static final String ARG_SPEND_ALL = "spend_all";
    private static final String ARG_LEFT_ALL = "left_all";
    private int sectionId;

    // UI references
    private TextView tvDayBalance;
    private TextView tvSummaryBalance;
    private TextView tvSpendAll;
    private TextView tvLeftAll;

    public HomeStatsFragment() {
    }

    public static HomeStatsFragment newInstance(FinanceSummaryBySection financeSummary) {
        HomeStatsFragment fragment = new HomeStatsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_ID, financeSummary.getSectionID());
        args.putDouble(ARG_TODAY_BALANCE, financeSummary.getTodayBalance());
        args.putDouble(ARG_SUMMARY_BALANCE, financeSummary.getSummaryBalance());
        args.putInt(ARG_SPEND_ALL, financeSummary.getMoneySpendAll());
        args.putInt(ARG_LEFT_ALL, financeSummary.getMoneyLeftAll());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_stats_frag, container, false);

        sectionId = getArguments().getInt(ARG_SECTION_ID);

        tvDayBalance = root.findViewById(R.id.home_stats_day_balance);
        tvSummaryBalance = root.findViewById(R.id.home_stats_summaryBalance);
        tvSpendAll = root.findViewById(R.id.home_stats_spend);
        tvLeftAll = root.findViewById(R.id.home_stats_left);

        tvDayBalance.setText(String.valueOf(getArguments().getDouble(ARG_TODAY_BALANCE)));
        tvSummaryBalance.setText(String.valueOf(getArguments().getDouble(ARG_SUMMARY_BALANCE)));
        tvSpendAll.setText(String.valueOf(getArguments().getInt(ARG_SPEND_ALL)));
        tvLeftAll.setText(String.valueOf(getArguments().getInt(ARG_LEFT_ALL)));
        return root;
    }


    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }
}
