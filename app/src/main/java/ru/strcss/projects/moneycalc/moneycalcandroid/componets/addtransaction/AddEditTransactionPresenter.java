package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addtransaction;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionAddContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionUpdateContainer;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.Transaction;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class AddEditTransactionPresenter implements AddEditTransactionContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    @Nullable
    private AddEditTransactionContract.View view;

    @Inject
    AddEditTransactionPresenter(MoneyCalcServerDAO moneyCalcServerDAO) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
    }

    @Override
    public void takeView(AddEditTransactionContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    @Override
    public void addTransaction(final Transaction transaction) {
        TransactionAddContainer container = new TransactionAddContainer(transaction);
        moneyCalcServerDAO.addTransaction(moneyCalcServerDAO.getToken(), container)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AjaxRs<Transaction>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMessage(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AjaxRs<Transaction> transactionAjaxRs) {
                        if (transactionAjaxRs.isSuccessful()) {
                            view.showAddSuccess();
                        } else {
                            view.showErrorMessage(transactionAjaxRs.getMessage());
                        }
                    }
                });
    }

    @Override
    public void editTransaction(String id, Transaction transaction) {
        TransactionUpdateContainer container = new TransactionUpdateContainer(id, transaction);
        moneyCalcServerDAO.updateTransaction(moneyCalcServerDAO.getToken(), container)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AjaxRs<Transaction>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMessage(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AjaxRs<Transaction> transactionAjaxRs) {
                        if (transactionAjaxRs.isSuccessful()) {
                            view.showEditSuccess();
                        } else {
                            view.showErrorMessage(transactionAjaxRs.getMessage());
                        }
                    }
                });

    }

    @Override
    public void requestSpendingSectionsList() {
        moneyCalcServerDAO.getSpendingSections(moneyCalcServerDAO.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AjaxRs<List<SpendingSection>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMessage(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AjaxRs<List<SpendingSection>> sectionsRs) {
                        if (sectionsRs.isSuccessful()) {
                            view.showSpendingSections(sortSpendingSectionListById(sectionsRs.getPayload()));
                        } else {
                            view.showErrorMessage(sectionsRs.getMessage());
                        }
                    }
                });
    }

    private List<SpendingSection> sortSpendingSectionListById(List<SpendingSection> sectionList) {
        Collections.sort(sectionList, new Comparator<SpendingSection>() {
            @Override
            public int compare(SpendingSection o1, SpendingSection o2) {
                return o1.getId() - o2.getId();
            }
        });
        return sectionList;
    }


}
