package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SummaryBySection

/**
 * This specifies the contract between the view and the homePresenter.
 */
interface HomeContract {
    interface View : BaseView<Presenter> {

        fun showDatesRange(from: String, to: String)

        fun showStatisticsSections(financeSummary: List<SummaryBySection>?)

        fun showAddTransactionActivity()

        fun updateStatsAndSettings()

        fun redrawTabLogos()
    }

    interface Presenter : BasePresenter<View> {
        fun requestSettings()

        fun requestSpendingSections()

        fun requestStatsBySectionSummary()

//        fun requestSectionStatistics(from: String, to: String, sections: List<Int>)

        fun showAddTransactionActivity()
    }
}
