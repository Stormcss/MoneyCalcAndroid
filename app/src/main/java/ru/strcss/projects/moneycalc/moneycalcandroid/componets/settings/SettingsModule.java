package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link SettingsPresenter}.
 */
@Module
public abstract class SettingsModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();

    @ActivityScoped
    @Binds
    abstract SettingsContract.Presenter settingsPresenter(SettingsPresenter presenter);
}
