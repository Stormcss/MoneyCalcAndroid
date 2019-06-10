package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.*
import android.widget.Toast
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsPreferenceKey.appl_settings_server_ip
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsPreferenceKey.appl_settings_server_port
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper
import javax.inject.Inject


class ApplicationSettingsFragment @Inject
constructor() : PreferenceFragment(), HasFragmentInjector, ApplicationSettingsContract.View, OnSharedPreferenceChangeListener {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var presenter: ApplicationSettingsContract.Presenter

    @Inject
    lateinit var dataStorage: DataStorage

    @Inject
    lateinit var moneyCalcServerDAO: MoneyCalcServerDAO

     var localContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.application_settings_preferences)

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        updatePreference(findPreference(key), key)
    }

    private fun updatePreference(preference: Preference?, key: String) {
        if (preference == null) return
        if (preference is ListPreference) {
            val listPreference = preference as ListPreference?
            listPreference!!.summary = listPreference.entry
            return
        }
        val sharedPrefs = preferenceManager.sharedPreferences
        preference.summary = sharedPrefs.getString(key, getDefaultValueForPreference(key))
    }

    private fun getDefaultValueForPreference(key: String): String {
        when (ApplicationSettingsPreferenceKey.valueOf(key)) {
            appl_settings_server_ip -> return appl_settings_server_ip.defaultValue
            appl_settings_server_port -> return appl_settings_server_port.defaultValue
            else -> return ""
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
        for (i in 0 until preferenceScreen.preferenceCount) {
            val preference = preferenceScreen.getPreference(i)
            if (preference is PreferenceGroup) {
                for (j in 0 until preference.preferenceCount) {
                    val singlePref = preference.getPreference(j)
                    updatePreference(singlePref, singlePref.key)
                }
            } else {
                updatePreference(preference, preference.key)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)

        val serverIP = preferences.getString(appl_settings_server_ip.name, "")
        val serverPort = preferences.getString(appl_settings_server_port.name, "")

        presenter.updateServerIP(serverIP, serverPort)
    }

    override fun onAttach(context: Context) {
        AndroidInjection.inject(this)
        this.localContext = context
        super.onAttach(context)
    }

    override fun fragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    override fun showUpdateSuccess() {
        val snackbarWrapper = SnackbarWrapper.make(localContext!!,
                localContext!!.getText(R.string.settings_update_success), 3000)

        snackbarWrapper.setAction(localContext!!.getText(R.string.cancel)
        ) {
            Toast.makeText(localContext, "CANCEL!!!",
                    Toast.LENGTH_SHORT).show()
        }

        snackbarWrapper.show()
    }

    override fun showErrorMessage(msg: String) {}
}