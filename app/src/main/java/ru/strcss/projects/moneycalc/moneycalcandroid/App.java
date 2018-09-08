package ru.strcss.projects.moneycalc.moneycalcandroid;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.DaggerAppComponent;

public class App extends DaggerApplication {

    // TODO: 07.09.2018 remove me?
    @Inject
    MoneyCalcServerDAO moneyCalcServerDAO;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
