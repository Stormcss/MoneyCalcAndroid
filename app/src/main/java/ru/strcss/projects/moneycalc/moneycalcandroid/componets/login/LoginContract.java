package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login;

import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the loginPresenter.
 */
public interface LoginContract {
    interface View extends BaseView<Presenter> {

        void showMainActivity();

        void showSpinner();

        void hideSpinner();
    }

    interface Presenter extends BasePresenter<View> {
        void attemptLogin(Access access);
    }
}
