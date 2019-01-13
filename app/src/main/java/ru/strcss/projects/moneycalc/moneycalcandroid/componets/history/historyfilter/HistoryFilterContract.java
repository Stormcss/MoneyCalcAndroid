package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter;

import java.util.List;

import ru.strcss.projects.moneycalc.moneycalcandroid.BasePresenter;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;

/**
 * This specifies the contract between the view and the loginPresenter.
 */
public interface HistoryFilterContract {
    interface View extends BaseView<Presenter> {
        void showSpendingSections(List<SpendingSection> spendingSections);
    }

    interface Presenter extends BasePresenter<View> {
        void requestFilteredTransactions(TransactionsSearchFilterLegacy filter);

        void requestSpendingSectionsList();
    }
}
