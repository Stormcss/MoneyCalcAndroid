package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper;

import static ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsPreferenceKey.appl_settings_server_ip;
import static ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsPreferenceKey.appl_settings_server_port;


public class ApplicationSettingsFragment extends PreferenceFragment implements HasFragmentInjector,
        ApplicationSettingsContract.View, OnSharedPreferenceChangeListener {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Inject
    ApplicationSettingsContract.Presenter presenter;

    @Inject
    DataStorage dataStorage;

    @Inject
    MoneyCalcServerDAO moneyCalcServerDAO;

    private Context context;

    @Inject
    public ApplicationSettingsFragment() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.application_settings_preferences);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(findPreference(key), key);
    }

    private void updatePreference(Preference preference, String key) {
        if (preference == null) return;
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
            return;
        }
        SharedPreferences sharedPrefs = getPreferenceManager().getSharedPreferences();
        preference.setSummary(sharedPrefs.getString(key, getDefaultValueForPreference(key)));
    }

    private String getDefaultValueForPreference(String key) {
        switch (ApplicationSettingsPreferenceKey.valueOf(key)) {
            case appl_settings_server_ip:
                return appl_settings_server_ip.getDefaultValue();
            case appl_settings_server_port:
                return appl_settings_server_port.getDefaultValue();
            default:
                return "";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    Preference singlePref = preferenceGroup.getPreference(j);
                    updatePreference(singlePref, singlePref.getKey());
                }
            } else {
                updatePreference(preference, preference.getKey());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String serverIP = preferences.getString(appl_settings_server_ip.name(), "");
        String serverPort = preferences.getString(appl_settings_server_port.name(), "");

        presenter.updateServerIP(serverIP, serverPort);
    }

    @Override
    public void onAttach(Context context) {
        AndroidInjection.inject(this);
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return childFragmentInjector;
    }

    @Override
    public void showUpdateSuccess() {
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(context,
                context.getText(R.string.settings_update_success), 3000);

        snackbarWrapper.setAction(context.getText(R.string.cancel),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "CANCEL!!!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        snackbarWrapper.show();
    }

    @Override
    public void showErrorMessage(String msg) {
    }
}