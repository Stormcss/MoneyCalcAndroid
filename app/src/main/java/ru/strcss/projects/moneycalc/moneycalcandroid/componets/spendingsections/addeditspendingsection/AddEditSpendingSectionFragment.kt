package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import dagger.android.support.DaggerFragment
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.SPENDING_SECTION
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsContract
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.hideSoftKeyboard
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class AddEditSpendingSectionFragment @Inject
constructor() : DaggerFragment(), AddEditSpendingSectionContract.View, SpendingSectionLogoRecyclerViewAdapter.ItemClickListener {

    @Inject
    lateinit var presenter: AddEditSpendingSectionContract.Presenter

    @Inject
    lateinit var spendingSectionsPresenter: SpendingSectionsContract.Presenter

    // UI references
    private var etSpendingSectionBudget: EditText? = null
    private var etSpendingSectionName: EditText? = null
    private var rvSectionLogo: RecyclerView? = null

    private var selectedLogoId: Int = 0
    private var sectionLogoAdapter: SpendingSectionLogoRecyclerViewAdapter? = null
    private val selectedRecyclerViewItem = AtomicInteger(-1)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = this.arguments

        val updatedSectionData = bundle!!.get(SPENDING_SECTION) as? SpendingSection
        val fabAddSpendingSection = activity!!.findViewById<FloatingActionButton>(R.id.fab_addEditSpendingSection_done)

        val isEditingSpendingSection = updatedSectionData != null

        activity?.let {
            sectionLogoAdapter = SpendingSectionLogoRecyclerViewAdapter(it, selectedRecyclerViewItem)
            sectionLogoAdapter!!.setClickListener(this)
            rvSectionLogo!!.adapter = sectionLogoAdapter
            rvSectionLogo!!.layoutManager = GridLayoutManager(it, 6)
        }

        if (isEditingSpendingSection) {
            updatedSectionData?.let {
                setUpdatedSpendingSectionData(it)
                fabAddSpendingSection.setImageResource(R.drawable.ic_edit_white_24dp)
                if (it.logoId != null) {
                    val position = it.logoId!!
                    //                int position = sectionLogoAdapter.getPositionByLogoId(updatedSectionData.getLogoId());
                    this.onItemClick(null, position)
                }
            }
        }

        fabAddSpendingSection.setOnClickListener {
            val name = etSpendingSectionName!!.text.toString()
            val budget = etSpendingSectionBudget!!.text.toString()
            val logoId = selectedRecyclerViewItem.get()

            if (!name.isEmpty() && !budget.isEmpty()) {
                val spendingSection = SpendingSection()
                spendingSection.name = name
                spendingSection.budget = java.lang.Long.parseLong(budget)
                spendingSection.logoId = logoId
                spendingSection.isAdded = true
                if (isEditingSpendingSection) {
                    presenter.editSpendingSection(updatedSectionData?.sectionId, spendingSection)
                } else {
                    presenter.addSpendingSection(spendingSection)
                }
                hideSoftKeyboard(activity!!)
                activity!!.finish()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.addeditspendingsection_frag, container, false)

        etSpendingSectionName = root.findViewById(R.id.ae_spening_section_name)
        etSpendingSectionBudget = root.findViewById(R.id.ae_spening_section_budget)
        rvSectionLogo = root.findViewById(R.id.ae_spendingsection_section_logo_rw)

        return root
    }

    private fun setUpdatedSpendingSectionData(selectedSection: SpendingSection) {
        etSpendingSectionName!!.setText(selectedSection.name.toString())
        etSpendingSectionBudget!!.setText(selectedSection.budget.toString())
    }

    override fun showErrorMessage(msg: String) {
        println("showErrorMessage! $msg")
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun showAddSuccess() {
        spendingSectionsPresenter.requestSpendingSections()

        val context = activity!!.applicationContext
        SnackbarWrapper.make(context, getContext()!!.getText(R.string.transaction_added), 3000).show()
    }

    override fun showEditSuccess() {
        spendingSectionsPresenter.requestSpendingSections()

        val context = activity!!.applicationContext
        SnackbarWrapper.make(context, getContext()!!.getText(R.string.spending_section_edit_success), 3000).show()
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    override fun onItemClick(view: View?, position: Int) { //todo why view is here?
        selectedLogoId = sectionLogoAdapter!!.getItem(position)
        println("item! = $selectedLogoId")
        selectedRecyclerViewItem.set(position)
        sectionLogoAdapter!!.notifyItemChanged(position)
    }
}
