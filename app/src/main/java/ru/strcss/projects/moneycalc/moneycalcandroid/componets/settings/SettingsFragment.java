package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;


public class SettingsFragment extends PreferenceFragment implements DatePickerDialog.OnDateSetListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private int year;
    private int month;
    private int day;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mypreferences);

        Preference preferencePeriodFrom = findPreference("settings_period_from");
//        preferencePeriodFrom.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                showDateDialog();
//                return false;
//            }
//        });
        preferencePeriodFrom.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return false;
            }
        });
//        Preference preferencePeriodTo = findPreference("settings_period_to");
//        String defaultValue = "Smith";
//        PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("settings_period_to", defaultValue);

//        periodToPreference.setSummary(periodToPreference.getSelectedData().toString());

//        DatePickerPreference periodToPreference = (DatePickerPreference) findPreference("settings_period_to");
//        periodToPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                preference.setSummary(newValue.toString());
//                return true;
//            }
//        });
//        periodToPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                showDateDialog();
//                return false;
//            }
//        });
    }

    private void showDateDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getActivity(), this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;

        System.out.println(year + " " + month + " " + dayOfMonth);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePrefSummary(findPreference(key));
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof DatePickerPreference) {
            DatePickerPreference datePickerPref = (DatePickerPreference) p;
//            p.setSummary(datePickerPref.getEntry());
        }
//        if (p instanceof EditTextPreference) {
//            EditTextPreference editTextPref = (EditTextPreference) p;
//            if (p.getTitle().toString().toLowerCase().contains("password"))
//            {
//                p.setSummary("******");
//            } else {
//                p.setSummary(editTextPref.getText());
//            }
//        }
//        if (p instanceof MultiSelectListPreference) {
//            EditTextPreference editTextPref = (EditTextPreference) p;
//            p.setSummary(editTextPref.getText());
//        }
    }
}
