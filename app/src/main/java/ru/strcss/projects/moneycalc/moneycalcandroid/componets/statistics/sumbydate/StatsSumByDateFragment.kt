package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.BaseStatistics
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDateLegacy
import javax.inject.Inject

/**
 * Created by Stormcss
 * Date: 28.05.2019
 */
@ActivityScoped
class StatsSumByDateFragment @Inject
constructor() : DaggerFragment(), StatisticsSumByDateContract.View {

    @Inject
    lateinit var presenter: StatisticsSumByDateContract.Presenter

    @Inject
    lateinit var dataStorage: DataStorage

    // UI references
    private var rvStatsItems: RecyclerView? = null
    private var adapter: StatsSumByDateAdapter? = null

    private var statsItems: ItemsContainer<SumByDateLegacy>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.stats_sum_by_date_frag, container, false)

        statsItems = ItemsContainer(0L, BaseStatistics.buildEmpty(), emptyList())
        // Set up the login form.

        rvStatsItems = root.findViewById(R.id.rv_stats_sum_by_date)

        adapter = StatsSumByDateAdapter(context!!, presenter, statsItems!!, dataStorage)

        val mLayoutManager = GridLayoutManager(context, 1)
        rvStatsItems!!.layoutManager = mLayoutManager
        rvStatsItems!!.itemAnimator = DefaultItemAnimator()
        rvStatsItems!!.adapter = adapter

        presenter.requestStatsByDateSum()
//        presenter.requestStatsByDateSum(StatisticsFilterLegacy("2018-01-01", "2019-09-01", emptyList()))

        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    override fun showStatsSumByDate(statsItems: ItemsContainer<SumByDateLegacy>) {
        adapter?.updateStats(statsItems)
    }

    override fun showErrorMessage(msg: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun showSpinner() {
//        val animTime = resources.getInteger(android.R.integer.config_mediumAnimTime)
//        showProgress(true, mLoginFormView, progressView, animTime)
    }

    override fun hideSpinner() {
//        val animTime = resources.getInteger(android.R.integer.config_mediumAnimTime)
//        showProgress(false, mLoginFormView, progressView, animTime)
    }

    override fun navigateToLoginActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
