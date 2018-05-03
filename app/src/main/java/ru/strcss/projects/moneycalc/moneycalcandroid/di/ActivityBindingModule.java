package ru.strcss.projects.moneycalc.moneycalcandroid.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.home.HomeActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.home.HomeModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.login.LoginActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.login.LoginModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.spendingsections.SpendingSectionsActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.spendingsections.SpendingSectionsModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.transactions.addtransaction.AddEditTransactionActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.transactions.addtransaction.AddEditTransactionModule;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SpendingSectionsModule.class)
    abstract SpendingSectionsActivity spendingSectionsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddEditTransactionModule.class)
    abstract AddEditTransactionActivity addEditTransactionActivity();
}
