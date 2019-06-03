package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.formatDateToPretty
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.NumberUtils.Companion.formatNumberToPretty
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDateLegacy

/**
 * Created by Stormcss
 * Date: 29.05.2019
 */
class StatsSumByDateAdapter(private val mContext: Context,
                            private val statsPresenter: StatisticsSumByDateContract.Presenter,
                            private var statsItemsList: ItemsContainer<SumByDateLegacy>,
                            private val dataStorage: DataStorage)
    : RecyclerView.Adapter<StatsSumByDateAdapter.StatsSumByDateViewHolder>() {

    private val logoStorage = DrawableStorage.spendingSectionLogoStorage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsSumByDateViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.stats_sum_by_date_item, parent, false)

        return StatsSumByDateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatsSumByDateViewHolder, position: Int) {
        val sumByDate = statsItemsList.items[position]
        holder.date.text = formatDateToPretty(sumByDate.date)
        holder.sum.text = formatNumberToPretty(sumByDate.sum)
    }

    fun updateStats(statsItems: ItemsContainer<SumByDateLegacy>) {
        this.statsItemsList = statsItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return statsItemsList.items.size
    }

    inner class StatsSumByDateViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var date: TextView
        internal var sum: TextView

        init {
            date = view.findViewById(R.id.stats_sum_by_date_date)
            sum = view.findViewById(R.id.stats_sum_by_date_sum)
        }
    }
}
