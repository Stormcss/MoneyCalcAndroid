package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView

/**
 * This specifies the contract between the view and the HomeStatsPresenter.
 */
interface HomeStatsContract {
    interface View : BaseView<Presenter> {

        override fun showErrorMessage(msg: String)
        //        void setTodayBalance(double todayBalance);
        //        void setSummaryBalance(double summaryBalance);
        //        void setSpendAll(int spendAll);
        //        void setLeftAll(int leftAll);
    }

    interface Presenter : BasePresenter<View>
}
