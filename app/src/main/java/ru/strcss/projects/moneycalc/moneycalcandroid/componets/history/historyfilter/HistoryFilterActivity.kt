package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import dagger.android.support.DaggerAppCompatActivity
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters.BaseSpendingSectionRVAdapter
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters.SpendingSectionRVMultiChooseAdapter
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.OnKeyboardVisibilityListener
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.changeActivityOnCondition
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.*
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.setKeyboardVisibilityListener
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import java.util.*
import javax.inject.Inject

class HistoryFilterActivity @Inject
constructor() : DaggerAppCompatActivity(), HistoryFilterContract.View,
        BaseSpendingSectionRVAdapter.ItemClickListener, OnKeyboardVisibilityListener {

    @Inject
    lateinit var presenter: HistoryFilterContract.Presenter

    @Inject
    lateinit var moneyCalcServerDAO: MoneyCalcServerDAO

    @Inject
    lateinit var dataStorage: DataStorage

    private var twDateFrom: TextView? = null
    private var twDateTo: TextView? = null
    private var rvSections: RecyclerView? = null
    private var btnSectionsCheckAll: Button? = null
    private var btnSectionsUncheckAll: Button? = null
    private var layoutSectionButtonsPane: LinearLayout? = null
    private var etTitle: EditText? = null
    private var etDesc: EditText? = null

    private var mActionBar: ActionBar? = null

    private var filter: TransactionsSearchFilterLegacy? = null

    private val spendingSectionsList = ArrayList<SpendingSection>()
    private var ssAdapter: SpendingSectionRVMultiChooseAdapter? = null
    private val selectedRecyclerViewItems = HashSet<Int?>()

    private val onDateFromSetListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        val date = getIsoDate(year, month + 1, dayOfMonth)
        twDateFrom!!.text = date
        filter!!.dateFrom = date
    }

    private val onDateToSetListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        val date = getIsoDate(year, month + 1, dayOfMonth)
        twDateTo!!.text = date
        filter!!.dateTo = date
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeActivityOnCondition(moneyCalcServerDAO.token == null, this@HistoryFilterActivity, LoginActivity::class.java)

        setContentView(R.layout.history_filter_activity)
        setKeyboardVisibilityListener(this, findViewById<View>(R.id.history_filter_root_view) as ViewGroup)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mActionBar = supportActionBar
        mActionBar!!.setDisplayHomeAsUpEnabled(true)
        mActionBar!!.setDisplayShowHomeEnabled(true)
        mActionBar!!.setTitle(R.string.menu_history_filter)

        twDateFrom = findViewById(R.id.history_filter_date_from)
        twDateTo = findViewById(R.id.history_filter_date_to)
        rvSections = findViewById(R.id.history_filter_sections)
        btnSectionsCheckAll = findViewById(R.id.history_filter_section_check_all_button)
        btnSectionsUncheckAll = findViewById(R.id.history_filter_section_uncheck_all_button)
        etTitle = findViewById(R.id.history_filter_title)
        etDesc = findViewById(R.id.history_filter_desc)
        layoutSectionButtonsPane = findViewById(R.id.history_filter_section_buttons_pane)

        filter = dataStorage.transactionsFilter

        if (filter == null) {
            filter = TransactionsSearchFilterLegacy()
            filter!!.dateFrom = dataStorage.settings!!.periodFrom
            filter!!.dateTo = dataStorage.settings!!.periodTo
        }

        setTransactionPeriodListener(filter)

        setSectionsCheckButtons()

        ssAdapter = SpendingSectionRVMultiChooseAdapter(this, spendingSectionsList, selectedRecyclerViewItems)
        ssAdapter!!.setClickListener(this)
        rvSections!!.adapter = ssAdapter
        rvSections!!.layoutManager = GridLayoutManager(this, 3)

        presenter.requestSpendingSectionsList()
    }

    private fun setSectionsCheckButtons() {
        btnSectionsCheckAll!!.setOnClickListener {
            for (spendingSection in spendingSectionsList) {
                selectedRecyclerViewItems.add(spendingSection.sectionId)
            }
            ssAdapter!!.notifyDataSetChanged()
        }
        btnSectionsUncheckAll!!.setOnClickListener {
            selectedRecyclerViewItems.clear()
            ssAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        if (filter != null) {
            filter!!.requiredSections = ArrayList(selectedRecyclerViewItems)
            val title = etTitle!!.text.toString()
            val description = etDesc!!.text.toString()
            if (!title.isEmpty())
                filter!!.title = title
            if (!description.isEmpty())
                filter!!.description = description

            println("filter = " + filter!!)
            presenter.requestFilteredTransactions(filter!!)
        }
        super.onBackPressed()
    }

    private fun setTransactionPeriodListener(savedFilter: TransactionsSearchFilterLegacy?) {
        var dateFrom: String? = null
        var dateTo: String? = null

        if (savedFilter != null) {
            dateFrom = formatDateToPretty(savedFilter.dateFrom)
            dateTo = formatDateToPretty(savedFilter.dateTo)
            twDateFrom!!.text = dateFrom
            twDateTo!!.text = dateTo
        } else {
            val calendar = formatDateToPretty(getStringIsoDateFromCalendar(Calendar.getInstance()))
            twDateFrom!!.text = calendar
            twDateTo!!.text = calendar
        }

        twDateFrom!!.setOnClickListener(getDateClickListener(dateFrom, onDateFromSetListener))
        twDateTo!!.setOnClickListener(getDateClickListener(dateTo, onDateToSetListener))
    }

    private fun getDateClickListener(date: String?,
                                     onDateSetListener: DatePickerDialog.OnDateSetListener): View.OnClickListener {
        return View.OnClickListener {
            val calendar: Calendar
            if (date != null) {
                calendar = getCalendarFromString(formatDateToIso(date))
            } else {
                calendar = Calendar.getInstance()
            }
            getDatePickerDialog(onDateSetListener, calendar).show()
        }
    }

    private fun getDatePickerDialog(listener: DatePickerDialog.OnDateSetListener, calendar: Calendar): DatePickerDialog {
        return DatePickerDialog(this, listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onItemClick(view: View?, position: Int) {
        ssAdapter!!.notifyItemChanged(position)
    }

    public override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    public override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    override fun showErrorMessage(msg: String) {

    }

    override fun onVisibilityChanged(visible: Boolean) {
        if (visible) {
            UiUtils.collapseView(rvSections!!, 100, 100)
            UiUtils.collapseView(layoutSectionButtonsPane!!, 100, 0)
        } else {
            UiUtils.expandView(rvSections!!, 100, 500)
            UiUtils.expandView(layoutSectionButtonsPane!!, 100, 180)
        }
    }

    override fun showSpendingSections(spendingSections: List<SpendingSection>) {
        spendingSectionsList.clear()
        spendingSectionsList.addAll(spendingSections)

        if (filter != null && filter!!.requiredSections != null) {
            selectedRecyclerViewItems.addAll(filter!!.requiredSections)
        }

        ssAdapter!!.notifyDataSetChanged()
    }
}
