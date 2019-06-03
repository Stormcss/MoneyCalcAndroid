package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.widget.Toast
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsPreferenceKey.settings_period_from
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsPreferenceKey.settings_period_to
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.formatDateToIsoString
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getCalendarFromString
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy
import java.util.*
import javax.inject.Inject


class SettingsFragment @Inject
constructor() : PreferenceFragment(), HasFragmentInjector, SettingsContract.View {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var presenter: SettingsContract.Presenter

    @Inject
    lateinit var dataStorage: DataStorage

    var context: Context? = null
        @get:JvmName("getContext_") get //fixme WTF?
    private var preferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings_preferences)

        preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val activeLogin = dataStorage.activeUserData.userLogin
        if (preferences!!.getLong(settings_period_from.name + activeLogin!!, -1) == -1L && dataStorage.settings != null) {
            val settings = dataStorage.settings
            preferences!!.edit()
                    .putLong(settings_period_from.name + activeLogin,
                            getCalendarFromString(settings!!.periodFrom).timeInMillis)
                    .putLong(settings_period_to.name + activeLogin,
                            getCalendarFromString(settings.periodTo).timeInMillis)
                    .apply()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onPause() {
        super.onPause()

        val activeLogin = dataStorage.activeUserData.userLogin

        val dateFrom = formatDateToIsoString(Date(preferences!!.getLong(settings_period_from.name + activeLogin!!, -1)))
        val dateTo = formatDateToIsoString(Date(preferences!!.getLong(settings_period_to.name + activeLogin, -1)))

        val settings = SettingsLegacy.builder()
                .periodFrom(dateFrom)
                .periodTo(dateTo)
                .build()

        presenter.updateSettings(settings)
    }

    override fun onAttach(context: Context) {
        AndroidInjection.inject(this)
        this.context = context
        super.onAttach(context)
    }

    override fun fragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    override fun showUpdateSuccess() {
        val snackbarWrapper = SnackbarWrapper.make(context!!,
                context!!.getText(R.string.settings_update_success), 3000)

        snackbarWrapper.setAction(context!!.getText(R.string.cancel)
        ) {
            Toast.makeText(context, "CANCEL!!!",
                    Toast.LENGTH_SHORT).show()
        }

        snackbarWrapper.show()
    }

    override fun showErrorMessage(msg: String) {

    }
}