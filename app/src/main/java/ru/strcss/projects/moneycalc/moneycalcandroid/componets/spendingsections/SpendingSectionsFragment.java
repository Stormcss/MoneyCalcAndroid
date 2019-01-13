package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

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
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection.AddEditSpendingSectionActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.showProgress;

public class SpendingSectionsFragment extends DaggerFragment implements SpendingSectionsContract.View {

    @Inject
    SpendingSectionsContract.Presenter presenter;

    @Inject
    DataStorage dataStorage;

    @Inject
    public SpendingSectionsFragment() {
    }

    // UI references
    private FloatingActionButton fabAddSpendingSection;
    private RecyclerView rvSpendingSections;
    private SpendingSectionsAdapter adapter;
    private List<SpendingSection> sectionList;
    private ProgressBar progressView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.spendingsections_frag, container, false);

        rvSpendingSections = root.findViewById(R.id.rv_spendingsections);
        progressView = root.findViewById(R.id.spending_section_progress);

        sectionList = new ArrayList<>();
        adapter = new SpendingSectionsAdapter(getContext(), sectionList, presenter);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        rvSpendingSections.setLayoutManager(mLayoutManager);
//        rvSpendingSections.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvSpendingSections.setItemAnimator(new DefaultItemAnimator());
        rvSpendingSections.setAdapter(adapter);

        presenter.requestSpendingSections();

        fabAddSpendingSection = root.findViewById(R.id.fab_spendingsecions_addspendingsection);
        fabAddSpendingSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEditSpendingSectionActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        rvSpendingSections.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fabAddSpendingSection.hide();
                else if (dy < 0)
                    fabAddSpendingSection.show();
            }
        });

        // show available data quickly
        if (dataStorage.getSpendingSections() != null) {
            showSpendingSections(dataStorage.getSpendingSections());
        }
        return root;
    }

    @Override
    public void showSpendingSections(List<SpendingSection> spendingSections) {
        adapter.updateList(spendingSections);
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
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
    public void showUpdateSuccess() {
        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.spending_section_edit_success, Snackbar.LENGTH_LONG).show();
        // TODO: 05.06.2018 show mini spinner here
        presenter.requestSpendingSections();
    }

    @Override
    public void showDeleteSuccess() {
        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.spending_section_delete_success, Snackbar.LENGTH_LONG).show();
        // TODO: 05.06.2018 show mini spinner here
        presenter.requestSpendingSections();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
//        presenter.requestSpendingSections();
//        System.out.println("SpendingSectionsFragment onResume");
    }

    @Override
    public void onDestroy() {
        presenter.dropView();
        super.onDestroy();
    }
}
