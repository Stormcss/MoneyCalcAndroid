package ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff.Mode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getSpendingSectionInnerIdByPosition
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection

class SpendingSectionRVMultiChooseAdapter(context: Context, spendingSections: List<SpendingSection>,
                                          private val selectedSpendingSections: MutableSet<Int?>)
    : BaseSpendingSectionRVAdapter<BaseSpendingSectionRVAdapterViewHolder>(context) {

    init {
        this.spendingSectionsList = spendingSections
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSpendingSectionRVAdapterViewHolder {
        val view = inflater!!.inflate(R.layout.addedittransaction_spendingsection_card, parent, false)
        return BaseSpendingSectionRVAdapterViewHolder(view, mClickListener)
    }

    override fun onBindViewHolder(holder: BaseSpendingSectionRVAdapterViewHolder, position: Int) {
        val spendingSection = spendingSectionsList!![position]
        holder.sectionName.setText(spendingSection.name)
        if (spendingSection.logoId != null) {
            holder.sectionLogo.setImageResource(DrawableStorage.spendingSectionLogoStorage.get(spendingSection.logoId!!))
        }

        holder.sectionLayout.setOnClickListener(View.OnClickListener { v ->
            //                System.out.println("adapter item! = " + position);
            val sectionId = getSpendingSectionInnerIdByPosition(spendingSectionsList, position)
            val isPositionNew = selectedSpendingSections.add(sectionId)
            if (!isPositionNew)
                selectedSpendingSections.remove(sectionId)
            holder.onClick(v)
            notifyDataSetChanged()
        })

        if (isPositionSelectedInSet(position, spendingSectionsList, selectedSpendingSections)) {
            holder.sectionLayout.setBackgroundColor(colorPrimaryBright)
            holder.sectionName.setTextColor(Color.WHITE)
            holder.sectionLogo.setColorFilter(Color.WHITE, Mode.SRC_ATOP)
        } else {
            holder.sectionLayout.setBackgroundColor(colorBackground)
            holder.sectionName.setTextColor(Color.BLACK)
            holder.sectionLogo.setColorFilter(null)
        }
    }

    private fun isPositionSelectedInSet(position: Int, sectionList: List<SpendingSection>?,
                                        selectedSectionsIds: Set<Int?>): Boolean {
        val sectionId = getSpendingSectionInnerIdByPosition(sectionList, position)
        for (selectedSectionId in selectedSectionsIds) {
            if (selectedSectionId == sectionId)
                return true
        }
        return false
    }

    override fun getItemCount(): Int {
        return spendingSectionsList!!.size
    }

    fun setClickListener(itemClickListener: BaseSpendingSectionRVAdapter.ItemClickListener) {
        this.mClickListener = itemClickListener
    }
}

