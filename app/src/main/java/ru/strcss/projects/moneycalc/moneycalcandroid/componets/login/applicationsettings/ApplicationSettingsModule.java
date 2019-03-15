package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link ApplicationSettingsPresenter}.
 */
@Module
public abstract class ApplicationSettingsModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract ApplicationSettingsFragment applicationSettingsFragment();

    @ActivityScoped
    @Binds
    abstract ApplicationSettingsContract.Presenter settingsPresenter(ApplicationSettingsPresenter presenter);
}
