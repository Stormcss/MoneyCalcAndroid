package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.FINANCE_SUMMARY_BY_SECTION
import ru.strcss.projects.moneycalc.moneycalcdto.entities.FinanceSummaryBySection

class HomeStatsFragment : Fragment(), HomeStatsContract.View {

    // UI references
    private var tvDayBalance: TextView? = null
    private var tvSummaryBalance: TextView? = null
    private var tvSpendAll: TextView? = null
    private var tvLeftAll: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.home_stats_frag, container, false)

        val financeSummary = arguments!!.getSerializable(FINANCE_SUMMARY_BY_SECTION) as FinanceSummaryBySection

        tvDayBalance = root.findViewById(R.id.home_stats_day_balance)
        tvSummaryBalance = root.findViewById(R.id.home_stats_summaryBalance)
        tvSpendAll = root.findViewById(R.id.home_stats_spend)
        tvLeftAll = root.findViewById(R.id.home_stats_left)

        if (financeSummary == null)
            return root

        setBalanceValue(tvDayBalance!!, financeSummary.todayBalance, true)
        setBalanceValue(tvSummaryBalance!!, financeSummary.summaryBalance, true)
        setBalanceValue(tvSpendAll!!, financeSummary.moneySpendAll, false)
        setBalanceValue(tvLeftAll!!, financeSummary.moneyLeftAll, true)
        return root
    }

    private fun setBalanceValue(textView: TextView, value: Double?, setColor: Boolean) {
        textView.text = value.toString()
        if (setColor)
            setBalanceColor(textView, value)
    }

    private fun setBalanceColor(textView: TextView, value: Double?) {
        if (value!! > 0)
            textView.setTextColor(Color.rgb(0, 150, 0))
        else if (value < 0)
            textView.setTextColor(Color.rgb(150, 0, 0))
        else
            textView.setTextColor(Color.BLACK)
    }


    override fun showErrorMessage(msg: String) {
        println("showErrorMessage! $msg")
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    companion object {

        fun newInstance(financeSummary: FinanceSummaryBySection): HomeStatsFragment {
            val fragment = HomeStatsFragment()

            val args = Bundle()
            args.putSerializable(FINANCE_SUMMARY_BY_SECTION, financeSummary)
            fragment.arguments = args

            return fragment
        }
    }
}
