package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link HomePresenter}.
 */
@Module
public abstract class HomeModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @ActivityScoped
    @Binds
    abstract HomeContract.Presenter homePresenter(HomePresenter presenter);

//    @ActivityScoped
//    @Binds
//    abstract HomeStatsContract.Presenter homeStatsPresenter(HomeStatsPresenter presenter);
}
