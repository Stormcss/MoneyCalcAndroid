package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

public interface LoginContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {
    }
}
