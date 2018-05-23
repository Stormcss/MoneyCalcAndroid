package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import java.util.List;

import ru.strcss.projects.moneycalc.enitities.Transaction;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the loginPresenter.
 */
public interface HistoryContract {
    interface View extends BaseView<Presenter> {

        void showTransactions(List<Transaction> transactions);

        void showErrorMessage(String msg);

        void showDeleteSuccess();

        void showSpinner();

        void hideSpinner();
    }

    interface Presenter extends BasePresenter<View> {
        void requestTransactions();

        void deleteTransaction(String id);
    }
}
