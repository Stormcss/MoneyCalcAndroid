package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class LoginPagerAdapter(fm: FragmentManager, private val mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return LoginFragment()
            }
            1 -> {
                return RegisterFragment()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

}
