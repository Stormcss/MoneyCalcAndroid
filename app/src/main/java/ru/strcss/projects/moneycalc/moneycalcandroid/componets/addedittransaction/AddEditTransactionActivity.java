package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTION;

public class AddEditTransactionActivity extends DaggerAppCompatActivity {

    @Inject
    AddEditTransactionPresenter presenter;

    @Inject
    Lazy<AddEditTransactionFragment> addEditTransactFragmentProvider;

    private ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addedittransaction_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        // TODO: 01.05.2018 insert transactionId here
//        getIntent().getAction()
        TransactionLegacy editedTransaction = null;
        if (getIntent().getExtras() != null) {
            editedTransaction = (TransactionLegacy) getIntent().getExtras().getSerializable(TRANSACTION);
        }
        setToolbarTitle(editedTransaction);

        AddEditTransactionFragment addEditTransactionFragment =
                (AddEditTransactionFragment) getSupportFragmentManager().findFragmentById(R.id.addEditTransaction_contentFrame);
        if (addEditTransactionFragment == null) {
            // Get the fragment from dagger
            addEditTransactionFragment = addEditTransactFragmentProvider.get();

            Bundle bundle = new Bundle();
            bundle.putSerializable(TRANSACTION, editedTransaction);
            addEditTransactionFragment.setArguments(bundle);

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), addEditTransactionFragment, R.id.addEditTransaction_contentFrame);
        }
    }

    private void setToolbarTitle(@Nullable TransactionLegacy transaction) {
        if (transaction == null) {
            mActionBar.setTitle(R.string.transaction_add);
        } else {
            mActionBar.setTitle(R.string.transaction_edit);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
