package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import retrofit2.Response;
import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
    public void attemptRegister(final Credentials credentials) {
        moneyCalcServerDAO.registerPerson(credentials)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<AjaxRs<Void>, Observable<Response<Void>>>() {
                    @Override
                    public Observable<Response<Void>> call(AjaxRs<Void> loginRs) {
                        if (loginRs.isSuccessful()) {
                            return moneyCalcServerDAO.login(credentials.getAccess());
                        } else {
                            throw new RuntimeException(loginRs.getMessage());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        registerView.hideSpinner();
                        registerView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Void> rs) {
                        if (rs.isSuccessful()) {
                            String token = rs.headers().get("Authorization");
                            moneyCalcServerDAO.setToken(token);
                            System.out.println("token = " + token);
                            registerView.showMainActivity();
                        } else {
                            System.out.println("rs = " + rs);
                            registerView.showErrorMessage(rs.message());
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