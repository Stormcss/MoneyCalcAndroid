package ru.strcss.projects.moneycalc.moneycalcandroid;

import android.app.Application;
import android.content.Context;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.DaggerAppComponent;

public class App extends DaggerApplication {

    private static Application instance;

    //    MoneyCalcServerDAO moneyCalcServerDAO;
    //    @Inject
    // TODO: 07.09.2018 remove me?


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
