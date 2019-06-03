package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection

/**
 * This specifies the contract between the view and the loginPresenter.
 */
interface HistoryFilterContract {
    interface View : BaseView<Presenter> {
        fun showSpendingSections(spendingSections: List<SpendingSection>)
    }

    interface Presenter : BasePresenter<View> {
        fun requestFilteredTransactions(filter: TransactionsSearchFilterLegacy)

        fun requestSpendingSectionsList()
    }
}
