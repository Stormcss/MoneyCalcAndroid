package ru.strcss.projects.moneycalc.moneycalcandroid.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Created by Stormcss
 * Date: 01.06.2019
 */
class NumberUtils {
    companion object {
        private val formatter = NumberFormat.getInstance() as DecimalFormat
        private val symbols = formatter.decimalFormatSymbols

        init {
            symbols.groupingSeparator = ' '
            formatter.decimalFormatSymbols = symbols
        }

        fun formatNumberToPretty(number: BigDecimal): String {
            return formatter.format(number)
        }
    }
}
