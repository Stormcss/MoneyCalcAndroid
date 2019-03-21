package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings

import android.app.DatePickerDialog
import android.content.Context
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_login
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsPreferenceKey.settings_period_from
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DatePickerPreference : DialogPreference, DatePicker.OnDateChangedListener, DatePickerDialog.OnDateSetListener {
    private var mDate: Long = 0
    private var twSelectedDate: TextView? = null
    private val currentKey = this.key
    private val otherKey = this.dependency
    @Volatile
    private var currentTimestamp: Long = 0
    @Volatile
    private var otherTimestamp: Long = 0

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        updatePreferenceView(view)
    }

    internal fun updatePreferenceView(view: View?) {
        val activeLogin = sharedPreferences.getString(appl_storage_login.name, null)
        currentTimestamp = sharedPreferences.getLong(currentKey + activeLogin!!, 0L)
        otherTimestamp = sharedPreferences.getLong(otherKey + activeLogin, 0L)

        val dateString = SimpleDateFormat("dd.MM.yyyy").format(Date(currentTimestamp))
        if (twSelectedDate != null) {
            twSelectedDate!!.text = dateString
        } else {
            if (view != null) {
                twSelectedDate = view.findViewById(R.id.pref_datepicker_selected_date)
                twSelectedDate!!.text = dateString
            }
        }
    }

    override fun onCreateDialogView(): View {
        //required to synchronise timestamps in both DatePickerPreference objects
        updatePreferenceView(null)

        val activeLogin = sharedPreferences.getString(appl_storage_login.name, null)

        val picker = DatePicker(context)
        mDate = sharedPreferences.getLong(currentKey + activeLogin!!, 0L)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mDate

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        picker.init(year, month, day, this)

        if (currentKey == settings_period_from.name)
            picker.maxDate = otherTimestamp - DAY_IN_MILLIS
        else
            picker.minDate = otherTimestamp + DAY_IN_MILLIS
        return picker
    }

    override fun onDateChanged(view: DatePicker, year: Int, monthOfYear: Int,
                               dayOfMonth: Int) {
        mDate = Date(year - 1900, monthOfYear, dayOfMonth).time
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                           dayOfMonth: Int) {
        onDateChanged(view, year, monthOfYear, dayOfMonth)
    }

    override fun setDefaultValue(defaultValue: Any) {
        super.setDefaultValue(Date(defaultValue.toString()).time.toString())
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)

        if (positiveResult) {
            if (isPersistent) {
                val activeLogin = sharedPreferences.getString(appl_storage_login.name, null)
                sharedPreferences.edit().putLong(key + activeLogin!!, mDate).apply()
            }
            callChangeListener(mDate.toString())
        }
        updatePreferenceView(null)
    }

    fun init() {
        isPersistent = true
    }

    fun setDate(date: Date) {
        mDate = date.time
    }

    fun setDate(milisseconds: Long) {
        mDate = milisseconds
    }

    fun getDate(style: Int): String {
        return DateFormat.getDateInstance(style).format(Date(mDate))
    }

    fun getDate(): Long {
        return mDate
    }

    companion object {

        private val DAY_IN_MILLIS = 24 * 60 * 60 * 1000
    }
}
