package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter;

import java.util.List;

import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionsSearchContainerLegacy;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

/**
 * This specifies the contract between the view and the loginPresenter.
 */
public interface HistoryFilterContract {
    interface View extends BaseView<Presenter> {
        void showSpendingSections(List<SpendingSection> spendingSections);
    }

    interface Presenter extends BasePresenter<View> {
        void requestFilteredTransactions(TransactionsSearchContainerLegacy filter);

        void requestSpendingSectionsList();
    }
}
