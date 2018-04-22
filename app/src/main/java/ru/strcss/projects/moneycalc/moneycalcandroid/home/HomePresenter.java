package ru.strcss.projects.moneycalc.moneycalcandroid.home;

import android.support.annotation.Nullable;

import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainer;
import ru.strcss.projects.moneycalc.enitities.Settings;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class HomePresenter implements HomeContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;
    private final DataStorage dataStorage;

    private int fuck;

    @Nullable
    private HomeContract.View homeView;

    @Inject
    HomePresenter(MoneyCalcServerDAO moneyCalcServerDAO, DataStorage dataStorage) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.dataStorage = dataStorage;

        fuck = ThreadLocalRandom.current().nextInt();
    }


    @Override
    public void takeView(HomeContract.View view) {
        homeView = view;
    }

    @Override
    public void dropView() {
        homeView = null;
    }

    @Override
    public void requestFinanceSummary(FinanceSummaryGetContainer financeSummaryGetContainer) {

    }

    @Override
    public void requestSettings() {
        moneyCalcServerDAO.getSettings(moneyCalcServerDAO.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AjaxRs<Settings>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AjaxRs<Settings> settingsRs) {
                        if (settingsRs.isSuccessful()) {
                            dataStorage.setSettings(settingsRs.getPayload());
                        } else {
                            homeView.showErrorMessage(settingsRs.getMessage());
                        }
                    }
                });

    }

    @Override
    public void updateHomeScreen() {
        System.out.println("fuck = " + fuck);
        moneyCalcServerDAO.getSettings(moneyCalcServerDAO.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AjaxRs<Settings>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AjaxRs<Settings> settingsRs) {
                        if (settingsRs.isSuccessful()) {
                            dataStorage.setSettings(settingsRs.getPayload());
                            homeView.setDatesRange(settingsRs.getPayload().getPeriodFrom(), settingsRs.getPayload().getPeriodTo());
                            homeView.setSections(settingsRs.getPayload().getSections());
                        } else {
                            homeView.showErrorMessage(settingsRs.getMessage());
                        }
                    }
                });

    }
}
