package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.SPENDING_SECTION
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import javax.inject.Inject

class AddEditSpendingSectionActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var presenter: AddEditSpendingSectionPresenter

    @Inject
    lateinit var addEditSpendingSectionFragmentProvider: Lazy<AddEditSpendingSectionFragment>

    private var mActionBar: ActionBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addeditspendingsection_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mActionBar = supportActionBar
        mActionBar!!.setDisplayHomeAsUpEnabled(true)
        mActionBar!!.setDisplayShowHomeEnabled(true)

        // TODO: 01.05.2018 insert transactionId here
        //        getIntent().getAction()
        var editedSpendingSection: SpendingSection? = null
        if (intent.extras != null) {
            editedSpendingSection = intent.extras!!.getSerializable(SPENDING_SECTION) as SpendingSection
        }
        setToolbarTitle(editedSpendingSection)

        var addEditSpendingSectionFragment: AddEditSpendingSectionFragment? =
                supportFragmentManager.findFragmentById(R.id.addEditSpendingSection_contentFrame) as? AddEditSpendingSectionFragment?
        if (addEditSpendingSectionFragment == null) {
            // Get the fragment from dagger
            addEditSpendingSectionFragment = addEditSpendingSectionFragmentProvider.get()

            val bundle = Bundle()
            bundle.putSerializable(SPENDING_SECTION, editedSpendingSection)
            addEditSpendingSectionFragment!!.arguments = bundle

            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, addEditSpendingSectionFragment, R.id.addEditSpendingSection_contentFrame)
        }
    }

    private fun setToolbarTitle(spendingSection: SpendingSection?) {
        if (spendingSection == null) {
            mActionBar!!.setTitle(R.string.spending_section_add)
        } else {
            mActionBar!!.setTitle(R.string.spending_section_edit)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
