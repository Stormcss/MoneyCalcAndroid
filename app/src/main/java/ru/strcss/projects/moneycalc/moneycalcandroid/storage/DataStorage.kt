package ru.strcss.projects.moneycalc.moneycalcandroid.storage

import android.content.SharedPreferences
import lombok.Getter
import lombok.Setter
import ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_login
import ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_username
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.spendingsections.SpendingSectionsSearchRs
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchLegacyRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SummaryBySection
import javax.inject.Singleton

@Getter
@Setter
@Singleton
class DataStorage(val sharedPreferences: SharedPreferences) {
    val activeUserData = ActiveUserData()

    var settings: SettingsLegacy? = null
    var spendingSections: SpendingSectionsSearchRs? = null
    var statsBySectionSummary: ItemsContainer<SummaryBySection>? = null
    var transactions: TransactionsSearchLegacyRs? = null

    var transactionsFilter: TransactionsSearchFilterLegacy? = null

    init {
        fillUserDataFromSharedPreferences()
    }

    fun clearStorage() {
        this.settings = null
        this.spendingSections = null
        this.statsBySectionSummary = null
        this.transactions = null
        this.transactionsFilter = null
        this.activeUserData.clearData()
    }

    private fun fillUserDataFromSharedPreferences() {
        this.activeUserData.userLogin = sharedPreferences.getString(appl_storage_login.name, null)
        this.activeUserData.userName = sharedPreferences.getString(appl_storage_username.name, null)
    }

    inner class ActiveUserData {
        var userLogin: String? = null
            set(value) {
                field = value
                sharedPreferences.edit().putString(appl_storage_login.name, userLogin).apply()
            }
        var userName: String? = null
            set(value) {
                field = value
                sharedPreferences.edit().putString(appl_storage_username.name, userName).apply()
            }

        fun clearData() {
            this.userLogin = null
            this.userName = null
        }
    }
}
