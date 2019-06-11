package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchLegacyRs

/**
 * This specifies the contract between the view and the loginPresenter.
 */
interface HistoryContract {
    interface View : BaseView<Presenter> {

        fun showTransactions(transactions: TransactionsSearchLegacyRs?)

        fun showDeleteSuccess()

        fun showSpinner()

        fun hideSpinner()

        fun showFilterWindow()

        fun hideFilterWindow()

        fun navigateToLoginActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun requestTransactions(transactionsFilter: TransactionsSearchFilterLegacy)

        fun requestTransactions()

        fun deleteTransaction(id: Int?)
    }
}
