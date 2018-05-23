package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainer;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.Settings;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getSpendingSectionIds;

@Singleton
public class HomePresenter implements HomeContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;
    private final DataStorage dataStorage;

    @Nullable
    private HomeContract.View homeView;

    @Inject
    HomePresenter(MoneyCalcServerDAO moneyCalcServerDAO, DataStorage dataStorage) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.dataStorage = dataStorage;
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
                .subscribe(new Observer<MoneyCalcRs<Settings>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.showErrorMessage(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MoneyCalcRs<Settings> settingsRs) {
                        if (settingsRs.isSuccessful()) {
                            dataStorage.setSettings(settingsRs.getPayload());

                            List<Integer> spendingSectionIds = getSpendingSectionIds(settingsRs.getPayload().getSections());

                            homeView.setDatesRange(settingsRs.getPayload().getPeriodFrom(),
                                    settingsRs.getPayload().getPeriodTo(), spendingSectionIds);
                        } else {
                            homeView.showErrorMessage(settingsRs.getMessage());
                        }
                    }
                });
    }

    @Override
    public void requestSectionStatistics(String from, String to, List<Integer> sections) {
        FinanceSummaryGetContainer getContainer = new FinanceSummaryGetContainer(from, to, sections);
        moneyCalcServerDAO.getFinanceSummaryBySection(moneyCalcServerDAO.getToken(), getContainer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<FinanceSummaryBySection>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.showErrorMessage(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<FinanceSummaryBySection>> statsRs) {
                        if (statsRs.isSuccessful()) {
                            dataStorage.setFinanceSummary(statsRs.getPayload());
                            homeView.setStatisticsSections(dataStorage.getSettings().getSections(),
                                    statsRs.getPayload());
                            //                            homeView.setStatisticsSection(statsRs.getPayload());
                        } else {
                            homeView.showErrorMessage(statsRs.getMessage());
                        }
                    }
                });
    }

    @Override
    public void addTransaction() {
        if (homeView != null) {
            homeView.showAddTransaction();
        }
    }
}
