package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsContract;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsPresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link AddEditSpendingSectionPresenter}.
 */
@Module
public abstract class AddEditSpendingSectionModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddEditSpendingSectionFragment addEditSpendingSectionFragment();

    @ActivityScoped
    @Binds
    abstract AddEditSpendingSectionContract.Presenter addEditSpendingSectionPresenter(AddEditSpendingSectionPresenter presenter);

    @ActivityScoped
    @Binds
    abstract SpendingSectionsContract.Presenter spendingSectionsPresenter(SpendingSectionsPresenter presenter);
}
