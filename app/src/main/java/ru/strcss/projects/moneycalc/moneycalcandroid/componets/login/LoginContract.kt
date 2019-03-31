package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.entities.Access

/**
 * This specifies the contract between the view and the loginPresenter.
 */
interface LoginContract {
    interface View : BaseView<Presenter> {

        fun showMainActivity()

        fun showSpinner()

        fun hideSpinner()

        fun saveLoginToPreferences(login: String)
    }

    interface Presenter : BasePresenter<View> {
        fun attemptLogin(access: Access)
    }
}
