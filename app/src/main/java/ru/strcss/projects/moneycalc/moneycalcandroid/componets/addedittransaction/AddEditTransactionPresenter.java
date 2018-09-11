package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionAddContainerLegacy;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.ADDED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.EDITED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

@Singleton
public class AddEditTransactionPresenter implements AddEditTransactionContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    private final EventBus eventBus;

    @Nullable
    private AddEditTransactionContract.View view;

    @Inject
    AddEditTransactionPresenter(MoneyCalcServerDAO moneyCalcServerDAO, EventBus eventBus) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.eventBus = eventBus;
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
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                        //                        showErrorMessageFromException(ex, view);
                        //                        String errorBodyMessage = getErrorBodyMessage((HttpException) ex);
//                        System.err.println("showAddTransactionActivity onErro: " + errorBodyMessage);
//                        view.showErrorMessage(errorBodyMessage);
                    }

                    @Override
                    public void onNext(MoneyCalcRs<TransactionLegacy> transactionRs) {
                        if (transactionRs.isSuccessful()) {
                            eventBus.addTransactionEvent(ADDED);
                            if (view != null) {
                                view.showAddSuccess();
                            }
                        } else {
                            eventBus.addErrorEvent(transactionRs.getMessage());
                            //                            System.err.println(transactionRs);
//                            view.showErrorMessage(transactionRs.toString());
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
                    public void onError(Throwable ex) {
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                        //                        showErrorMessageFromException(ex, view);
                        //                        view.showErrorMessage(ex.getMessage());
//                        ex.printStackTrace();
                    }

                    @Override
                    public void onNext(MoneyCalcRs<TransactionLegacy> transactionRs) {
                        if (transactionRs.isSuccessful()) {
                            eventBus.addTransactionEvent(EDITED);
                            if (view != null) {
                                view.showEditSuccess();
                            }
                        } else {
                            eventBus.addErrorEvent(transactionRs.getMessage());
                            //                            System.err.println(transactionRs);
//                            view.showErrorMessage(transactionRs.toString());
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
                        //                        showErrorMessageFromException(ex, view);
                        //                        view.showErrorMessage(ex.getMessage());
//                        ex.printStackTrace();
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
