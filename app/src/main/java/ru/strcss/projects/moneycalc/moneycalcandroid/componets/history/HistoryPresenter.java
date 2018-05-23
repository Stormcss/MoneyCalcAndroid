package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionDeleteContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionsSearchContainer;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.Transaction;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class HistoryPresenter implements HistoryContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;
    private final DataStorage dataStorage;

    @Nullable
    private HistoryContract.View view;

    @Inject
    HistoryPresenter(MoneyCalcServerDAO moneyCalcServerDAO, DataStorage dataStorage) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.dataStorage = dataStorage;
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
        String periodFrom = dataStorage.getSettings().getPeriodFrom();
        String periodTo = dataStorage.getSettings().getPeriodTo();
        List<Integer> spendingSectionIds = getSpendingSectionIds(dataStorage.getSettings().getSections());
        TransactionsSearchContainer searchContainer =
                new TransactionsSearchContainer(periodFrom, periodTo, spendingSectionIds);

        moneyCalcServerDAO.getTransactions(moneyCalcServerDAO.getToken(), searchContainer)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<Transaction>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("requestTransactions onError!!!!! " + e.getMessage());
                        view.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<Transaction>> rs) {
                        System.out.println("requestTransactions. rs = " + rs);
                        if (rs.isSuccessful()) {
                            dataStorage.setTransactionList(rs.getPayload());
                            view.showTransactions(rs.getPayload());
                        } else {
                            view.showErrorMessage(rs.getMessage());
                        }
                        view.hideSpinner();
                    }
                });
    }

    @Override
    public void deleteTransaction(String id) {

        moneyCalcServerDAO.deleteTransaction(moneyCalcServerDAO.getToken(), new TransactionDeleteContainer(id))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<Void>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("requestTransactions onError!!!!! " + e.getMessage());
                        view.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(MoneyCalcRs<Void> rs) {
                        System.out.println("requestTransactions. rs = " + rs);
                        if (rs.isSuccessful()) {
                            dataStorage.getTransactionList().clear();
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
