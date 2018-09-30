package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainerLegacy;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.SettingsLegacy;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.Event;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.Event.SETTING_UPDATED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

@Singleton
public class HomePresenter implements HomeContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;
    private final DataStorage dataStorage;
    private final EventBus eventBus;

    @Nullable
    private HomeContract.View homeView;

    @Inject
    HomePresenter(MoneyCalcServerDAO moneyCalcServerDAO, DataStorage dataStorage, EventBus eventBus) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.dataStorage = dataStorage;
        this.eventBus = eventBus;

        subscribeEvents();
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
        moneyCalcServerDAO.getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<SettingsLegacy>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                    }

                    @Override
                    public void onNext(MoneyCalcRs<SettingsLegacy> settingsRs) {
                        if (settingsRs.isSuccessful()) {
                            dataStorage.setSettings(settingsRs.getPayload());
                            if (homeView != null) {
                                homeView.showDatesRange(settingsRs.getPayload().getPeriodFrom(),
                                        settingsRs.getPayload().getPeriodTo());
                            }
                        } else {
                            eventBus.addErrorEvent(settingsRs.getMessage());
                            //                            homeView.showErrorMessage(settingsRs.toString());
                        }
                    }
                });
    }

    @Override
    public void requestSpendingSections() {
        moneyCalcServerDAO.getSpendingSections()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<SpendingSection>>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<SpendingSection>> sectionsRs) {
                        if (sectionsRs.isSuccessful()) {
                            dataStorage.setSpendingSections(sectionsRs.getPayload());
                        }
                    }
                });
    }

    @Override
    public void requestSectionStatistics() {
        moneyCalcServerDAO.getFinanceSummaryBySection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<FinanceSummaryBySection>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<FinanceSummaryBySection>> statsRs) {
                        if (statsRs.isSuccessful()) {
                            dataStorage.setFinanceSummary(statsRs.getPayload());
                            if (homeView != null) {
                                homeView.showStatisticsSections(statsRs.getPayload());
                            }
                            //                            homeView.setStatisticsSection(statsRs.getPayload());
                        } else {
                            eventBus.addErrorEvent(statsRs.getMessage());
                            //                            homeView.showErrorMessage(statsRs.toString());
                        }
                    }
                });
    }

    @Override
    public void requestSectionStatistics(String from, String to, List<Integer> sections) {
        moneyCalcServerDAO.getFinanceSummaryBySection(new FinanceSummaryGetContainerLegacy(from, to, sections))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<FinanceSummaryBySection>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<FinanceSummaryBySection>> statsRs) {
                        if (statsRs.isSuccessful()) {
                            dataStorage.setFinanceSummary(statsRs.getPayload());
                            if (homeView != null) {
                                homeView.showStatisticsSections(statsRs.getPayload());
                            }
                            //                            homeView.setStatisticsSection(statsRs.getPayload());
                        } else {
                            eventBus.addErrorEvent(statsRs.getMessage());
                            //                            homeView.showErrorMessage(statsRs.toString());
                        }
                    }
                });
    }

    @Override
    public void showAddTransactionActivity() {
        if (homeView != null) {
            homeView.showAddTransactionActivity();
        }
    }

    private void subscribeEvents() {
        this.eventBus.subscribeTransactionEvent().subscribe(new Action1<CrudEvent>() {
            @Override
            public void call(CrudEvent event) {
                if (event.equals(CrudEvent.ADDED) || event.equals(CrudEvent.DELETED) ||
                        event.equals(CrudEvent.EDITED)) {
                    requestSettings();
                    requestSectionStatistics();
                }
            }
        });
        this.eventBus.subscribeEvent().subscribe(new Action1<Event>() {
            @Override
            public void call(Event event) {
                if (event.equals(SETTING_UPDATED)) {
                    requestSettings();
                    requestSectionStatistics();
                }
            }
        });
    }
}
