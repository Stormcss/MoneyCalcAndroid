//package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction
//
//import android.app.DatePickerDialog
//import android.content.Context
//import android.os.Bundle
//import android.support.design.widget.FloatingActionButton
//import android.support.v7.widget.GridLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.DatePicker
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//
//import java.util.ArrayList
//import java.util.Calendar
//import java.util.Date
//import java.util.concurrent.atomic.AtomicInteger
//
//import javax.inject.Inject
//
//import dagger.android.support.DaggerFragment
//import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
//import ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters.SpendingSectionRVSingleChooseAdapter
//import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.OnKeyboardVisibilityListener
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils
//import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
//import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy
//
//import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTION
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.hideSoftKeyboard
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.formatDateToIsoString
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.formatDateToPretty
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getCalendarFromString
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getIsoDate
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getPositionBySpendingSectionInnerId
//import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.setKeyboardVisibilityListener
//
//class AddEditTransactionFragment @Inject
//constructor() : DaggerFragment(), AddEditTransactionContract.View, SpendingSectionRVSingleChooseAdapter.ItemClickListener, OnKeyboardVisibilityListener {
//
//    @Inject
//    internal var presenter: AddEditTransactionContract.Presenter? = null
//
//    // UI references
//    private var twTransactionDate: TextView? = null
//    private var etTransactionSum: EditText? = null
//    private var etTransactionTitle: EditText? = null
//    private var etTransactionDesc: EditText? = null
//    private var rvTransactionSection: RecyclerView? = null
//
//    private var selectedSection: SpendingSection? = null
//    private var transactionDate: String? = null
//    private val spendingSectionsList = ArrayList<SpendingSection>()
//    private var ssAdapter: SpendingSectionRVSingleChooseAdapter? = null
//
//    private val selectedRecyclerViewItem = AtomicInteger(-1)
//    private var isEditingTransaction: Boolean = false
//    private var updatedTransactionSectionId: Int = 0
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        val bundle = this.arguments
//
//        val updatedTransactionData = bundle!!.get(TRANSACTION) as TransactionLegacy
//        val fabAddTransaction = activity!!.findViewById<FloatingActionButton>(R.id.fab_addEditTransaction_done)
//
//        twTransactionDate!!.text = formatDateToPretty(formatDateToIsoString(Date()))
//
//        isEditingTransaction = updatedTransactionData != null
//
//        setKeyboardVisibilityListener(this, view!!.parent as ViewGroup)
//
//        fabAddTransaction.setOnClickListener {
//            val sum = etTransactionSum!!.text.toString()
//            val title = etTransactionTitle!!.text.toString()
//            val description = etTransactionDesc!!.text.toString()
//
//            if (!sum.isEmpty() && selectedSection != null) {
//                val transaction = TransactionLegacy.builder()
//                        .date(transactionDate)
//                        .sum(Integer.parseInt(sum))
//                        .description(description)
//                        .title(title)
//                        .sectionId(selectedSection!!.getSectionId())
//                        .build()
//                if (isEditingTransaction) {
//                    presenter!!.editTransaction(updatedTransactionData!!.getId(), transaction)
//                } else {
//                    presenter!!.addTransaction(transaction)
//                }
//                hideSoftKeyboard(activity!!)
//                activity!!.finish()
//            }
//        }
//
//        val onDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//            val date = getIsoDate(year, month + 1, dayOfMonth)
//            twTransactionDate!!.text = formatDateToPretty(date)
//            transactionDate = date
//        }
//
//        twTransactionDate!!.setOnClickListener {
//            val calendar: Calendar
//            if (isEditingTransaction) {
//                calendar = getCalendarFromString(updatedTransactionData!!.getDate())
//            } else {
//                calendar = Calendar.getInstance()
//            }
//            DatePickerDialog(activity!!, onDateSetListener,
//                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//                    calendar.get(Calendar.DAY_OF_MONTH)).show()
//        }
//
//        ssAdapter = SpendingSectionRVSingleChooseAdapter(activity, spendingSectionsList, selectedRecyclerViewItem)
//        ssAdapter!!.setClickListener(this)
//        rvTransactionSection!!.adapter = ssAdapter
//        rvTransactionSection!!.layoutManager = GridLayoutManager(activity, 3)
//
//        if (isEditingTransaction) {
//            setUpdatedTransactionData(updatedTransactionData!!)
//            fabAddTransaction.setImageResource(R.drawable.ic_edit_white_24dp)
//        }
//        presenter!!.requestSpendingSectionsList()
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val root = inflater.inflate(R.layout.addedittransaction_frag, container, false)
//
//        etTransactionSum = root.findViewById(R.id.ae_transaction_sum)
//        etTransactionTitle = root.findViewById(R.id.ae_transaction_title)
//        etTransactionDesc = root.findViewById(R.id.ae_transaction_desc)
//        twTransactionDate = root.findViewById(R.id.ae_transaction_date)
//        rvTransactionSection = root.findViewById(R.id.ae_transaction_section_recyclerview)
//
//        return root
//    }
//
//    private fun setUpdatedTransactionData(transaction: TransactionLegacy) {
//        etTransactionSum!!.setText(String.valueOf(transaction.getSum()))
//        etTransactionTitle!!.setText(transaction.getTitle())
//        etTransactionDesc!!.setText(transaction.getDescription())
//        twTransactionDate!!.text = formatDateToPretty(transaction.getDate())
//
//        updatedTransactionSectionId = transaction.getSectionId()
//    }
//
//    override fun showErrorMessage(msg: String) {
//        println("showErrorMessage! $msg")
//        snackBarAction(activity!!.applicationContext, msg)
//    }
//
//    override fun showAddSuccess() {
//        snackBarAction(activity!!.applicationContext, R.string.transaction_added, R.string.transaction_cancel)
//    }
//
//    override fun showEditSuccess() {
//        val context = activity!!.applicationContext
//        val snackbarWrapper = SnackbarWrapper.make(context,
//                getContext()!!.getText(R.string.transaction_edited), 3000)
//
//        snackbarWrapper.setAction(getContext()!!.getText(R.string.transaction_cancel)
//        ) {
//            Toast.makeText(context, "CANCEL!!!",
//                    Toast.LENGTH_SHORT).show()
//        }
//
//        snackbarWrapper.show()
//    }
//
//    override fun showSpendingSections(spendingSections: List<SpendingSection>) {
//        spendingSectionsList.clear()
//        spendingSectionsList.addAll(spendingSections)
//
//        if (isEditingTransaction) {
//            val position = getPositionBySpendingSectionInnerId(spendingSectionsList, updatedTransactionSectionId)
//            this.onItemClick(null, position)
//        }
//        ssAdapter!!.notifyDataSetChanged()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        presenter!!.takeView(this)
//    }
//
//    override fun onDestroy() {
//        presenter!!.dropView()
//        super.onDestroy()
//    }
//
//    override fun onItemClick(view: View?, position: Int) {
//        selectedSection = ssAdapter!!.getItem(position)
//        selectedRecyclerViewItem.set(position)
//        ssAdapter!!.notifyItemChanged(position)
//    }
//
//    override fun onVisibilityChanged(visible: Boolean) {
//        if (visible)
//            UiUtils.collapseView(rvTransactionSection!!, 100, 200)
//        else
//            UiUtils.expandView(rvTransactionSection!!, 100, 500)
//    }
//}
