package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    @Nullable
    private RegisterContract.View registerView;

    @Inject
    RegisterPresenter(MoneyCalcServerDAO moneyCalcServerDAO) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
    }

    @Override
    public void attemptRegister(Credentials credentials) {
        moneyCalcServerDAO.registerPerson(credentials)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AjaxRs<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        registerView.hideSpinner();
                        registerView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AjaxRs<Void> rs) {
                        if (rs.isSuccessful()) {
//                            String token = rs.headers().get("Authorization");
//                            moneyCalcServerDAO.setToken(token);
//                            System.out.println("token = " + token);
                            registerView.showMainActivity();
                        } else {
                            registerView.showErrorMessage(rs.getMessage());
                        }
                        registerView.hideSpinner();
                    }
                });
    }

    @Override
    public void takeView(RegisterContract.View view) {
        registerView = view;
    }

    @Override
    public void dropView() {
        registerView = null;
    }
}
