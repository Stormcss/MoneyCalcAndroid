package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy

/**
 * This specifies the contract between the view and the loginPresenter.
 */
interface HistoryContract {
    interface View : BaseView<Presenter> {

        fun showTransactions(transactions: List<TransactionLegacy>?)

        fun showDeleteSuccess()

        fun showSpinner()

        fun hideSpinner()

        fun showFilterWindow()

        fun hideFilterWindow()

        fun navigateToLoginActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun requestTransactions()

        fun deleteTransaction(id: Int?)
    }
}
