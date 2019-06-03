package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import dagger.android.support.DaggerAppCompatActivity
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_token
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.OnKeyboardVisibilityListener
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.setKeyboardVisibilityListener
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : DaggerAppCompatActivity(), OnKeyboardVisibilityListener {
    @Inject
    lateinit var loginPresenter: LoginPresenter
    @Inject
    lateinit var loginFragment: LoginFragment
    @Inject
    lateinit var registerPresenter: RegisterPresenter
    @Inject
    lateinit var registerFragment: RegisterFragment

    private var signInLogo: ImageView? = null
    private var signInSettingsLogo: ImageView? = null
    internal var signInLogoTargetSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getString(appl_storage_token.name, null) != null) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        }

        supportActionBar!!.hide()
        setKeyboardVisibilityListener(this, findViewById<View>(android.R.id.content) as ViewGroup)

        signInLogo = findViewById(R.id.signIn_logo)
        signInSettingsLogo = findViewById(R.id.signIn_settings)

        signInLogoTargetSize = signInLogo!!.maxHeight

        signInSettingsLogo!!.setOnClickListener { startActivity(Intent(applicationContext, ApplicationSettingsActivity::class.java)) }

        //fragments setup
        val tabLayout = findViewById<TabLayout>(R.id.login_tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.title_activity_login)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.title_activity_register)))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = findViewById<ViewPager>(R.id.login_viewPager)
        val adapter = LoginPagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    override fun onVisibilityChanged(visible: Boolean) {
        if (visible) {
            signInLogoTargetSize = signInLogo!!.height
            UiUtils.collapseView(signInLogo!!, 100, 0)
        } else
            UiUtils.expandView(signInLogo!!, 100, signInLogoTargetSize)
    }
}

