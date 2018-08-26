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

import static ru.strcss.projects.moneycalc.dto.crudcontainers.SpendingSectionSearchType.BY_ID;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

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
                        String errorBodyMessage = getErrorBodyMessage((HttpException) e);
                        System.out.println("requestSpendingSections onError!!!!! " + errorBodyMessage);
                        view.showErrorMessage(errorBodyMessage);
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
    public void deleteSpendingSection(String id) {
        moneyCalcServerDAO.deleteSpendingSection(new SpendingSectionDeleteContainer(id, BY_ID))
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
                    public void onNext(MoneyCalcRs<List<SpendingSection>> sectionRs) {
                        if (sectionRs.isSuccessful()) {
                            System.out.println("deleteSpendingSection! sectionRs.getPayload() = " + sectionRs.getPayload());
                            view.showDeleteSuccess();
                        } else {
                            view.showErrorMessage(sectionRs.toString());
                        }
                    }
                });
    }
}
