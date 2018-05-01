package ru.strcss.projects.moneycalc.moneycalcandroid.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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

public class HomeFragment extends DaggerFragment implements HomeContract.View {

    @Inject
    HomeContract.Presenter presenter;

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
                Snackbar.make(view, "Transaction will be added.. maybe!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tvDatesRange = root.findViewById(R.id.home_dates_range);


        //preparing up section fragments
        viewPager = root.findViewById(R.id.home_sections_viewPager);
        setupViewPager(viewPager);
//        setupViewPager(viewPager, Arrays.asList(SpendingSection.builder().id(1).name("A").budget(100).build(),
//                SpendingSection.builder().id(2).name("B").budget(200).build()));

        tabLayout = root.findViewById(R.id.home_sections_tabLayout);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
//
        tabLayout.setupWithViewPager(viewPager);
//        final HomePagerAdapter adapter = new HomePagerAdapter
//                (getFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        // TODO: 22.04.2018 show spinner

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

//    @Override
//    public void setSections(List<SpendingSection> sections) {
//        System.out.println("setSections!");
//
//        System.out.println("adapter.getCount() = " + adapter.getCount());
//        System.out.println("sections.size() = " + sections.size());
//
//        fillAdapterWithFragments(sections);
//
////        for (int i = 0; i < sections.size(); i++) {
////            System.out.println("i = " + i);
////            HomeStatsFragment statsFragment = (HomeStatsFragment) adapter.getItem(i);
////            statsFragment.setTodayBalance(sections.get(i).getId());
////        }
//
//
////        setupViewPager(viewPager, sections);
//    }

//    public void requestStatisticsSectionUpdate(Integer id){
//        presenter.updateStatisticsSection(id);
//    }

    @Override
    public void setStatisticsSections(List<SpendingSection> spendingSections, List<FinanceSummaryBySection> financeSummary) {
        System.out.println("setStatisticsSection!");

//        FragmentTransaction fragTransaction = getChildFragmentManager().beginTransaction();
//        for (int i = 0; i < adapter.getCount(); i++) {
//            fragTransaction.remove(adapter.getItem(i));
//        }
//        fragTransaction.addToBackStack(null);
//        fragTransaction.commitAllowingStateLoss();

//        for (Fragment fragment : getChildFragmentManager().f) {
//            getActivity().getSupportFragmentManager().beginTransaction().remove(R.layout.home_stats_frag).commit();
//        }
        adapter.clearFragments();

        for (FinanceSummaryBySection finSumBySec : financeSummary) {
            HomeStatsFragment fView = HomeStatsFragment.newInstance(finSumBySec);
            adapter.addFrag(fView, findSpendingSectionById(spendingSections, finSumBySec.getSectionID()).getName());
        }
//        adapter.refreshFragments(getActivity());
        adapter.notifyDataSetChanged();

//        getChildFragmentManager().getFragments().clear();


    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        presenter.dropView();
        super.onDestroy();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new HomePagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(adapter);
    }

    private SpendingSection findSpendingSectionById(List<SpendingSection> spendingSections, int id) {
        for (SpendingSection spendingSection : spendingSections) {
            if (spendingSection.getId() == id)
                return spendingSection;
        }
        return null;
    }

//    private void setupViewPager(ViewPager viewPager, List<SpendingSection> spendingSections) {
//        adapter = new HomePagerAdapter(getActivity().getSupportFragmentManager());
//
//        for (SpendingSection spendingSection : spendingSections) {
//            HomeStatsFragment fView = HomeStatsFragment.newInstance(spendingSection.getId());
//            adapter.addFrag(fView, spendingSection.getName());
//        }
//
//        viewPager.setAdapter(adapter);
//    }
}
