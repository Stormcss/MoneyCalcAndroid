package ru.strcss.projects.moneycalc.moneycalcandroid.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;

@Module
public class AppModule {
//    @Provides
//    public AppExecutors provideAppExecutors() {
//        return new AppExecutors();
//    }

    @Provides
    @Singleton
    public MoneyCalcServerDAO provideMoneyCalcServerDAO() {
        return new MoneyCalcServerDAO();
    }

    @Provides
    @Singleton
    public DataStorage provideDataStorage() {
        return new DataStorage();
    }
}
