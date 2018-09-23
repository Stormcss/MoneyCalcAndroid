package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.showProgress;

public class HistoryFragment extends DaggerFragment implements HistoryContract.View {

    @Inject
    HistoryContract.Presenter presenter;

    @Inject
    public HistoryFragment() {
    }

    // UI references
    private FloatingActionButton fabAddTransaction;
    private RecyclerView rvTransactions;
    private HistoryAdapter adapter;
    private List<TransactionLegacy> transactionList;
    private ProgressBar progressView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.history_frag, container, false);

        rvTransactions = root.findViewById(R.id.rv_history);
        progressView = root.findViewById(R.id.history_progress);

        transactionList = new ArrayList<>();
        adapter = new HistoryAdapter(getContext(), presenter, transactionList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        rvTransactions.setLayoutManager(mLayoutManager);
        rvTransactions.setItemAnimator(new DefaultItemAnimator());
        rvTransactions.setAdapter(adapter);


        presenter.requestTransactions();

        fabAddTransaction = root.findViewById(R.id.fab_history_addtransaction);
        fabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEditTransactionActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        rvTransactions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fabAddTransaction.hide();
                else if (dy < 0)
                    fabAddTransaction.show();
            }
        });

        return root;
    }

    @Override
    public void showTransactions(List<TransactionLegacy> transactions) {
        adapter.updateList(transactions);
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showDeleteSuccess() {
        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.transaction_delete_success, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSpinner() {

    }

    @Override
    public void hideSpinner() {
        int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        showProgress(false, null, progressView, animTime);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
//        presenter.requestTransactions();
    }

    @Override
    public void onDestroy() {
        presenter.dropView();
        super.onDestroy();
    }
}
