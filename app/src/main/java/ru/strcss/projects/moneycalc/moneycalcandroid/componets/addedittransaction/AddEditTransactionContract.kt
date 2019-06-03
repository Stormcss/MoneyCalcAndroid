package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy

/**
 * This specifies the contract between the view and the homePresenter.
 */
interface AddEditTransactionContract {
    interface View : BaseView<Presenter> {

        fun showAddSuccess()

        fun showEditSuccess()

        fun showSpendingSections(spendingSections: List<SpendingSection>)
    }

    interface Presenter : BasePresenter<View> {
        fun addTransaction(transaction: TransactionLegacy)

        fun editTransaction(id: Int?, transaction: TransactionLegacy)

        fun requestSpendingSectionsList()

    }
}
