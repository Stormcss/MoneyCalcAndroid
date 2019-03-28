package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTION
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction.AddEditTransactionActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.setViewVisibility
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getLogoIdBySectionId
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy

class HistoryAdapter(private val mContext: Context, private val historyPresenter: HistoryContract.Presenter, private val transactionList: MutableList<TransactionLegacy>, private val dataStorage: DataStorage) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> /*implements HistoryAdapter.ItemClickListener*/() {

    private var checkedRvItem: View? = null

    private val logoStorage = DrawableStorage.getSpendingSectionLogoStorage()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.history_card, parent, false)

        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.title.text = transaction.title
        holder.description.text = transaction.description
        holder.date.text = DatesUtils.formatDateToPretty(transaction.date)
        holder.sum.text = transaction.sum.toString()

        val sectionLogoId = getLogoIdBySectionId(dataStorage.spendingSections, transaction.sectionId)
        if (sectionLogoId != null) {
            holder.logo.setImageResource(logoStorage.get(sectionLogoId))
        }

        holder.menu.setOnClickListener { showPopupMenu(holder.menu, holder.adapterPosition) }

        holder.cv.setOnClickListener {
            if (checkedRvItem != null) {
                if (checkedRvItem === holder.description) {
                    setViewVisibility(checkedRvItem, false)
                    checkedRvItem = null
                } else {
                    setViewVisibility(holder.description, true)
                    setViewVisibility(checkedRvItem, false)
                    checkedRvItem = holder.description
                }
            } else {
                setViewVisibility(holder.description, true)
                checkedRvItem = holder.description
            }
        }
        setViewVisibility(holder.description, false)
    }

    fun updateList(transactions: List<TransactionLegacy>) {
        this.transactionList.clear()
        this.transactionList.addAll(transactions)
        notifyDataSetChanged()
    }

    /**
     * Showing popup menu when tapping on settings icon
     */
    private fun showPopupMenu(view: View, adapterPosition: Int) {
        // inflate menu
        val popup = PopupMenu(mContext, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_spending_section, popup.menu)
        popup.setOnMenuItemClickListener(SpendingSectionMenuItemClickListener(adapterPosition))
        popup.show()
    }

    /**
     * Click listener for popup menu items
     */
    inner class SpendingSectionMenuItemClickListener(private val adapterPosition: Int) : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.spendingsection_menu_update -> {
                    val intent = Intent(mContext, AddEditTransactionActivity::class.java)
                    intent.putExtra(TRANSACTION, transactionList[adapterPosition])

                    mContext.startActivity(intent)

                    return true
                }
                R.id.spendingsection_menu_delete -> {
                    val alertDialogBuilder = AlertDialog.Builder(mContext)

                    alertDialogBuilder.setTitle(R.string.transaction_delete_title)
                            .setMessage(getMergedDescription(mContext, transactionList[adapterPosition]))
                            .setIcon(R.drawable.ic_warning_red_24dp)
                            .setPositiveButton(R.string.yes) { dialog, id -> historyPresenter.deleteTransaction(transactionList[adapterPosition].id) }
                            .setNegativeButton(R.string.cancel) { dialog, id -> dialog.cancel() }

                    val dialog = alertDialogBuilder.create()
                    dialog.show()

                    return true
                }
            }
            return false
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    private fun getMergedDescription(context: Context, transaction: TransactionLegacy): String {
        val stringBuilder = StringBuilder(context.getText(R.string.transaction_delete_part1))
                .append(" ")
                .append(transaction.sum)
                .append(" ")
                .append(context.getText(R.string.transaction_delete_part3))
        if (transaction.description != null && !transaction.description.isEmpty()) {
            stringBuilder.append(" ")
                    .append(context.getText(R.string.transaction_delete_part4))
                    .append(" \"")
                    .append(transaction.description)
                    .append("\"")
        }
        return stringBuilder.append("?").toString()
    }

    inner class HistoryViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var cv: CardView
        internal var logo: ImageView
        internal var menu: ImageView

        internal var sum: TextView
        internal var date: TextView
        internal var title: TextView
        internal var description: TextView

        init {
            cv = view.findViewById(R.id.transaction_cardview)
            logo = view.findViewById(R.id.transaction_spending_section_logo)
            menu = view.findViewById(R.id.transaction_menu)
            sum = view.findViewById(R.id.transaction_sum)
            date = view.findViewById(R.id.transaction_date)
            title = view.findViewById(R.id.transaction_title)
            description = view.findViewById(R.id.transaction_desc)
        }
    }
}
