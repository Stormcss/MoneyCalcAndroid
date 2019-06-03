package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate.StatsSumByDateFragment
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydatesection.StatsSumByDateSectionFragment
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbysection.StatsSumBySectionFragment

class StatisticsPagerAdapter(fm: FragmentManager, private val mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> {
                StatsSumBySectionFragment()
            }
            1 -> {
                StatsSumByDateFragment()
            }
            2 -> {
                StatsSumByDateSectionFragment()
            }
            else -> null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

}
