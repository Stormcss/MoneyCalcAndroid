package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the loginPresenter.
 */
public interface RegisterContract {
    interface View extends BaseView<Presenter> {

        void showMainActivity();

        void showErrorMessage(String msg);

        void showSpinner();

        void hideSpinner();
    }

    interface Presenter extends BasePresenter<View> {
        void attemptRegister(Credentials credentials);
    }
}
