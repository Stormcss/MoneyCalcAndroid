package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTION
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy
import javax.inject.Inject

class AddEditTransactionActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var presenter: AddEditTransactionPresenter

    @Inject
    lateinit var addEditTransactFragmentProvider: Lazy<AddEditTransactionFragment>

    private var mActionBar: ActionBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addedittransaction_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mActionBar = supportActionBar
        mActionBar!!.setDisplayHomeAsUpEnabled(true)
        mActionBar!!.setDisplayShowHomeEnabled(true)

        var editedTransaction: TransactionLegacy? = null
        if (intent.extras != null) {
            editedTransaction = intent.extras!!.getSerializable(TRANSACTION) as TransactionLegacy
        }
        setToolbarTitle(editedTransaction)

        var addEditTransactionFragment: AddEditTransactionFragment? =
                supportFragmentManager.findFragmentById(R.id.addEditTransaction_contentFrame) as? AddEditTransactionFragment
        if (addEditTransactionFragment == null) {
            // Get the fragment from dagger
            addEditTransactionFragment = addEditTransactFragmentProvider.get()

            val bundle = Bundle()
            bundle.putSerializable(TRANSACTION, editedTransaction)
            addEditTransactionFragment!!.arguments = bundle

            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, addEditTransactionFragment, R.id.addEditTransaction_contentFrame)
        }
    }

    private fun setToolbarTitle(transaction: TransactionLegacy?) {
        if (transaction == null) {
            mActionBar!!.setTitle(R.string.transaction_add)
        } else {
            mActionBar!!.setTitle(R.string.transaction_edit)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
