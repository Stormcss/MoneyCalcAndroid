package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import dagger.android.support.DaggerAppCompatActivity
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.HistoryActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate.StatisticsSumByDateContract
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydatesection.StatisticsSumByDateSectionContract
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbysection.StatisticsSumBySectionContract
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.changeActivityOnCondition
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.*
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.StatisticsFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy
import java.util.*
import javax.inject.Inject


/**
 * Created by Stormcss
 * Date: 28.05.2019
 */
class StatisticsActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        StatisticsContract.View {
    @Inject
    lateinit var moneyCalcServerDAO: MoneyCalcServerDAO

    @Inject
    lateinit var dataStorage: DataStorage

    @Inject
    lateinit var statisticsPresenter: StatisticsContract.Presenter
    @Inject
    lateinit var sumBySectionPresenter: StatisticsSumBySectionContract.Presenter
    @Inject
    lateinit var sumByDatePresenter: StatisticsSumByDateContract.Presenter
    @Inject
    lateinit var sumByDateSectionPresenter: StatisticsSumByDateSectionContract.Presenter

    private lateinit var twDateFrom: TextView
    private lateinit var twDateTo: TextView

    private lateinit var filter: StatisticsFilterLegacy

    private val onDateFromSetListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        val date = DatesUtils.getIsoDate(year, month + 1, dayOfMonth)
        twDateFrom.text = formatDateToPretty(date)
        filter.dateFrom = date
        sumBySectionPresenter.setFilter(filter, true)
        sumByDatePresenter.setFilter(filter, true)
        sumByDateSectionPresenter.setFilter(filter, true)
    }

    private val onDateToSetListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        val date = DatesUtils.getIsoDate(year, month + 1, dayOfMonth)
        twDateTo.text = formatDateToPretty(date)
        filter.dateTo = date

        sumBySectionPresenter.setFilter(filter, true)
        sumByDatePresenter.setFilter(filter, true)
        sumByDateSectionPresenter.setFilter(filter, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stats_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.menu_stats)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        twDateFrom = findViewById(R.id.stats_date_from)
        twDateTo = findViewById(R.id.stats_date_to)

        //filter and period buttons listener setup
        val storedSettings = dataStorage.settings
        if (storedSettings != null) {
            setUpStatisticsPeriodData(storedSettings)
        } else {
            statisticsPresenter.requestSettings()
        }
//        filter = setUpFilter(dataStorage.settings)
//        addFilterToPresenters()
//        setUpStatisticsPeriodListeners(filter)

        //fragments setup
        val tabLayout = findViewById<TabLayout>(R.id.stats_tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.stats_by_section_sum)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.stats_by_date_sum)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.stats_by_date_section_sum)))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = findViewById<ViewPager>(R.id.stats_viewPager)
        val adapter = StatisticsPagerAdapter(supportFragmentManager, tabLayout.tabCount)
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

        setUpNavigationViewLogin(navigationView)
    }

    /**
     * Two different threads call this method. First when settings is received,
     * second when spendingSections is received.
     */
    override fun drawStatisticsFragments() {
        if (dataStorage.settings != null && dataStorage.spendingSections != null)
            addFilterToPresenters(true)
    }

    override fun showSpinner() {
        TODO("not implemented")
    }

    override fun hideSpinner() {
        TODO("not implemented")
    }

    override fun showErrorMessage(msg: String) {
        TODO("not implemented")
    }

    override fun setUpStatisticsPeriodData(settingsLegacy: SettingsLegacy) {
        filter = setUpFilter(settingsLegacy)
        addFilterToPresenters(false)
        setUpStatisticsPeriodListeners(filter)
    }

    private fun addFilterToPresenters(isStatsUpdateRequired: Boolean) {
        sumBySectionPresenter.setFilter(filter, isStatsUpdateRequired)
        sumByDatePresenter.setFilter(filter, isStatsUpdateRequired)
        sumByDateSectionPresenter.setFilter(filter, isStatsUpdateRequired)
    }

    private fun setUpFilter(settings: SettingsLegacy): StatisticsFilterLegacy {
        return StatisticsFilterLegacy(settings.periodFrom, settings.periodTo, null)
//        return dataStorage.statisticsFilter
//                ?: StatisticsFilterLegacy(settings?.periodFrom, settings?.periodTo, null)
    }

    private fun setUpStatisticsPeriodListeners(savedFilter: StatisticsFilterLegacy?) {
        if (savedFilter != null) {
            twDateFrom.text = formatDateToPretty(savedFilter.dateFrom)
            twDateTo.text = formatDateToPretty(savedFilter.dateTo)
        } else {
            val calendar = formatDateToPretty(getStringIsoDateFromCalendar(Calendar.getInstance()))
            twDateFrom.text = calendar
            twDateTo.text = calendar
        }

        twDateFrom.setOnClickListener {
            if (savedFilter?.dateFrom != null) {
                getDatePickerDialog(onDateFromSetListener, getCalendarFromString(savedFilter.dateFrom)).show()
            } else {
                getDatePickerDialog(onDateFromSetListener, Calendar.getInstance()).show()
            }
        }
        twDateTo.setOnClickListener {
            if (savedFilter?.dateTo != null) {
                getDatePickerDialog(onDateToSetListener, getCalendarFromString(savedFilter.dateTo)).show()
            } else {
                getDatePickerDialog(onDateToSetListener, Calendar.getInstance()).show()
            }
        }
    }

    private fun getDatePickerDialog(listener: DatePickerDialog.OnDateSetListener, calendar: Calendar): DatePickerDialog {
        return DatePickerDialog(this, listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
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

    override fun onResume() {
        super.onResume()
        statisticsPresenter.takeView(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
            }
            R.id.menu_refresh -> {
            }
            R.id.menu_about -> {
                ActivityUtils.showAboutPopup(this)
            }
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
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_home) {
            startActivity(Intent(this@StatisticsActivity, HomeActivity::class.java))
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        } else if (id == R.id.nav_history) {
            val intent = Intent(this@StatisticsActivity, HistoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        } else if (id == R.id.nav_stats) {
        } else if (id == R.id.nav_spending_sections) {
            val intent = Intent(this@StatisticsActivity, SpendingSectionsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Set up showing active login in navigation header view
     */
    private fun setUpNavigationViewLogin(navigationView: NavigationView) {
        val headerView = navigationView.getHeaderView(0)
        val navHeaderUser = headerView.findViewById<TextView>(R.id.nav_header_user)
        navHeaderUser.text = dataStorage.activeUserData.userLogin
    }
}
