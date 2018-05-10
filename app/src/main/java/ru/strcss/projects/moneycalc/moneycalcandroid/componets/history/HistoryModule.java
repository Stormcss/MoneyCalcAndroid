package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link HistoryPresenter}.
 */
@Module
public abstract class HistoryModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ActivityScoped
    @Binds
    abstract HistoryContract.Presenter historyPresenter(HistoryPresenter presenter);
}
