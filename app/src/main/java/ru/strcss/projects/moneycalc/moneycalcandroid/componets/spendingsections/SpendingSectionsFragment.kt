package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import dagger.android.support.DaggerFragment
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection.AddEditSpendingSectionActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.showProgress
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import java.util.*
import javax.inject.Inject

class SpendingSectionsFragment @Inject
constructor() : DaggerFragment(), SpendingSectionsContract.View {

    @Inject
    lateinit var presenter: SpendingSectionsContract.Presenter

    @Inject
    lateinit var dataStorage: DataStorage

    // UI references
    private var fabAddSpendingSection: FloatingActionButton? = null
    private var rvSpendingSections: RecyclerView? = null
    private var adapter: SpendingSectionsAdapter? = null
    private var sectionList: MutableList<SpendingSection>? = null
    private var progressView: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.spendingsections_frag, container, false)

        rvSpendingSections = root.findViewById(R.id.rv_spendingsections)
        progressView = root.findViewById(R.id.spending_section_progress)

        sectionList = ArrayList()
        context?.let { adapter = SpendingSectionsAdapter(it, sectionList, presenter) }

        val mLayoutManager = GridLayoutManager(context, 1)
        rvSpendingSections!!.layoutManager = mLayoutManager
        //        rvSpendingSections.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvSpendingSections!!.itemAnimator = DefaultItemAnimator()
        rvSpendingSections!!.adapter = adapter

        presenter.requestSpendingSections()

        fabAddSpendingSection = root.findViewById(R.id.fab_spendingsecions_addspendingsection)
        fabAddSpendingSection!!.setOnClickListener {
            val intent = Intent(context, AddEditSpendingSectionActivity::class.java)
            startActivityForResult(intent, 0)
        }
        rvSpendingSections!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0)
                    fabAddSpendingSection!!.hide()
                else if (dy < 0)
                    fabAddSpendingSection!!.show()
            }
        })

        // show available data quickly
        if (dataStorage.spendingSections != null) {
            dataStorage.spendingSections?.let { showSpendingSections(it.items) }
//            showSpendingSections(dataStorage.spendingSections)
//            showSpendingSections(dataStorage.spendingSections ?: null)
        }
        return root
    }

    override fun showSpendingSections(spendingSections: List<SpendingSection>) {
        adapter?.updateList(spendingSections)
    }

    override fun showErrorMessage(msg: String) {
        println("showErrorMessage! $msg")
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun showSpinner() {

    }

    override fun hideSpinner() {
        val animTime = resources.getInteger(android.R.integer.config_mediumAnimTime)
        showProgress(false, null, progressView, animTime)
    }

    override fun showUpdateSuccess() {
        Snackbar.make(activity!!.findViewById(android.R.id.content), R.string.spending_section_edit_success, Snackbar.LENGTH_LONG).show()
        // TODO: 05.06.2018 show mini spinner here
        presenter.requestSpendingSections()
    }

    override fun showDeleteSuccess() {
        Snackbar.make(activity!!.findViewById(android.R.id.content), R.string.spending_section_delete_success, Snackbar.LENGTH_LONG).show()
        // TODO: 05.06.2018 show mini spinner here
        presenter.requestSpendingSections()
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
        //        presenter.requestSpendingSections();
        //        System.out.println("SpendingSectionsFragment onResume");
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }
}
