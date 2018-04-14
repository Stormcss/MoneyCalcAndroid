package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link LoginPresenter}.
 */
@Module
public abstract class LoginModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract LoginFragment loginFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RegisterFragment registerFragment();

    @ActivityScoped
    @Binds
    abstract LoginContract.Presenter loginPresenter(LoginPresenter presenter);

    @ActivityScoped
    @Binds
    abstract RegisterContract.Presenter registerPresenter(RegisterPresenter presenter);
}
