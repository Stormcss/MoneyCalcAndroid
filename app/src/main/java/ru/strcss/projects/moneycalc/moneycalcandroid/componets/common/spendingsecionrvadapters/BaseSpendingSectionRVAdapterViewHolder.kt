package ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R

/**
 * Created by Stormcss
 * Date: 25.03.2019
 */
class BaseSpendingSectionRVAdapterViewHolder internal constructor(itemView: View,
                                                                  var mClickListener: BaseSpendingSectionRVAdapter.ItemClickListener?)
    : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var sectionName: TextView
    var sectionLayout: RelativeLayout
    var sectionLogo: ImageView

    init {
        sectionName = itemView.findViewById(R.id.ae_transaction_section_name)
        sectionLogo = itemView.findViewById(R.id.ae_transaction_section_logo)
        sectionLayout = itemView.findViewById(R.id.ae_transaction_section_layout)
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (mClickListener != null)
            mClickListener!!.onItemClick(view, adapterPosition)
    }
}