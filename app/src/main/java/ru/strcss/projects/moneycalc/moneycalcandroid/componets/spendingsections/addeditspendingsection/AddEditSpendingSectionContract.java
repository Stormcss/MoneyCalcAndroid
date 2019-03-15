package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;

/**
 * This specifies the contract between the view and the homePresenter.
 */
public interface AddEditSpendingSectionContract {
    interface View extends BaseView<Presenter> {

        void showAddSuccess();

        void showEditSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void addSpendingSection(SpendingSection spendingSection);

        void editSpendingSection(Integer id, SpendingSection spendingSection);
    }
}
