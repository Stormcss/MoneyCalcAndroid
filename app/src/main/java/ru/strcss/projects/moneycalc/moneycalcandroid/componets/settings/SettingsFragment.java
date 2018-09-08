package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;

import static ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.PreferenceKey.settings_period_from;
import static ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.PreferenceKey.settings_period_to;


public class SettingsFragment extends PreferenceFragment implements HasFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Inject
    SettingsContract.Presenter presenter;

    @Inject
    DataStorage dataStorage;

    @Inject
    public SettingsFragment() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mypreferences);
    }


    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.getLong(settings_period_from.name(), -1);
        preferences.getLong(settings_period_to.name(), -1);
    }

    @Override
    public void onAttach(Context context) {
        AndroidInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return childFragmentInjector;
    }
}