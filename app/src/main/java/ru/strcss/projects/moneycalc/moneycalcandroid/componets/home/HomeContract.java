package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home;

import java.util.List;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.FinanceSummaryFilter;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.FinanceSummaryBySection;

/**
 * This specifies the contract between the view and the homePresenter.
 */
public interface HomeContract {
    interface View extends BaseView<Presenter> {

        void showDatesRange(String from, String to);

        void showStatisticsSections(List<FinanceSummaryBySection> financeSummary);

        void showAddTransactionActivity();

        void updateStatsAndSettings();
    }

    interface Presenter extends BasePresenter<View> {
        void requestFinanceSummary(FinanceSummaryFilter financeSummaryGetContainer);

        void requestSettings();

        void requestSpendingSections();

        void requestSectionStatistics();

        void requestSectionStatistics(String from, String to, List<Integer> sections);

        void showAddTransactionActivity();

        //Stats
//        void saveStatsFragmentState(int sectionId, android.view.View view);
//
//        void clearStatsFragmentStates();
//
//        void updateStatisticsSection(Integer id);
    }
}
