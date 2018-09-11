package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionAddContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionUpdateContainer;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
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
public class AddEditSpendingSectionPresenter implements AddEditSpendingSectionContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    private final EventBus eventBus;

    @Nullable
    private AddEditSpendingSectionContract.View view;

    @Inject
    AddEditSpendingSectionPresenter(MoneyCalcServerDAO moneyCalcServerDAO, EventBus eventBus) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.eventBus = eventBus;
    }

    @Override
    public void takeView(AddEditSpendingSectionContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    @Override
    public void addSpendingSection(SpendingSection spendingSection) {
        moneyCalcServerDAO.addSpendingSection(new SpendingSectionAddContainer(spendingSection))
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
                        //                        String errorBodyMessage = getErrorBodyMessage((HttpException) ex);
//                        snackBarAction(getAppContext(), errorBodyMessage);
//                        showErrorMessageFromException(ex, view);
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<SpendingSection>> addSectionRs) {
                        if (addSectionRs.isSuccessful()) {
                            eventBus.addSpendingSectionEvent(ADDED);
//                            view.showAddSuccess();
                        } else {
                            eventBus.addErrorEvent(addSectionRs.getMessage());
                            //                            view.showErrorMessage(addSectionRs.getMessage());
                        }
                    }
                });
    }

    @Override
    public void editSpendingSection(Integer id, SpendingSection spendingSection) {
        moneyCalcServerDAO.updateSpendingSection(new SpendingSectionUpdateContainer(id, spendingSection))
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
                    public void onNext(MoneyCalcRs<List<SpendingSection>> editSectionRs) {
                        if (editSectionRs.isSuccessful()) {
                            eventBus.addSpendingSectionEvent(EDITED);
//                            view.showEditSuccess();
                        } else {
                            eventBus.addErrorEvent(editSectionRs.getMessage());
                            //                            view.showErrorMessage(editSectionRs.getMessage());
                        }
                    }
                });
    }
}
