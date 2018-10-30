package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter.HistoryFilterActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;

import static android.view.View.INVISIBLE;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.showProgress;

public class HistoryFragment extends DaggerFragment implements HistoryContract.View {

    @Inject
    HistoryContract.Presenter presenter;

    @Inject
    public HistoryFragment() {
    }

    @Inject
    DataStorage dataStorage;

    // UI references
    private HistoryFab historyFab;
    private View sheetView;
    private RecyclerView rvTransactions;
    private HistoryAdapter adapter;
    private ProgressBar progressView;
    private MaterialSheetFab materialSheetFab;
    private RelativeLayout filterWindow;
    private TextView filterWindowCancel;

    private int statusBarColor;
    private List<TransactionLegacy> transactionList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.history_frag, container, false);

        filterWindow = root.findViewById(R.id.history_filter_window);
        filterWindowCancel = root.findViewById(R.id.history_filter_window_cancel);

        rvTransactions = root.findViewById(R.id.rv_history);
        progressView = root.findViewById(R.id.history_progress);
        sheetView = root.findViewById(R.id.history_fab_sheet);

        transactionList = new ArrayList<>();
        adapter = new HistoryAdapter(getContext(), presenter, transactionList, dataStorage);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        rvTransactions.setLayoutManager(mLayoutManager);
        rvTransactions.setItemAnimator(new DefaultItemAnimator());
        rvTransactions.setAdapter(adapter);

        presenter.requestTransactions();

        setupFab(root);

        rvTransactions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && sheetView.getVisibility() == INVISIBLE)
                    historyFab.hide();
                else if (dy < 0 && sheetView.getVisibility() == INVISIBLE)
                    historyFab.show();
            }
        });

        filterWindowCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestTransactions();
                hideFilterWindow();
                dataStorage.setTransactionsFilter(null);
            }
        });

        return root;
    }


    /**
     * Sets up the Floating action button.
     */
    private void setupFab(View root) {
        historyFab = root.findViewById(R.id.fab_history_addtransaction);

        sheetView = root.findViewById(R.id.history_fab_sheet);
        View overlay = root.findViewById(R.id.history_overlay);
//        int sheetColor = getResources().getColor(R.color.fab_sheet_color);
        int sheetColor = getResources().getColor(R.color.colorPrimary);
//        int fabColor = getResources().getColor(R.color.fab_color);
        int fabColor = getResources().getColor(R.color.colorPrimaryBright);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(historyFab, sheetView, overlay,
                sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        root.findViewById(R.id.history_fab_sheet_item_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryFilterActivity.class);
                startActivityForResult(intent, 0);
                materialSheetFab.hideSheet();
            }
        });
        root.findViewById(R.id.history_fab_sheet_item_add_transaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEditTransactionActivity.class);
                startActivityForResult(intent, 0);
                materialSheetFab.hideSheet();
            }
        });
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
    public void showFilterWindow() {
        filterWindow.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFilterWindow() {
        filterWindow.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        presenter.dropView();
        super.onDestroy();
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getActivity().getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(color);
        }
    }
}
