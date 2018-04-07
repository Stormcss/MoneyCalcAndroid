package ru.strcss.projects.moneycalc.moneycalcandroid.di;

import dagger.Module;
import dagger.Provides;
import ru.strcss.projects.moneycalc.moneycalcandroid.AppExecutors;
import ru.strcss.projects.moneycalc.moneycalcandroid.handlers.ServerHandler;

@Module
public class AppModule {
    @Provides
    public AppExecutors provideAppExecutors() {
        return new AppExecutors();
    }

    @Provides
    public ServerHandler provideServerHandler() {
        return new ServerHandler();
    }
}
