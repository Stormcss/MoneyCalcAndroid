package ru.strcss.projects.moneycalc.moneycalcandroid.di;

import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.strcss.projects.moneycalc.moneycalcandroid.App;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;

@Module
public class AppModule {
    @Provides
    @Singleton
    public MoneyCalcServerDAO provideMoneyCalcServerDAO() {
        return new MoneyCalcServerDAO(PreferenceManager.getDefaultSharedPreferences(App.getAppContext()));
    }

    @Provides
    @Singleton
    public DataStorage provideDataStorage() {
        return new DataStorage(PreferenceManager.getDefaultSharedPreferences(App.getAppContext()));
    }

    @Provides
    @Singleton
    public EventBus provideMyBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public DrawableStorage provideDrawableStorage() {
        return new DrawableStorage();
    }

}
