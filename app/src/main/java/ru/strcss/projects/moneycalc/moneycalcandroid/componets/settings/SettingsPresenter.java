package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.Event.SETTING_UPDATED;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

@Singleton
public class SettingsPresenter implements SettingsContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    private final EventBus eventBus;

    @Inject
    SettingsPresenter(MoneyCalcServerDAO moneyCalcServerDAO, EventBus eventBus) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
        this.eventBus = eventBus;
    }

    @Nullable
    private SettingsContract.View view;

    @Override
    public void takeView(SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }

    @Override
    public void updateSettings(SettingsLegacy settings) {
        moneyCalcServerDAO.updateSettings(settings)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoneyCalcRs<SettingsLegacy>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                        //                        String errorBodyMessage = getErrorBodyMessage((HttpException) ex);
//                        snackBarAction(view.getContext(), errorBodyMessage);
                    }

                    @Override
                    public void onNext(MoneyCalcRs<SettingsLegacy> updateRs) {
                        if (updateRs.isSuccessful()) {
                            eventBus.addEvent(SETTING_UPDATED);
                            view.showUpdateSuccess();
                        } else {
                            eventBus.addErrorEvent(updateRs.getMessage());
                            //                            view.showErrorMessage(updateRs.getMessage());
                        }
                    }
                });
    }
}
