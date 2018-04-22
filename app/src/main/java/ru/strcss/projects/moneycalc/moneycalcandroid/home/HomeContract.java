package ru.strcss.projects.moneycalc.moneycalcandroid.home;

import java.util.List;

import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainer;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the loginPresenter.
 */
public interface HomeContract {
    interface View extends BaseView<Presenter> {

        void showErrorMessage(String msg);

        void setDatesRange(String from, String to);

        void setSections(List<SpendingSection> sections);
    }

    interface Presenter extends BasePresenter<View> {
        void requestFinanceSummary(FinanceSummaryGetContainer financeSummaryGetContainer);

        void requestSettings();

        void updateHomeScreen();
    }
}
