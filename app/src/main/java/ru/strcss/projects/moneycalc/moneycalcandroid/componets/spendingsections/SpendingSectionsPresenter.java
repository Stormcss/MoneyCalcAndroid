package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.HttpException;
import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionDeleteContainer;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.showErrorMessageFromException;

@Singleton
public class SpendingSectionsPresenter implements SpendingSectionsContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;
    private final DataStorage dataStorage;

    @Nullable
    private SpendingSectionsContract.View view;

    @Inject
    SpendingSectionsPresenter(MoneyCalcServerDAO moneyCalcServerDAO, DataStorage dataStorage) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.dataStorage = dataStorage;
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
                    public void onError(Throwable e) {
                        showErrorMessageFromException(e, view);
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
        moneyCalcServerDAO.deleteSpendingSection(new SpendingSectionDeleteContainer(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<List<SpendingSection>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        String errorBodyMessage = getErrorBodyMessage((HttpException) ex);
                        snackBarAction(view.getContext(), errorBodyMessage);
//                        snackBarAction(getActivity().getApplicationContext(), msg);
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<SpendingSection>> sectionRs) {
                        if (sectionRs.isSuccessful()) {
                            System.out.println("deleteSpendingSection! sectionRs.getPayload() = " + sectionRs.getPayload());
                            view.showDeleteSuccess();
                            snackBarAction(view.getContext(), sectionRs.toString());
                        } else {
                            snackBarAction(view.getContext(), sectionRs.toString());
                            view.showErrorMessage(sectionRs.toString());
                        }
                    }
                });
    }
}
