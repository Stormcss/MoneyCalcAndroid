package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections

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
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.changeActivityOnCondition
import javax.inject.Inject

class SpendingSectionsActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var presenter: SpendingSectionsPresenter

    @Inject
    lateinit var spendingSectionsFragmentProvider: Lazy<SpendingSectionsFragment>

    @Inject
    lateinit var moneyCalcServerDAO: MoneyCalcServerDAO

    @Inject
    lateinit var dataStorage: DataStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spendingsections_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.menu_spending_sections)
        setSupportActionBar(toolbar)

        changeActivityOnCondition(moneyCalcServerDAO.token == null, this@SpendingSectionsActivity, LoginActivity::class.java)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        var spendingSectionsFragment: SpendingSectionsFragment? =
                supportFragmentManager.findFragmentById(R.id.spendingSections_contentFrame) as? SpendingSectionsFragment
        if (spendingSectionsFragment == null) {
            // Get the fragment from dagger
            spendingSectionsFragment = spendingSectionsFragmentProvider.get()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, spendingSectionsFragment!!, R.id.spendingSections_contentFrame)
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
            R.id.menu_settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
            }
            R.id.menu_refresh -> presenter.requestSpendingSections()
            R.id.menu_logout -> {
                moneyCalcServerDAO.token = null
                changeActivityOnCondition(true, this, LoginActivity::class.java)
            }
            else -> {
            }
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_home) {
            startActivity(Intent(this@SpendingSectionsActivity, HomeActivity::class.java))
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        } else if (id == R.id.nav_history) {
            val intent = Intent(this@SpendingSectionsActivity, HistoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            //        } else if (id == R.id.nav_stats) {
            //
            //        } else if (id == R.id.nav_spending_sections) {
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
