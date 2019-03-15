package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.ADDED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.EDITED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

@Singleton
public class SpendingSectionsPresenter implements SpendingSectionsContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;
    private final DataStorage dataStorage;
    private final EventBus eventBus;

    @Nullable
    private SpendingSectionsContract.View view;

    @Inject
    SpendingSectionsPresenter(MoneyCalcServerDAO moneyCalcServerDAO, DataStorage dataStorage, EventBus eventBus) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.dataStorage = dataStorage;
        this.eventBus = eventBus;

        eventBus.subscribeSpendingSectionEvent()
                .subscribe(new Action1<CrudEvent>() {
                    @Override
                    public void call(CrudEvent sectionEvent) {
                        requestSpendingSections();
                        if (sectionEvent.equals(ADDED)) {
                            snackBarAction(R.string.spending_section_added);
                        } else if (sectionEvent.equals(EDITED)) {
                            snackBarAction(R.string.spending_section_edit_success);
                        }
                    }
                });
    }

    @Override
    public void takeView(SpendingSectionsContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    @Override
    public void requestSpendingSections() {
        moneyCalcServerDAO.getSpendingSections()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<SpendingSection>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                        //                        showErrorMessageFromException(ex, view);
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<SpendingSection>> getSpendingSectionsRs) {
                        System.out.println("getSpendingSectionsRs = " + getSpendingSectionsRs);
                        if (getSpendingSectionsRs.isSuccessful()) {
                            dataStorage.setSpendingSections(getSpendingSectionsRs.getPayload());
                            view.showSpendingSections(getSpendingSectionsRs.getPayload());
                        } else {
                            view.showErrorMessage(getSpendingSectionsRs.toString());
                        }
                        view.hideSpinner();
                    }
                });
    }

    @Override
    public void deleteSpendingSection(Integer id) {
        moneyCalcServerDAO.deleteSpendingSection(id)
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
                        //                        snackBarAction(getActivity().getApplicationContext(), msg);
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<SpendingSection>> sectionRs) {
                        if (sectionRs.isSuccessful()) {
//                            eventBus.addSpendingSectionEvent(DELETED);
                            view.showDeleteSuccess();
//                            System.out.println("deleteSpendingSection! sectionRs.getPayload() = " + sectionRs.getPayload());
//                            snackBarAction(view.getContext(), sectionRs.toString());
                        } else {
                            view.showErrorMessage(sectionRs.toString());
//                            snackBarAction(view.getContext(), sectionRs.toString());
                        }
                    }
                });
    }
}
