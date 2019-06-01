package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydatesection

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.StatisticsFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDateSectionLegacy

/**
 * Created by Stormcss
 * Date: 29.05.2019
 */
interface StatisticsSumByDateSectionContract {
    interface View : BaseView<Presenter> {

        fun showStatsSumByDateSection(statsItems: ItemsContainer<SumByDateSectionLegacy>)

        fun showSpinner()

        fun hideSpinner()

        fun navigateToLoginActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun requestStatsSumByDateSection()

        fun setFilter(filter: StatisticsFilterLegacy, isStatsUpdateRequired: Boolean)
    }
}
