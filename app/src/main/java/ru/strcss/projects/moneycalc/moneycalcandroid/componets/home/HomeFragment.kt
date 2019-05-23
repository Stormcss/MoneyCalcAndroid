package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import dagger.android.support.DaggerFragment
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.FINANCE_SUMMARY_BY_SECTION
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.sectiontabs.TabAdapter
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.sectiontabs.TabHolder
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.formatDateToPretty
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getLogoIdBySectionId
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getStatsSummaryBySectionById
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SummaryBySection
import javax.inject.Inject

class HomeFragment @Inject
constructor() : DaggerFragment(), HomeContract.View, TabAdapter.OnItemClickListener, ViewPager.OnPageChangeListener {

    @Inject
    lateinit var presenter: HomeContract.Presenter

    @Inject
    lateinit var dataStorage: DataStorage

    // UI references
    private var tvDatesRange: TextView? = null
    private var fabAddTransaction: FloatingActionButton? = null
    private var viewPager: ViewPager? = null

    private var adapter: HomePagerAdapter? = null
    private var tabAdapter: TabAdapter? = null
    private var tabs: ListView? = null
    private var areTabLogosShown = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.home_frag, container, false)

        fabAddTransaction = root.findViewById(R.id.fab_home_addTransaction)
        fabAddTransaction!!.setOnClickListener { presenter.showAddTransactionActivity() }

        tvDatesRange = root.findViewById(R.id.home_dates_range)

        //preparing up section fragments
        viewPager = root.findViewById(R.id.home_sections_viewPager)
        adapter = HomePagerAdapter(activity!!.supportFragmentManager)
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(this)

        tabs = root.findViewById(R.id.home_section_tabs)
        if (tabs != null) {
            // landscape mode
            this.tabAdapter = TabAdapter(tabs!!, this)
            tabs!!.adapter = tabAdapter
            tabs!!.divider = null
        }

        // TODO: 22.04.2018 show spinner
        // show available data quickly
        if (dataStorage.settings != null) {
            val periodFrom = dataStorage.settings!!.periodFrom
            val periodTo = dataStorage.settings!!.periodTo
            showDatesRange(periodFrom, periodTo)
        }
        if (dataStorage.statsBySectionSummary != null) {
            showStatisticsSections(dataStorage.statsBySectionSummary?.items)
        }

        this.updateStatsAndSettings()

        //request spending sections to have copy of sections at DataStorage
        presenter.requestSpendingSections()
        return root
    }

    override fun showErrorMessage(msg: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun showDatesRange(from: String, to: String) {
        tvDatesRange!!.text = String.format("%s - %s", formatDateToPretty(from), formatDateToPretty(to))
    }

    override fun showStatisticsSections(financeSummaryList: List<SummaryBySection>?) {
        adapter!!.clearFragments()
        tabAdapter!!.data.clear()

        for (finSumBySec in financeSummaryList!!) {
            val fView = HomeStatsFragment.newInstance(finSumBySec)
            if (!fView.isAdded) {
                adapter!!.addFrag(fView, finSumBySec.sectionName)
                val tabHolder = TabHolder(finSumBySec.sectionId, null, finSumBySec.sectionName)
                if (dataStorage.spendingSections != null) {
                    tabHolder.logoId = getLogoIdBySectionId(dataStorage.spendingSections?.items, finSumBySec.sectionId)
                    areTabLogosShown = true
                }
                tabAdapter!!.data.add(tabHolder)
            }
        }
        adapter!!.notifyDataSetChanged()
        tabAdapter!!.notifyDataSetChanged()

        val fragmentManager = fragmentManager


        for (fragment in fragmentManager!!.fragments) {
            if (fragment.javaClass == HomeStatsFragment::class.java) {
                val oldSBS = fragment.arguments!!.getSerializable(FINANCE_SUMMARY_BY_SECTION) as SummaryBySection

                fragment.arguments!!.putSerializable(FINANCE_SUMMARY_BY_SECTION,
                        getStatsSummaryBySectionById(financeSummaryList, oldSBS.sectionId))
                fragmentManager.beginTransaction().detach(fragment).attach(fragment).commitAllowingStateLoss()
            }
        }
    }

    override fun showAddTransactionActivity() {
        val intent = Intent(context, AddEditTransactionActivity::class.java)
        startActivityForResult(intent, 0)
    }

    override fun updateStatsAndSettings() {
        presenter.requestSettings()
        presenter.requestStatsBySectionSummary()
    }

    override fun redrawTabLogos() {
        if (!areTabLogosShown && dataStorage.statsBySectionSummary != null) {
            showStatisticsSections(dataStorage.statsBySectionSummary?.items)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        tabAdapter?.setCurrentSelected(position)
//        if (tabAdapter != null) {
//        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun selectItem(position: Int) {
        viewPager?.setCurrentItem(position, true)
    }
}
