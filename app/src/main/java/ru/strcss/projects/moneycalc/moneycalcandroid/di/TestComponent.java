package ru.strcss.projects.moneycalc.moneycalcandroid.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.strcss.projects.moneycalc.moneycalcandroid.activities.RegisterActivity;

@Singleton
@Component(modules = AppModule.class)
public interface TestComponent {
    void inject(RegisterActivity injector);
}
