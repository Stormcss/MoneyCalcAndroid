package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dagger.android.support.DaggerFragment
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.NumberUtils.Companion.formatNumberToPretty
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
    private lateinit var tvStatsSum: TextView
    private lateinit var rvStatsItems: RecyclerView
    private lateinit var adapter: StatsSumByDateAdapter

    private lateinit var statsItems: ItemsContainer<SumByDateLegacy>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.stats_sum_by_date_frag, container, false)

        statsItems = ItemsContainer(0L, BaseStatistics.buildEmpty(), emptyList())
        adapter = StatsSumByDateAdapter(context!!, presenter, statsItems, dataStorage)

        tvStatsSum = activity!!.findViewById(R.id.stats_sum_tv)
        rvStatsItems = root.findViewById(R.id.rv_stats_sum_by_date)

        val mLayoutManager = GridLayoutManager(context, 1)
        rvStatsItems.layoutManager = mLayoutManager
        rvStatsItems.itemAnimator = DefaultItemAnimator()
        rvStatsItems.adapter = adapter

        presenter.requestStatsByDateSum()
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
        tvStatsSum.text = formatNumberToPretty(statsItems.stats.total)
        adapter.updateStats(statsItems)
    }

    override fun showErrorMessage(msg: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun showSpinner() {
    }

    override fun hideSpinner() {
    }

    override fun navigateToLoginActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
