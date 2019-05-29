package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.bysectionsum

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

        fun showStatsBySectionSum(statsItems: ItemsContainer<SumBySection>)

        fun showSpinner()

        fun hideSpinner()

        fun navigateToLoginActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun requestStatsBySectionSum(filter: StatisticsFilterLegacy)

//        fun requestStatsByDateSum()
//
//        fun requestStatsByDateSumBySection()
    }
}
