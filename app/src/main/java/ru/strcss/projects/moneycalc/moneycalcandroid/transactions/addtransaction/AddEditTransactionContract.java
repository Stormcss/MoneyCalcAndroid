package ru.strcss.projects.moneycalc.moneycalcandroid.transactions.addtransaction;

import java.util.List;

import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.Transaction;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the homePresenter.
 */
public interface AddEditTransactionContract {
    interface View extends BaseView<Presenter> {

        void showErrorMessage(String msg);

        void showAddSuccess();

        void showEditSuccess();

        void showSpendingSections(List<SpendingSection> spendingSections);
    }

    interface Presenter extends BasePresenter<View> {
        void addTransaction(Transaction transaction);

        void editTransaction(String id, Transaction transaction);

        void requestSpendingSectionsList();

    }
}
