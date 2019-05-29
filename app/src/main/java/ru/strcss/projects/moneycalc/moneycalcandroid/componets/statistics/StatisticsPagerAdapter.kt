package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.bysectionsum.StatsSumBySectionFragment

class StatisticsPagerAdapter(fm: FragmentManager, private val mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return StatsSumBySectionFragment()
            }
            1 -> {
                return StatsSumBySectionFragment()
            }
            2 -> {
                return StatsSumBySectionFragment()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

}
