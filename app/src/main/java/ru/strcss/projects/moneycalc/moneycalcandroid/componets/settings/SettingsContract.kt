package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy

/**
 * This specifies the contract between the view and the [SettingsPresenter].
 */
interface SettingsContract {
    interface View : BaseView<Presenter> {
        fun showUpdateSuccess()
    }

    interface Presenter : BasePresenter<View> {
        fun updateSettings(settings: SettingsLegacy)
    }
}
