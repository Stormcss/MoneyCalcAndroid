package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the {@link ApplicationSettingsPresenter}.
 */
public interface ApplicationSettingsContract {
    interface View extends BaseView<Presenter> {
        void showUpdateSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void updateServerIP(String serverIP, String serverPort);
    }
}
