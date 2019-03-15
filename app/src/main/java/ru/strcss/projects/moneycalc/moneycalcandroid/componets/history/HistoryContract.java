package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import java.util.List;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy;

/**
 * This specifies the contract between the view and the loginPresenter.
 */
public interface HistoryContract {
    interface View extends BaseView<Presenter> {

        void showTransactions(List<TransactionLegacy> transactions);

        void showDeleteSuccess();

        void showSpinner();

        void hideSpinner();

        void showFilterWindow();

        void hideFilterWindow();
    }

    interface Presenter extends BasePresenter<View> {
        void requestTransactions();

        void deleteTransaction(Integer id);
    }
}
