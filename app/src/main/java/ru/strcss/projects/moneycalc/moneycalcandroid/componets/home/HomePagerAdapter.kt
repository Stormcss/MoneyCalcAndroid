package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

class HomePagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val mFragmentList = ArrayList<HomeStatsFragment>()
    private val mFragmentTitleList = ArrayList<String>()

    fun addFrag(fragment: HomeStatsFragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun clearFragments() {
        mFragmentList.clear()
        mFragmentTitleList.clear()
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }


    override fun getPageWidth(position: Int): Float {
        return 1f
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}
