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
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.login.LoginPagerAdapter;

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
        tabLayout = root.findViewById(R.id.home_sections_tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = root.findViewById(R.id.home_sections_viewPager);
        final LoginPagerAdapter adapter = new LoginPagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // TODO: 22.04.2018 show spinner

        getSettingsData();
        return root;
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setDatesRange(String from, String to) {
        tvDatesRange.setText(String.format("%s - %s", from, to));
    }

    @Override
    public void setSections(List<SpendingSection> sections) {
        tabLayout.removeAllTabs();
        for (SpendingSection section : sections) {
            tabLayout.addTab(tabLayout.newTab().setText(section.getName()));
        }
    }

    private void getSettingsData() {
        presenter.updateHomeScreen();
    }

    @Override
    public void onResume() {
        System.out.println("HomeFragment: onResume()");
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        System.out.println("HomeFragment: onDestroy()");
        presenter.dropView();
        super.onDestroy();
    }
}
