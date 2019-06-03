package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections

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
import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection.AddEditSpendingSectionActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection

class SpendingSectionsAdapter(private val mContext: Context,
                              private val sectionList: MutableList<SpendingSection>?,
                              private val presenter: SpendingSectionsContract.Presenter)
    : RecyclerView.Adapter<SpendingSectionsAdapter.SpendingSectionViewHolder>() {

    private val logoStorage = DrawableStorage.spendingSectionLogoStorage

    class SpendingSectionViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var cv: CardView
        internal var logo: ImageView
        internal var menu: ImageView
        internal var name: TextView
        internal var finance: TextView

        init {
            cv = view.findViewById(R.id.spending_section_cardview)
            logo = view.findViewById(R.id.spending_section_logo)
            menu = view.findViewById(R.id.spending_section_menu)
            name = view.findViewById(R.id.spending_section_name)
            finance = view.findViewById(R.id.spending_section_finance)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingSectionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.spendingsection_card, parent, false)

        return SpendingSectionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SpendingSectionViewHolder, position: Int) {
        val spendingSection = sectionList?.get(position)
        spendingSection?.let {
            holder.name.text = it.name
            holder.finance.text = it.budget.toString()
            if (it.logoId != null) {
                holder.logo.setImageResource(logoStorage.get(it.logoId, 0))
            }
            holder.menu.setOnClickListener { showPopupMenu(holder.menu, holder.adapterPosition) }
        }
    }

    fun updateList(sectionList: List<SpendingSection>) {
        this.sectionList?.clear()
        this.sectionList?.addAll(sectionList)
        notifyDataSetChanged()
    }

    /**
     * Showing popup menu when tapping on 3 dots
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
    internal inner class SpendingSectionMenuItemClickListener(private val adapterPosition: Int)
        : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.spendingsection_menu_update -> {
                    val intent = Intent(mContext, AddEditSpendingSectionActivity::class.java)
                    intent.putExtra(AppConstants.SPENDING_SECTION, sectionList?.get(adapterPosition))
//                    intent.putExtra(AppConstants.SPENDING_SECTION, sectionList[adapterPosition])

                    mContext.startActivity(intent)
                    return true
                }

                R.id.spendingsection_menu_delete -> {
                    val alertDialogBuilder = AlertDialog.Builder(mContext)

                    alertDialogBuilder.setTitle(R.string.spending_section_delete)
                            .setMessage(R.string.are_you_sure)
                            .setIcon(R.drawable.ic_warning_red_24dp)
                            .setPositiveButton(R.string.yes) { dialog, id ->
                                presenter.deleteSpendingSection(
                                        sectionList?.get(adapterPosition)?.sectionId)
//                                presenter.deleteSpendingSection(sectionList[adapterPosition]
                            }
                            .setNegativeButton(R.string.no) { dialog, id -> dialog.cancel() }

                    val dialog = alertDialogBuilder.create()
                    dialog.show()
                    return true
                }
            }
            return false
        }
    }

    override fun getItemCount(): Int {
        return sectionList?.size ?: 0
    }

}
