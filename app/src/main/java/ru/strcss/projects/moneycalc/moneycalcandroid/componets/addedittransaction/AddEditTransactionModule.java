package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link AddEditTransactionPresenter}.
 */
@Module
public abstract class AddEditTransactionModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddEditTransactionFragment addEditTransactionFragment();

    @ActivityScoped
    @Binds
    abstract AddEditTransactionContract.Presenter addEditTransactionPresenter(AddEditTransactionPresenter presenter);
}
