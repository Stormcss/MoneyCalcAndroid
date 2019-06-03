package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView

/**
 * This specifies the contract between the view and the [ApplicationSettingsPresenter].
 */
interface ApplicationSettingsContract {
    interface View : BaseView<Presenter> {
        fun showUpdateSuccess()
    }

    interface Presenter : BasePresenter<View> {
        fun updateServerIP(serverIP: String, serverPort: String)
    }
}
