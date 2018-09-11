package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionDeleteContainer;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.ADDED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.DELETED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.EDITED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

@Singleton
public class HistoryPresenter implements HistoryContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;
    private final DataStorage dataStorage;
    private final EventBus eventBus;

    @Nullable
    private HistoryContract.View view;

    @Inject
    HistoryPresenter(MoneyCalcServerDAO moneyCalcServerDAO, DataStorage dataStorage, EventBus eventBus) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.dataStorage = dataStorage;
        this.eventBus = eventBus;

        eventBus.subscribeTransactionEvent()
                .subscribe(new Action1<CrudEvent>() {
                    @Override
                    public void call(CrudEvent transactionEvent) {
                        if (transactionEvent.equals(ADDED) || transactionEvent.equals(DELETED)
                                || transactionEvent.equals(EDITED))
                            requestTransactions();
                    }
                });
    }

    @Override
    public void takeView(HistoryContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    @Override
    public void requestTransactions() {
        moneyCalcServerDAO.getTransactions()
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
                        System.out.println("requestTransactions. transactionsListRs = " + transactionsListRs);
                        if (transactionsListRs.isSuccessful()) {
                            dataStorage.setTransactionList(transactionsListRs.getPayload());
                            view.showTransactions(transactionsListRs.getPayload());
                        } else {
                            eventBus.addErrorEvent(transactionsListRs.getMessage());
//                            view.showErrorMessage(transactionsListRs.getMessage());
                        }
                        view.hideSpinner();
                    }
                });
    }

    @Override
    public void deleteTransaction(Integer id) {

        moneyCalcServerDAO.deleteTransaction(new TransactionDeleteContainer(id))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<Void>>() {
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
                    public void onNext(MoneyCalcRs<Void> rs) {
                        System.out.println("requestTransactions. rs = " + rs);
                        if (rs.isSuccessful()) {
                            dataStorage.getTransactionList().clear();
                            eventBus.addTransactionEvent(DELETED);
                            view.showDeleteSuccess();
//                            view.showDeleteSuccess(getContext().getString(R.string.transaction_delete_success));
                        } else {
                            view.showErrorMessage(rs.getMessage());
                        }
                        view.hideSpinner();
                    }
                });

    }


    private List<Integer> getSpendingSectionIds(List<SpendingSection> spendingSections) {
        List<Integer> ids = new ArrayList<>();
        for (SpendingSection spendingSection : spendingSections) {
            ids.add(spendingSection.getId());
        }
        return ids;
    }
}
