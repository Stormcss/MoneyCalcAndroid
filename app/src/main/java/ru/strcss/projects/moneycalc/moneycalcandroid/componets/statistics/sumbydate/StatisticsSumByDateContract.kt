package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.StatisticsFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDateLegacy

/**
 * Created by Stormcss
 * Date: 29.05.2019
 */
interface StatisticsSumByDateContract {
    interface View : BaseView<Presenter> {

        fun showStatsSumByDate(statsItems: ItemsContainer<SumByDateLegacy>)

        fun showSpinner()

        fun hideSpinner()

        fun navigateToLoginActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun requestStatsByDateSum()

        fun setFilter(filter: StatisticsFilterLegacy, isStatsUpdateRequired: Boolean)
    }
}
