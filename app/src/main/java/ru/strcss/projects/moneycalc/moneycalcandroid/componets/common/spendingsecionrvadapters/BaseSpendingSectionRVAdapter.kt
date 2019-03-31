package ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection

abstract class BaseSpendingSectionRVAdapter<VH : RecyclerView.ViewHolder> internal constructor(context: Context)
    : RecyclerView.Adapter<VH>() {

    protected var mClickListener: ItemClickListener? = null
    protected var inflater: LayoutInflater? = null
    protected var spendingSectionsList: List<SpendingSection>? = null
    internal var logoStorage = DrawableStorage.spendingSectionLogoStorage

    protected var colorPrimaryBright: Int = 0
    protected var colorBackground: Int = 0

    init {
        colorPrimaryBright = ResourcesCompat.getColor(context.resources, R.color.colorPrimaryBright, null)
        colorBackground = ResourcesCompat.getColor(context.resources, R.color.colorBackground, null)
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}
