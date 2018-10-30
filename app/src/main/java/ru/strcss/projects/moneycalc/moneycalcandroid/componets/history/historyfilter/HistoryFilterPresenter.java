package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionsSearchContainerLegacy;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

@Singleton
public class HistoryFilterPresenter implements HistoryFilterContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;
    private final DataStorage dataStorage;
    private final EventBus eventBus;

    @Nullable
    private HistoryFilterContract.View view;

    @Inject
    HistoryFilterPresenter(MoneyCalcServerDAO moneyCalcServerDAO, DataStorage dataStorage, EventBus eventBus) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.dataStorage = dataStorage;
        this.eventBus = eventBus;
    }

    @Override
    public void takeView(HistoryFilterContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    @Override
    public void requestFilteredTransactions(final TransactionsSearchContainerLegacy filter) {
        moneyCalcServerDAO.getTransactions(filter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<TransactionLegacy>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable ex) {
                        System.out.println("requestTransactions onError!!!!! " + ex.getMessage());
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
//                        view.showErrorMessage(ex.getMessage());
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<TransactionLegacy>> transactionsListRs) {
                        System.out.println("requestTransactions FILTERED! transactionsListRs = " + transactionsListRs);
                        System.out.println("transactionsListRs.getPayload().size() = " + transactionsListRs.getPayload().size());
                        if (transactionsListRs.isSuccessful()) {
                            dataStorage.setTransactionList(transactionsListRs.getPayload());
                            dataStorage.setTransactionsFilter(filter);
                            eventBus.addTransactionEvent(CrudEvent.REQUESTED);
                        } else {
                            eventBus.addErrorEvent(transactionsListRs.getMessage());
                        }
                    }
                });

    }

    @Override
    public void requestSpendingSectionsList() {
        moneyCalcServerDAO.getSpendingSections()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<SpendingSection>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable ex) {
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<SpendingSection>> sectionsRs) {
                        if (sectionsRs.isSuccessful()) {
                            view.showSpendingSections(sectionsRs.getPayload());
                        } else {
                            view.showErrorMessage(sectionsRs.toString());
                        }
                    }
                });

    }
}
