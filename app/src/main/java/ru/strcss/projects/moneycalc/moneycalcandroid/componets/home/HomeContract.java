package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home;

import java.util.List;

import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainer;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the homePresenter.
 */
public interface HomeContract {
    interface View extends BaseView<Presenter> {

        void showDatesRange(String from, String to);

//        void setSections(List<SpendingSection> sections);

        void showStatisticsSections(List<FinanceSummaryBySection> financeSummary);

        void showAddTransactionActivity();
    }

    interface Presenter extends BasePresenter<View> {
        void requestFinanceSummary(FinanceSummaryGetContainer financeSummaryGetContainer);

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
