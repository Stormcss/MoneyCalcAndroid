package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.bysectionsum.StatisticsBySectionSumPresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.bysectionsum.StatisticsSumBySectionContract;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.bysectionsum.StatsSumBySectionFragment;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.FragmentScoped;

/**
 * Created by Stormcss
 * Date: 28.05.2019
 */
@Module
public abstract class StatisticsModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract StatsSumBySectionFragment statsBySectionSumFragment();

    @ActivityScoped
    @Binds
    abstract StatisticsSumBySectionContract.Presenter statsSumBySectionPresenter(StatisticsBySectionSumPresenter presenter);
}
