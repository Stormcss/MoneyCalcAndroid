package ru.strcss.projects.moneycalc.moneycalcandroid.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.HistoryActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.HistoryModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter.HistoryFilterActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter.HistoryFilterModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsModule;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection.AddEditSpendingSectionActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection.AddEditSpendingSectionModule;

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

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddEditSpendingSectionModule.class)
    abstract AddEditSpendingSectionActivity addEditSpendingSectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = HistoryModule.class)
    abstract HistoryActivity addHistoryActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SettingsModule.class)
    abstract SettingsActivity addSettingsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = HistoryFilterModule.class)
    abstract HistoryFilterActivity addHistoryFilterActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules =  ApplicationSettingsModule.class)
    abstract ApplicationSettingsActivity applicationSettingsActivity();
}
