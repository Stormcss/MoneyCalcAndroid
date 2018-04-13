package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import android.support.annotation.Nullable;

import javax.inject.Inject;

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
        if (loginView == null)
            System.out.println("loginView is null !!!!!!!!!!!!!!!!!!!");

        moneyCalcServerDAO.login(access)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        loginView.showMainActivity();
                        System.out.println("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError!!!!! " + e.getMessage());
                        loginView.hideSpinner();
                        loginView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(Void s) {
                        System.out.println("onNext" + s);
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
