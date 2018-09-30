package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.SPENDING_SECTION;

public class AddEditSpendingSectionActivity extends DaggerAppCompatActivity {

    @Inject
    AddEditSpendingSectionPresenter presenter;

    @Inject
    Lazy<AddEditSpendingSectionFragment> addEditSpendingSectionFragmentProvider;

    private ActionBar mActionBar;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeditspendingsection_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        // TODO: 01.05.2018 insert transactionId here
//        getIntent().getAction()
        SpendingSection editedSpendingSection = null;
        if (getIntent().getExtras() != null) {
            editedSpendingSection = (SpendingSection) getIntent().getExtras().getSerializable(SPENDING_SECTION);
        }
        setToolbarTitle(editedSpendingSection);

        AddEditSpendingSectionFragment addEditSpendingSectionFragment =
                (AddEditSpendingSectionFragment) getSupportFragmentManager().findFragmentById(R.id.addEditSpendingSection_contentFrame);
        if (addEditSpendingSectionFragment == null) {
            // Get the fragment from dagger
            addEditSpendingSectionFragment = addEditSpendingSectionFragmentProvider.get();

            Bundle bundle = new Bundle();
            bundle.putSerializable(SPENDING_SECTION, editedSpendingSection);
            addEditSpendingSectionFragment.setArguments(bundle);

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), addEditSpendingSectionFragment, R.id.addEditSpendingSection_contentFrame);
        }
    }

    private void setToolbarTitle(@Nullable SpendingSection spendingSection) {
        if (spendingSection == null) {
            mActionBar.setTitle(R.string.spending_section_add);
        } else {
            mActionBar.setTitle(R.string.spending_section_edit);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
