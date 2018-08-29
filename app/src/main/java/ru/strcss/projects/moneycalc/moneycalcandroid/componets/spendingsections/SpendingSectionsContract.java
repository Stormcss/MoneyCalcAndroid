package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

import java.util.List;

import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the loginPresenter.
 */
public interface SpendingSectionsContract {
    interface View extends BaseView<Presenter> {

        void showSpendingSections(List<SpendingSection> spendingSections);

        void showSpinner();

        void hideSpinner();

        void showUpdateSuccess();

        void showDeleteSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void requestSpendingSections();

        void deleteSpendingSection(String id);
    }
}
