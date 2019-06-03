package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection

/**
 * This specifies the contract between the view and the loginPresenter.
 */
interface SpendingSectionsContract {
    interface View : BaseView<Presenter> {

        fun showSpendingSections(spendingSections: List<SpendingSection>)

        fun showSpinner()

        fun hideSpinner()

        fun showUpdateSuccess()

        fun showDeleteSuccess()
    }

    interface Presenter : BasePresenter<View> {
        fun requestSpendingSections()

        fun deleteSpendingSection(id: Int?)
    }
}
