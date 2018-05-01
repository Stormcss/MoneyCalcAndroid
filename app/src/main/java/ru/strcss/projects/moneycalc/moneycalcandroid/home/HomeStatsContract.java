package ru.strcss.projects.moneycalc.moneycalcandroid.home;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the HomeStatsPresenter.
 */
public interface HomeStatsContract {
    interface View extends BaseView<Presenter> {

        void showErrorMessage(String msg);
//        void setTodayBalance(double todayBalance);
//        void setSummaryBalance(double summaryBalance);
//        void setSpendAll(int spendAll);
//        void setLeftAll(int leftAll);
    }

    interface Presenter extends BasePresenter<View> {

    }
}
