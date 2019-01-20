package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import retrofit2.Response;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.Access;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

public class LoginPresenter implements LoginContract.Presenter {

    private final MoneyCalcServerDAO moneyCalcServerDAO;

    @Inject
    DataStorage dataStorage;

    @Nullable
    private LoginContract.View loginView;

    @Inject
    LoginPresenter(MoneyCalcServerDAO moneyCalcServerDAO) {
        this.moneyCalcServerDAO = moneyCalcServerDAO;
    }

    @Override
    public void attemptLogin(final Access access) {
        moneyCalcServerDAO.login(access)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("completed");
                    }

                    @Override
                    public void onError(Throwable ex) {
                        loginView.hideSpinner();
                        ex.printStackTrace();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                    }

                    @Override
                    public void onNext(Response<Void> rs) {
                        String token = rs.headers().get("Authorization");
                        if (token != null) {
                            moneyCalcServerDAO.setToken(token);
                            dataStorage.getActiveUserData().setUserLogin(access.getLogin());
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
