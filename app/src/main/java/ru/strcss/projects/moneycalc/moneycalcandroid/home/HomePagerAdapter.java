package ru.strcss.projects.moneycalc.moneycalcandroid.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private final List<HomeStatsFragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public HomePagerAdapter(FragmentManager manager) {
        super(manager);
    }

    public void addFrag(HomeStatsFragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void clearFragments() {
        mFragmentList.clear();
        mFragmentTitleList.clear();
    }

//    public void refreshFragments(FragmentActivity fa) {
//        for (HomeStatsFragment fragment : mFragmentList) {
//            fragment.redraw();
////            fa.getSupportFragmentManager().beginTransaction()
////                    .detach(fragment)
////                    .attach(fragment)
////                    .commitAllowingStateLoss();
//        }
//    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
//        return HomeStatsFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    @Override
    public float getPageWidth(int position) {
        return 0.5f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
