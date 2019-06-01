package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbysection

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getLogoIdByName
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumBySection

/**
 * Created by Stormcss
 * Date: 29.05.2019
 */
class StatsSumBySectionAdapter(private val mContext: Context,
                               private val statsPresenter: StatisticsSumBySectionContract.Presenter,
                               private var statsItemsList: ItemsContainer<SumBySection>,
                               private val dataStorage: DataStorage)
    : RecyclerView.Adapter<StatsSumBySectionAdapter.StatsBySectionSumViewHolder>() {

    private val logoStorage = DrawableStorage.spendingSectionLogoStorage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsBySectionSumViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.stats_sum_by_section_item, parent, false)

        return StatsBySectionSumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatsBySectionSumViewHolder, position: Int) {
        val sumBySection = statsItemsList.items[position]
        holder.name.text = sumBySection.name
        holder.sum.text = sumBySection.sum.toPlainString()

        val sectionLogoId = getLogoIdByName(dataStorage.spendingSections?.items, sumBySection.name)
        if (sectionLogoId != null) {
            holder.logo.setImageResource(logoStorage.get(sectionLogoId))
        }
    }

    fun updateStats(statsItems: ItemsContainer<SumBySection>) {
//        this.statsItemsList.items.clear()
        this.statsItemsList = statsItems
//        this.statsItemsList.items.addAll(statsItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return statsItemsList.items.size
    }

    inner class StatsBySectionSumViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var logo: ImageView

        internal var name: TextView
        internal var sum: TextView

        init {
            logo = view.findViewById(R.id.stats_sum_by_section_logo)
            name = view.findViewById(R.id.stats_sum_by_section_name)
            sum = view.findViewById(R.id.stats_sum_by_section_sum)
        }
    }
}
