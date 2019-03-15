package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction;

import java.util.List;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy;

/**
 * This specifies the contract between the view and the homePresenter.
 */
public interface AddEditTransactionContract {
    interface View extends BaseView<Presenter> {

        void showAddSuccess();

        void showEditSuccess();

        void showSpendingSections(List<SpendingSection> spendingSections);
    }

    interface Presenter extends BasePresenter<View> {
        void addTransaction(TransactionLegacy transaction);

        void editTransaction(Integer id, TransactionLegacy transaction);

        void requestSpendingSectionsList();

    }
}
