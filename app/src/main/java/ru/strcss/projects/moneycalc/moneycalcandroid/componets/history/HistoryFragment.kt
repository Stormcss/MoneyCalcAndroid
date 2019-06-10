package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.gordonwong.materialsheetfab.MaterialSheetFab
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener
import dagger.android.support.DaggerFragment
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter.HistoryFilterActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.changeActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.NumberUtils.Companion.formatNumberToPretty
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.showProgress
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchLegacyRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy
import java.util.*
import javax.inject.Inject

class HistoryFragment @Inject
constructor() : DaggerFragment(), HistoryContract.View {

    @Inject
    lateinit var presenter: HistoryContract.Presenter

    @Inject
    lateinit var dataStorage: DataStorage

    // UI references
    private lateinit var historyFab: HistoryFab
    private lateinit var sheetView: View
    private lateinit var rvTransactions: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var progressView: ProgressBar
    private lateinit var materialSheetFab: MaterialSheetFab<*>
    private lateinit var filterWindow: RelativeLayout
    private lateinit var filterWindowCancel: TextView
    private lateinit var transactionsSumTv: TextView
    private lateinit var noItemsBlock: LinearLayout

    private var statusBarColor: Int = 0
    private var transactionList: MutableList<TransactionLegacy>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.history_frag, container, false)

        filterWindow = root.findViewById(R.id.history_filter_window)
        filterWindowCancel = root.findViewById(R.id.history_filter_window_cancel)

        rvTransactions = root.findViewById(R.id.rv_history)
        progressView = root.findViewById(R.id.history_progress)
        sheetView = root.findViewById(R.id.history_fab_sheet)
        noItemsBlock = root.findViewById(R.id.history_empty_block)
        transactionsSumTv = root.findViewById(R.id.history_sum_tv)

        transactionList = ArrayList()
        adapter = HistoryAdapter(context!!, presenter, transactionList!!, dataStorage)

        val mLayoutManager = GridLayoutManager(context, 1)
        rvTransactions.layoutManager = mLayoutManager
        rvTransactions.itemAnimator = DefaultItemAnimator()
        rvTransactions.adapter = adapter

        presenter.requestTransactions()

        setupFab(root)

        rvTransactions.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && sheetView.visibility == INVISIBLE){
                    historyFab.hide()
                }
                else if (dy < 0 && sheetView.visibility == INVISIBLE) {

                    historyFab.show()
                }
            }
        })

        filterWindowCancel.setOnClickListener {
            presenter.requestTransactions()
            hideFilterWindow()
            dataStorage.transactionsFilter = null
        }

        return root
    }

    /**
     * Sets up the Floating action button.
     */
    private fun setupFab(root: View) {
        historyFab = root.findViewById(R.id.fab_history_addtransaction)

        sheetView = root.findViewById(R.id.history_fab_sheet)
        val overlay = root.findViewById<View>(R.id.history_overlay)
        //        int sheetColor = getResources().getColor(R.color.fab_sheet_color);
        val sheetColor = resources.getColor(R.color.colorPrimary)
        //        int fabColor = getResources().getColor(R.color.fab_color);
        val fabColor = resources.getColor(R.color.colorPrimaryBright)

        // Initialize material sheet FAB
        materialSheetFab = MaterialSheetFab(historyFab, sheetView, overlay,
                sheetColor, fabColor)

        // Set material sheet event listener
        materialSheetFab.setEventListener(object : MaterialSheetFabEventListener() {
            override fun onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor()
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(resources.getColor(R.color.colorAccent))
            }

            override fun onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor)
            }
        })

        root.findViewById<View>(R.id.history_fab_sheet_item_filter).setOnClickListener {
            val intent = Intent(context, HistoryFilterActivity::class.java)
            startActivityForResult(intent, 0)
            materialSheetFab.hideSheet()
        }
        root.findViewById<View>(R.id.history_fab_sheet_item_add_transaction).setOnClickListener {
            val intent = Intent(context, AddEditTransactionActivity::class.java)
            startActivityForResult(intent, 0)
            materialSheetFab.hideSheet()
        }
    }

    override fun showTransactions(transactions: TransactionsSearchLegacyRs?) {
        if (transactions?.items!!.isEmpty())
            noItemsBlock.visibility = View.VISIBLE
        else
            noItemsBlock.visibility = View.GONE
        adapter.updateList(transactions.items)
        transactionsSumTv.text = formatNumberToPretty(transactions.stats.sum)
    }

    override fun showErrorMessage(msg: String) {
        println("showErrorMessage! $msg")
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun showDeleteSuccess() {
        Snackbar.make(activity!!.findViewById(android.R.id.content), R.string.transaction_delete_success, Snackbar.LENGTH_LONG).show()
    }

    override fun showSpinner() {
    }

    override fun hideSpinner() {
        val animTime = resources.getInteger(android.R.integer.config_mediumAnimTime)
        showProgress(false, null, progressView, animTime)
    }

    override fun showFilterWindow() {
        filterWindow.visibility = View.VISIBLE
    }

    override fun hideFilterWindow() {
        filterWindow.visibility = View.GONE
    }

    override fun navigateToLoginActivity() {
        changeActivity(context!!, LoginActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    private fun getStatusBarColor(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor
        } else 0
    }

    private fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor = color
        }
    }
}
