package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link SpendingSectionsPresenter}.
 */
@Module
public abstract class SpendingSectionsModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SpendingSectionsFragment sectionsFragment();

    @ActivityScoped
    @Binds
    abstract SpendingSectionsContract.Presenter spendingSectionsPresenter(SpendingSectionsPresenter presenter);
}
