package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home;

import java.util.List;

import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainer;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the homePresenter.
 */
public interface HomeContract {
    interface View extends BaseView<Presenter> {

        void showErrorMessage(String msg);

        void setDatesRange(String from, String to, List<Integer> spendingSectionsIds);

//        void setSections(List<SpendingSection> sections);

        void setStatisticsSections(List<SpendingSection> spendingSections, List<FinanceSummaryBySection> financeSummary);

        void showAddTransaction();
    }

    interface Presenter extends BasePresenter<View> {
        void requestFinanceSummary(FinanceSummaryGetContainer financeSummaryGetContainer);

        void requestSettings();

        void requestSectionStatistics(String from, String to, List<Integer> sections);

        void addTransaction();

        //Stats
//        void saveStatsFragmentState(int sectionId, android.view.View view);
//
//        void clearStatsFragmentStates();
//
//        void updateStatisticsSection(Integer id);
    }
}
