package ru.strcss.projects.moneycalc.moneycalcandroid.transactions.addtransaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils;

public class AddEditTransactionActivity extends DaggerAppCompatActivity {

    public static final int REQUEST_ADD_TRANSACTION = 1;

    @Inject
    AddEditTransactionPresenter presenter;

    @Inject
    Lazy<AddEditTransactionFragment> addEditTransactFragmentProvider;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addedittransaction_activity);
        mActionBar = getSupportActionBar();
        //        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        mActionBar = getSupportActionBar();
//        mActionBar.setDisplayHomeAsUpEnabled(true);
//        mActionBar.setDisplayShowHomeEnabled(true);

        // TODO: 01.05.2018 insert transactionId here
//        setToolbarTitle(null);

        AddEditTransactionFragment homeFragment =
                (AddEditTransactionFragment) getSupportFragmentManager().findFragmentById(R.id.addEditTransaction_contentFrame);
        if (homeFragment == null) {
            // Get the fragment from dagger
            homeFragment = addEditTransactFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, R.id.addEditTransaction_contentFrame);
        }
    }


    private void setToolbarTitle(@Nullable String transactionId) {
        if (transactionId == null) {
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
