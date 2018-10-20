package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter;

import dagger.Binds;
import dagger.Module;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link HistoryFilterPresenter}.
 */
@Module
public abstract class HistoryFilterModule {
//    @FragmentScoped
//    @ContributesAndroidInjector
//    abstract HistoryFragment historyFragment();

    @ActivityScoped
    @Binds
    abstract HistoryFilterContract.Presenter historyFilterPresenter(HistoryFilterPresenter presenter);
}
