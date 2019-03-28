package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.sectiontabs

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DrawableStorage
import java.util.*

/**
 * Created by Stormcss
 * Date: 28.01.2019
 */
class TabAdapter(private val listView: ListView?, private var listener: OnItemClickListener?) : BaseAdapter(), AdapterView.OnItemClickListener {

    val data: MutableList<TabHolder> = ArrayList()
    private val logoStorage = DrawableStorage.getSpendingSectionLogoStorage()

    private var currentSelected = 0

    init {
        listView?.onItemClickListener = this
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(i: Int): TabHolder {
        return data[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        // Currently not using viewHolder pattern cause there aren't too many tabs in the demo project.

        if (view == null) {
            view = LayoutInflater.from(viewGroup.context).inflate(R.layout.home_section_tab_item, viewGroup, false)
        }

        val tabTitle = view!!.findViewById<TextView>(R.id.home_tab_title)
        val tabLogo = view.findViewById<ImageView>(R.id.home_tab_spending_section_logo)
        tabTitle.text = getItem(i).sectionName

        if (getItem(i).logoId != null)
            tabLogo.setImageResource(logoStorage.get(getItem(i).logoId!!, 0))

        if (i == currentSelected) {
            setTextViewToSelected(view, tabTitle, tabLogo)
        } else {
            setTextViewToUnSelected(view, tabTitle, tabLogo)
        }

        return view
    }

    /**
     * Return item view at the given position or null if position is not visible.
     */
    fun getViewByPosition(pos: Int): View? {
        if (listView == null) {
            return null
        }
        val firstListItemPosition = listView.firstVisiblePosition
        val lastListItemPosition = firstListItemPosition + listView.childCount - 1

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return null
        } else {
            val childIndex = pos - firstListItemPosition
            return listView.getChildAt(childIndex)
        }
    }

    private fun setTextViewToSelected(tab: View, tabTitle: TextView, tabLogo: ImageView) {
        tab.setBackgroundResource(R.color.indigoA400)
        tabTitle.setTextColor(Color.WHITE)
        tabLogo.setColorFilter(Color.WHITE)
    }

    private fun setTextViewToUnSelected(tab: View, tabTitle: TextView, tabLogo: ImageView) {
        tab.setBackgroundResource(R.color.indigo100)
        tabTitle.setTextColor(Color.GRAY)
        tabLogo.setColorFilter(Color.DKGRAY)
    }

    private fun select(position: Int) {
        if (currentSelected >= 0) {
            deselect(currentSelected)
        }
        val targetView = getViewByPosition(position)
        if (targetView != null) {
            val tabTitle = targetView.findViewById<TextView>(R.id.home_tab_title)
            val tabLogo = targetView.findViewById<ImageView>(R.id.home_tab_spending_section_logo)
            setTextViewToSelected(targetView, tabTitle, tabLogo)
        }
        if (listener != null) {
            listener!!.selectItem(position)
        }
        currentSelected = position
    }

    private fun deselect(position: Int) {
        if (getViewByPosition(position) != null) {
            val targetView = getViewByPosition(position)
            if (targetView != null) {
                val tabTitle = targetView.findViewById<TextView>(R.id.home_tab_title)
                val tabLogo = targetView.findViewById<ImageView>(R.id.home_tab_spending_section_logo)
                setTextViewToUnSelected(targetView, tabTitle, tabLogo)
            }
        }
        currentSelected = -1
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        select(i)
    }

    fun OnItemClickListener(listener: TabAdapter.OnItemClickListener) {
        this.listener = listener
    }

    fun setCurrentSelected(i: Int) {
        select(i)
    }

    interface OnItemClickListener {
        fun selectItem(position: Int)
    }

}
