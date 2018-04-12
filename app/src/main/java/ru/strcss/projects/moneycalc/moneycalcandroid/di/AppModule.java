package ru.strcss.projects.moneycalc.moneycalcandroid.di;

import dagger.Module;
import dagger.Provides;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;

@Module
public class AppModule {
//    @Provides
//    public AppExecutors provideAppExecutors() {
//        return new AppExecutors();
//    }

    @Provides
    public MoneyCalcServerDAO provideMoneyCalcServerDAO() {
        return new MoneyCalcServerDAO();
    }
}
