package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy;

import static ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsPreferenceKey.settings_period_from;
import static ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsPreferenceKey.settings_period_to;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.formatDateToIsoString;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getCalendarFromString;


public class SettingsFragment extends PreferenceFragment implements HasFragmentInjector, SettingsContract.View {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Inject
    SettingsContract.Presenter presenter;

    @Inject
    DataStorage dataStorage;

    private Context context;
    private SharedPreferences preferences;

    @Inject
    public SettingsFragment() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String activeLogin = dataStorage.getActiveUserData().getUserLogin();
        if (preferences.getLong(settings_period_from.name() + activeLogin, -1) == -1 && dataStorage.getSettings() != null) {
            SettingsLegacy settings = dataStorage.getSettings();
            preferences.edit()
                    .putLong(settings_period_from.name() + activeLogin,
                            getCalendarFromString(settings.getPeriodFrom()).getTimeInMillis())
                    .putLong(settings_period_to.name() + activeLogin,
                            getCalendarFromString(settings.getPeriodTo()).getTimeInMillis())
                    .apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        String activeLogin = dataStorage.getActiveUserData().getUserLogin();

        String dateFrom = formatDateToIsoString(new Date(preferences.getLong(settings_period_from.name() + activeLogin, -1)));
        String dateTo = formatDateToIsoString(new Date(preferences.getLong(settings_period_to.name() + activeLogin, -1)));

        SettingsLegacy settings = SettingsLegacy.builder()
                .periodFrom(dateFrom)
                .periodTo(dateTo)
                .build();

        presenter.updateSettings(settings);
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