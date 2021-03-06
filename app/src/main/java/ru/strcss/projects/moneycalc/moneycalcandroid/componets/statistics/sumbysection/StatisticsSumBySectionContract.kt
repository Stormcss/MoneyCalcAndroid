package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbysection

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.StatisticsFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumBySection

/**
 * Created by Stormcss
 * Date: 29.05.2019
 */
interface StatisticsSumBySectionContract {
    interface View : BaseView<Presenter> {

        fun showStatsSumBySection(statsItems: ItemsContainer<SumBySection>)

        fun showSpinner()

        fun hideSpinner()

        fun navigateToLoginActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun requestStatsSumBySection()

        fun setFilter(filter: StatisticsFilterLegacy, isStatsUpdateRequired: Boolean)
    }
}
