package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.HttpException;
import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionAddContainerLegacy;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

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
    public void addTransaction(final TransactionLegacy transaction) {
        moneyCalcServerDAO.addTransaction(new TransactionAddContainerLegacy(transaction))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<TransactionLegacy>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorBodyMessage = getErrorBodyMessage((HttpException) e);
                        e.printStackTrace();
                        System.err.println("addTransaction onErro: " + errorBodyMessage);
                        view.showErrorMessage(errorBodyMessage);
                    }

                    @Override
                    public void onNext(MoneyCalcRs<TransactionLegacy> transactionRs) {
                        if (transactionRs.isSuccessful()) {
                            view.showAddSuccess();
                        } else {
                            System.err.println(transactionRs);
                            view.showErrorMessage(transactionRs.toString());
                        }
                    }
                });
    }

    @Override
    public void editTransaction(Integer id, TransactionLegacy transaction) {
        TransactionUpdateContainerLegacy container = new TransactionUpdateContainerLegacy(id, transaction);
        moneyCalcServerDAO.updateTransaction(container)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<TransactionLegacy>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMessage(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MoneyCalcRs<TransactionLegacy> transactionRs) {
                        if (transactionRs.isSuccessful()) {
                            view.showEditSuccess();
                        } else {
                            System.err.println(transactionRs);
                            view.showErrorMessage(transactionRs.toString());
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
                    public void onError(Throwable e) {
                        view.showErrorMessage(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<SpendingSection>> sectionsRs) {
                        if (sectionsRs.isSuccessful()) {
                            view.showSpendingSections(sortSpendingSectionListById(sectionsRs.getPayload()));

                        } else {
                            view.showErrorMessage(sectionsRs.toString());
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
