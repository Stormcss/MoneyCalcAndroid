package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings;

import ru.strcss.projects.moneycalc.enitities.SettingsLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the {@link SettingsPresenter}.
 */
public interface SettingsContract {
    interface View extends BaseView<Presenter> {
        void showUpdateSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void updateSettings(SettingsLegacy settings);
    }
}
