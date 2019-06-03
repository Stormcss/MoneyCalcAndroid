package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.HistoryActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.StatisticsActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.changeActivityOnCondition
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.showAboutPopup
import javax.inject.Inject


class HomeActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var homePresenter: HomePresenter

    @Inject
    lateinit var homeFragmentProvider: Lazy<HomeFragment>

    @Inject
    lateinit var moneyCalcServerDAO: MoneyCalcServerDAO

    @Inject
    lateinit var dataStorage: DataStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        changeActivityOnCondition(moneyCalcServerDAO.token == null, this@HomeActivity, LoginActivity::class.java)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        var homeFragment: HomeFragment? = supportFragmentManager.findFragmentById(R.id.home_contentFrame) as? HomeFragment
        if (homeFragment == null) {
            // Get the fragment from dagger
            homeFragment = homeFragmentProvider.get()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, homeFragment!!, R.id.home_contentFrame)
        }

        val headerView = navigationView.getHeaderView(0)

        val navHeaderUser = headerView.findViewById<TextView>(R.id.nav_header_user)
        navHeaderUser.text = dataStorage.activeUserData.userLogin
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.menu_refresh -> {
                homePresenter.requestSettings()
                homePresenter.requestStatsBySectionSummary()
            }
            R.id.menu_about -> {
                showAboutPopup(this)
            }
            R.id.menu_logout -> {
                moneyCalcServerDAO.token = null
                dataStorage.clearStorage()
                changeActivityOnCondition(true, this, LoginActivity::class.java)
            }
            else -> {
            }
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_history) {
            val intent = Intent(this@HomeActivity, HistoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        } else if (id == R.id.nav_stats) {
            val intent = Intent(this@HomeActivity, StatisticsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        } else if (id == R.id.nav_spending_sections) {
            val intent = Intent(this@HomeActivity, SpendingSectionsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
