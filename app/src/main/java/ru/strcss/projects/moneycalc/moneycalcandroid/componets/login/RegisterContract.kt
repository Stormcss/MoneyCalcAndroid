package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials

/**
 * This specifies the contract between the view and the RegisterPresenter.
 */
interface RegisterContract {
    interface View : BaseView<Presenter> {

        fun showMainActivity()

        fun showSpinner()

        fun hideSpinner()

        fun saveLoginToPreferences(login: String)
    }

    interface Presenter : BasePresenter<View> {
        fun attemptRegister(credentials: Credentials)
    }
}
