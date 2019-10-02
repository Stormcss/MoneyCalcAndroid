package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy

/**
 * Created by Stormcss
 * Date: 29.05.2019
 */
interface StatisticsContract {
    interface View : BaseView<Presenter> {

        fun setUpStatisticsPeriodData(settingsLegacy: SettingsLegacy)

        fun drawStatisticsFragments()

        fun showSpinner()

        fun hideSpinner()
    }

    interface Presenter : BasePresenter<View> {
        fun requestSettings()

        fun requestSpendingSections()
    }
}
