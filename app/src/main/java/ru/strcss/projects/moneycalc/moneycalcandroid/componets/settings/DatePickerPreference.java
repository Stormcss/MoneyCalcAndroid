package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings;

import android.app.DatePickerDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;

import static ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_login;
import static ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsPreferenceKey.settings_period_from;

public class DatePickerPreference extends DialogPreference implements
        DatePicker.OnDateChangedListener, DatePickerDialog.OnDateSetListener {

    private static final int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    private long mDate;
    private TextView twSelectedDate;
    private final String currentKey = this.getKey();
    private final String otherKey = this.getDependency();
    private volatile long currentTimestamp;
    private volatile long otherTimestamp;

    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DatePickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        updatePreferenceView(view);
    }

    void updatePreferenceView(View view) {
        String activeLogin = getSharedPreferences().getString(appl_storage_login.name(), null);
        currentTimestamp = getSharedPreferences().getLong(currentKey + activeLogin, 0L);
        otherTimestamp = getSharedPreferences().getLong(otherKey + activeLogin, 0L);

        String dateString = new SimpleDateFormat("dd.MM.yyyy").format(new Date(currentTimestamp));
        if (twSelectedDate != null) {
            twSelectedDate.setText(dateString);
        } else {
            if (view != null) {
                twSelectedDate = view.findViewById(R.id.pref_datepicker_selected_date);
                twSelectedDate.setText(dateString);
            }
        }
    }

    @Override
    protected View onCreateDialogView() {
        //required to synchronise timestamps in both DatePickerPreference objects 
        updatePreferenceView(null);

        String activeLogin = getSharedPreferences().getString(appl_storage_login.name(), null);

        DatePicker picker = new DatePicker(getContext());
        mDate = getSharedPreferences().getLong(currentKey + activeLogin, 0L);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDate);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        picker.init(year, month, day, this);

        if (currentKey.equals(settings_period_from.name()))
            picker.setMaxDate(otherTimestamp - DAY_IN_MILLIS);
        else
            picker.setMinDate(otherTimestamp + DAY_IN_MILLIS);
        return picker;
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        mDate = (new Date(year - 1900, monthOfYear, dayOfMonth)).getTime();
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        onDateChanged(view, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        super.setDefaultValue(String.valueOf((
                new Date(String.valueOf(defaultValue))).getTime()));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            if (isPersistent()) {
                String activeLogin = getSharedPreferences().getString(appl_storage_login.name(), null);
                getSharedPreferences().edit().putLong(getKey() + activeLogin, mDate).apply();
            }
            callChangeListener(String.valueOf(mDate));
        }
        updatePreferenceView(null);
    }

    public void init() {
        setPersistent(true);
    }

    public void setDate(Date date) {
        mDate = date.getTime();
    }

    public void setDate(long milisseconds) {
        mDate = milisseconds;
    }

    public String getDate(int style) {
        return DateFormat.getDateInstance(style).format(new Date(mDate));
    }

    public long getDate() {
        return mDate;
    }
}
