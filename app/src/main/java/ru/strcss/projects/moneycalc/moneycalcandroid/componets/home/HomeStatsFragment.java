package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.FinanceSummaryBySection;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.FINANCE_SUMMARY_BY_SECTION;

public class HomeStatsFragment extends Fragment implements HomeStatsContract.View {

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
        args.putSerializable(FINANCE_SUMMARY_BY_SECTION, financeSummary);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_stats_frag, container, false);

        FinanceSummaryBySection financeSummary = (FinanceSummaryBySection) getArguments().getSerializable(FINANCE_SUMMARY_BY_SECTION);

        tvDayBalance = root.findViewById(R.id.home_stats_day_balance);
        tvSummaryBalance = root.findViewById(R.id.home_stats_summaryBalance);
        tvSpendAll = root.findViewById(R.id.home_stats_spend);
        tvLeftAll = root.findViewById(R.id.home_stats_left);

        if (financeSummary == null)
            return root;

        setBalanceValue(tvDayBalance, financeSummary.getTodayBalance(), true);
        setBalanceValue(tvSummaryBalance, financeSummary.getSummaryBalance(), true);
        setBalanceValue(tvSpendAll, financeSummary.getMoneySpendAll(), false);
        setBalanceValue(tvLeftAll, financeSummary.getMoneyLeftAll(), true);
        return root;
    }

    private void setBalanceValue(TextView textView, Double value, boolean setColor){
        textView.setText(String.valueOf(value));
        if (setColor)
            setBalanceColor(textView, value);
    }

    private void setBalanceColor(TextView textView, Double value){
        if (value > 0)
            textView.setTextColor(Color.rgb(0,150,0));
        else if (value < 0)
            textView.setTextColor(Color.rgb(150,0,0));
        else
            textView.setTextColor(Color.BLACK);
    }


    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }
}
