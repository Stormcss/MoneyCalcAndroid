package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        moneyCalcServerDAO.getSpendingSections(moneyCalcServerDAO.getToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AjaxRs<List<SpendingSection>>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("requestSpendingSections completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("requestSpendingSections onError!!!!! " + e.getMessage());
                        view.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AjaxRs<List<SpendingSection>> rs) {
                        System.out.println("requestSpendingSections. rs = " + rs);
                        if (rs.isSuccessful()) {
                            view.showSpendingSections(rs.getPayload());
                        } else {
                            view.showErrorMessage(rs.getMessage());
                        }
                        view.hideSpinner();
                    }
                });
    }
}
