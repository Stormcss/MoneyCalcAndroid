package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;

@Singleton
public class ApplicationSettingsPresenter implements ApplicationSettingsContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    @Inject
    ApplicationSettingsPresenter(MoneyCalcServerDAO moneyCalcServerDAO) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
    }

    @Nullable
    private ApplicationSettingsContract.View view;

    @Override
    public void takeView(ApplicationSettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    @Override
    public void updateServerIP(String serverIP, String serverPort) {
        moneyCalcServerDAO.saveServerIp(serverIP, serverPort);
    }
}
