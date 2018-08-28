package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import retrofit2.Response;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    @Nullable
    private LoginContract.View loginView;

    @Inject
    LoginPresenter(MoneyCalcServerDAO moneyCalcServerDAO) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
    }

    @Override
    public void attemptLogin(Access access) {
        moneyCalcServerDAO.login(access)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError!!!!! " + e.getMessage());
                        loginView.hideSpinner();
                        loginView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Void> rs) {
                        String token = rs.headers().get("Authorization");
                        if (token != null) {
                            moneyCalcServerDAO.setToken(token);
                            System.out.println("token = " + moneyCalcServerDAO.getToken());
                            loginView.showMainActivity();
                        } else {
                            loginView.showErrorMessage("Bad credentials");
                        }
                        loginView.hideSpinner();
                    }
                });
    }

    @Override
    public void takeView(LoginContract.View view) {
        loginView = view;
    }

    @Override
    public void dropView() {
        loginView = null;
    }
}
