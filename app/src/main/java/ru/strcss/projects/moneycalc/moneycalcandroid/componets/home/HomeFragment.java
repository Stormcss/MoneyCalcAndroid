package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.FINANCE_SUMMARY_BY_SECTION;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getFinanceSummaryBySectionById;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getSpendingSectionById;

public class HomeFragment extends DaggerFragment implements HomeContract.View {

    @Inject
    HomeContract.Presenter presenter;

    @Inject
    DataStorage dataStorage;

    @Inject
    public HomeFragment() {
    }

    // UI references
    private TextView tvDatesRange;
    private FloatingActionButton fabAddTransaction;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private HomePagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_frag, container, false);

        fabAddTransaction = root.findViewById(R.id.fab_home_addTransaction);
        fabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addTransaction();
            }
        });

        tvDatesRange = root.findViewById(R.id.home_dates_range);

        //preparing up section fragments
        viewPager = root.findViewById(R.id.home_sections_viewPager);
        adapter = new HomePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.home_sections_tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        // TODO: 22.04.2018 show spinner
        if (dataStorage.getSettings() != null) {
            String periodFrom = dataStorage.getSettings().getPeriodFrom();
            String periodTo = dataStorage.getSettings().getPeriodTo();
            List<SpendingSection> sections = dataStorage.getSettings().getSections();
//            if (periodFrom != null && periodTo != null && sections != null);
            // FIXME: 08.06.2018 fuck, setting DatesRange also causes request for statistics calculation!
            //                setDatesRange(periodFrom, periodTo, getSpendingSectionIds(sections));
        }
        presenter.requestSettings();
        return root;
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setDatesRange(String from, String to, List<Integer> spendingSectionsIds) {
        tvDatesRange.setText(String.format("%s - %s", from, to));
        // FIXME: 29.04.2018 this should be done using RxJava
        presenter.requestSectionStatistics(from, to, spendingSectionsIds);
    }

    @Override
    public void setStatisticsSections(List<SpendingSection> spendingSections, List<FinanceSummaryBySection> financeSummary) {
        System.out.println("setStatisticsSection! financeSummary=" + financeSummary);

        adapter.clearFragments();

        for (FinanceSummaryBySection finSumBySec : financeSummary) {
            HomeStatsFragment fView = HomeStatsFragment.newInstance(finSumBySec);
            if (!fView.isAdded()) {
                adapter.addFrag(fView, getSpendingSectionById(spendingSections, finSumBySec.getSectionID()).getName());
            }
        }
        adapter.notifyDataSetChanged();

        FragmentManager fragmentManager = getFragmentManager();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.getClass().equals(HomeStatsFragment.class)) {
                FinanceSummaryBySection oldSBS = (FinanceSummaryBySection)
                        fragment.getArguments().getSerializable(FINANCE_SUMMARY_BY_SECTION);

                fragment.getArguments().putSerializable(FINANCE_SUMMARY_BY_SECTION,
                        getFinanceSummaryBySectionById(financeSummary, oldSBS.getSectionID()));
                fragmentManager.beginTransaction().detach(fragment).attach(fragment).commit();
            }
        }
    }

    @Override
    public void showAddTransaction() {
        Intent intent = new Intent(getContext(), AddEditTransactionActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
        presenter.requestSettings();
    }

    @Override
    public void onDestroy() {
        presenter.dropView();
        super.onDestroy();
    }
}
