package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate.StatisticsSumByDateContract;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate.StatisticsSumByDatePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate.StatsSumByDateFragment;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydatesection.StatisticsSumByDateSectionContract;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydatesection.StatisticsSumByDateSectionPresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydatesection.StatsSumByDateSectionFragment;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbysection.StatisticsSumBySectionContract;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbysection.StatisticsSumBySectionPresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbysection.StatsSumBySectionFragment;
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

    @FragmentScoped
    @ContributesAndroidInjector
    abstract StatsSumByDateFragment statsSumByDateFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract StatsSumByDateSectionFragment statsSumByDateSectionFragment();

    @ActivityScoped
    @Binds
    abstract StatisticsContract.Presenter statisticsPresenter(StatisticsPresenter presenter);

    @ActivityScoped
    @Binds
    abstract StatisticsSumBySectionContract.Presenter statsSumBySectionPresenter(StatisticsSumBySectionPresenter presenter);

    @ActivityScoped
    @Binds
    abstract StatisticsSumByDateContract.Presenter statsSumByDatePresenter(StatisticsSumByDatePresenter presenter);

    @ActivityScoped
    @Binds
    abstract StatisticsSumByDateSectionContract.Presenter statsSumByDateSectionPresenter(StatisticsSumByDateSectionPresenter presenter);
}
