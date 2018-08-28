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
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.dto.crudcontainers.SpendingSectionSearchType.BY_ID;

@Singleton
public class AddEditSpendingSectionPresenter implements AddEditSpendingSectionContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    @Nullable
    private AddEditSpendingSectionContract.View view;

    @Inject
    AddEditSpendingSectionPresenter(MoneyCalcServerDAO moneyCalcServerDAO) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
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
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(MoneyCalcRs<List<SpendingSection>> addSectionRs) {
                        if (addSectionRs.isSuccessful()) {
                            view.showAddSuccess();
                        } else {
                            view.showErrorMessage(addSectionRs.getMessage());
                        }
                    }
                });
    }

    @Override
    public void editSpendingSection(String id, SpendingSection spendingSection) {
        moneyCalcServerDAO.updateSpendingSection(new SpendingSectionUpdateContainer(id, spendingSection, BY_ID))
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
                            view.showEditSuccess();
                        } else {
                            view.showErrorMessage(sectionRs.getMessage());
                        }
                    }
                });
    }
}
