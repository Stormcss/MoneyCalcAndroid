package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import retrofit2.Response;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage;

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
                .flatMap(new Func1<MoneyCalcRs<Void>, Observable<Response<Void>>>() {
                    @Override
                    public Observable<Response<Void>> call(MoneyCalcRs<Void> loginRs) {
                        System.out.println("loginRs = " + loginRs);
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
                    public void onError(Throwable ex) {
                        registerView.hideSpinner();
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex));
                        //                        if (ex instanceof HttpException)
//                            registerView.showErrorMessage(getErrorBodyMessage((HttpException) ex));
//                        else
//                            registerView.showErrorMessage(ex.getMessage());
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
