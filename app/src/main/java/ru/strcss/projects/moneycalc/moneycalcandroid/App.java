package ru.strcss.projects.moneycalc.moneycalcandroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsPreferenceKey;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.DaggerAppComponent;

public class App extends DaggerApplication {

    private static Application instance;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        setupDefaultSharedPreferences();
    }

    private void setupDefaultSharedPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        for (ApplicationSettingsPreferenceKey key : ApplicationSettingsPreferenceKey.values()) {
            if (preferences.getString(key.name(), null) == null && key.getDefaultValue() != null)
                preferences.edit().putString(key.name(), key.getDefaultValue()).apply();
        }
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
