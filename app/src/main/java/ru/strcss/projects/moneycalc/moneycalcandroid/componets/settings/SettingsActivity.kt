package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings

import android.app.Fragment
import android.os.Bundle
import android.preference.PreferenceActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SettingsActivity : PreferenceActivity(), HasFragmentInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>
    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()
    }

    override fun fragmentInjector(): AndroidInjector<Fragment>? {
        return frameworkFragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment>? {
        return supportFragmentInjector
    }
}
