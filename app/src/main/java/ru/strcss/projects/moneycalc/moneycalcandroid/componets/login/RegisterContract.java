package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials;

/**
 * This specifies the contract between the view and the RegisterPresenter.
 */
public interface RegisterContract {
    interface View extends BaseView<Presenter> {

        void showMainActivity();

        void showSpinner();

        void hideSpinner();
    }

    interface Presenter extends BasePresenter<View> {
        void attemptRegister(Credentials credentials);
    }
}
