package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection

/**
 * This specifies the contract between the view and the homePresenter.
 */
interface AddEditSpendingSectionContract {
    interface View : BaseView<Presenter> {

        fun showAddSuccess()

        fun showEditSuccess()
    }

    interface Presenter : BasePresenter<View> {
        fun addSpendingSection(spendingSection: SpendingSection)

        fun editSpendingSection(id: Int?, spendingSection: SpendingSection)
    }
}
