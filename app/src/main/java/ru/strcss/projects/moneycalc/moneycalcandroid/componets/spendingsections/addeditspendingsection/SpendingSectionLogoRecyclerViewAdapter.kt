package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff.Mode
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage
import java.util.concurrent.atomic.AtomicInteger

class SpendingSectionLogoRecyclerViewAdapter(context: Context, private val selectedPosition: AtomicInteger)
    : RecyclerView.Adapter<SpendingSectionLogoRecyclerViewAdapter.ViewHolder>() {

    private val inflater: LayoutInflater
    private val logoStorage = DrawableStorage.getSpendingSectionLogoStorage()
    private var mClickListener: ItemClickListener? = null

    private val colorPrimaryBright: Int
    private val colorBackground: Int

    init {
        this.inflater = LayoutInflater.from(context)

        colorPrimaryBright = ResourcesCompat.getColor(context.resources, R.color.colorPrimaryBright, null)
        colorBackground = ResourcesCompat.getColor(context.resources, R.color.colorBackground, null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.addeditspendingsection_spendingsection_logo_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //        int logoId = ;
        //        System.out.println("position = " + position);
        //        System.out.println("logoId = " + logoId);
        //        Drawable drawable = holder.itemView.getContext().getResources().getDrawable(logoId);
        //        System.out.println("drawable = " + drawable);
        //        holder.sectionLogo.setImageDrawable(drawable);
        holder.sectionLogo.setImageResource(logoStorage.get(position, 0))

        holder.sectionLayoutLayout.setOnClickListener { v ->
            selectedPosition.set(position)
            holder.onClick(v)
            notifyDataSetChanged()
        }

        if (selectedPosition.get() == position) {
            holder.sectionLayoutLayout.setBackgroundColor(colorPrimaryBright)
            holder.sectionLogo.setColorFilter(Color.WHITE, Mode.SRC_ATOP)
        } else {
            holder.sectionLayoutLayout.setBackgroundColor(colorBackground)
            holder.sectionLogo.colorFilter = null
        }

    }

    override fun getItemCount(): Int {
        return logoStorage.size()
    }

    inner class ViewHolder internal constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val sectionLayoutLayout: RelativeLayout
        val sectionLogo: ImageView

        init {
            sectionLogo = itemView.findViewById(R.id.ae_spendingsection_section_logo)
            sectionLayoutLayout = itemView.findViewById(R.id.ae_spendingsection_section_layout)

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null)
                mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    //    public int getPositionByLogoId(int logoId){
    //        return logoStorage.get(logoId, 0);
    //    }

    internal fun getItem(id: Int): Int {
        return logoStorage.get(id)
    }

    internal fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}

