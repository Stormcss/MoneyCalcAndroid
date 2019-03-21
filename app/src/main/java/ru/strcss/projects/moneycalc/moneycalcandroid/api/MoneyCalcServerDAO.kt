package ru.strcss.projects.moneycalc.moneycalcandroid.api

import android.content.SharedPreferences
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_token
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsPreferenceKey.appl_settings_server_ip
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsPreferenceKey.appl_settings_server_port
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.settings.SpendingSectionUpdateContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.FinanceSummaryFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.*
import rx.Observable
import javax.inject.Singleton

@Singleton
class MoneyCalcServerDAO(private val sharedPreferences: SharedPreferences) {
    var token: String? = null
        set(value) {
            field = value
            sharedPreferences.edit().putString(appl_storage_token.name, token).apply()
        }
    var client: MoneyCalcClient? = null

    val settings: Observable<MoneyCalcRs<SettingsLegacy>>
        get() = client!!.getSettings(token)


    val spendingSections: Observable<MoneyCalcRs<List<SpendingSection>>>
        get() = client!!.getSpendingSections(token)


    val financeSummaryBySection: Observable<MoneyCalcRs<List<FinanceSummaryBySection>>>
        get() = client!!.getFinanceSummaryBySection(token)


    val transactions: Observable<MoneyCalcRs<List<TransactionLegacy>>>
        get() = client!!.getTransactions(token)

    init {
        val serverIp = sharedPreferences.getString(appl_settings_server_ip.name, null)
        val serverPort = sharedPreferences.getString(appl_settings_server_port.name, null)
        token = sharedPreferences.getString(appl_storage_token.name, null)
        saveServerIp(serverIp, serverPort)
    }

    fun saveServerIp(serverIp: String?, serverPort: String?) {
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(String.format("http://%s:%s", serverIp, serverPort))
                .build()
        client = retrofit.create(MoneyCalcClient::class.java)
    }

//    fun setToken(token: String) {
//        this.token = token
//        sharedPreferences.edit().putString(appl_storage_token.name, token).apply()
//    }

    fun registerPerson(credentials: Credentials): Observable<MoneyCalcRs<Void>> {
        return client!!.registerPerson(credentials)
    }

    fun login(access: Access): Observable<Response<Void>> {
        return client!!.login(access)
    }

    fun updateSettings(settings: SettingsLegacy): Observable<MoneyCalcRs<SettingsLegacy>> {
        return client!!.updateSettings(token, settings)
    }


    fun addSpendingSection(spendingSection: SpendingSection): Observable<MoneyCalcRs<List<SpendingSection>>> {
        return client!!.addSpendingSection(token, spendingSection)
    }


    fun updateSpendingSection(updateContainer: SpendingSectionUpdateContainer): Observable<MoneyCalcRs<List<SpendingSection>>> {
        return client!!.updateSpendingSection(token, updateContainer)
    }


    fun deleteSpendingSection(deleteId: Int?): Observable<MoneyCalcRs<List<SpendingSection>>> {
        return client!!.deleteSpendingSection(token, deleteId)
    }


    fun getFinanceSummaryBySection(getContainer: FinanceSummaryFilterLegacy): Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> {
        return client!!.getFinanceSummaryBySection(token, getContainer)
    }


    fun addTransaction(transaction: TransactionLegacy): Observable<MoneyCalcRs<TransactionLegacy>> {
        return client!!.addTransaction(token, transaction)
    }

    fun getTransactions(container: TransactionsSearchFilterLegacy): Observable<MoneyCalcRs<List<TransactionLegacy>>> {
        return client!!.getTransactions(token, container)
    }


    fun deleteTransaction(transactionId: Int?): Observable<MoneyCalcRs<Void>> {
        return client!!.deleteTransaction(token, transactionId)
    }


    fun updateTransaction(transactionUpdateContainer: TransactionUpdateContainerLegacy): Observable<MoneyCalcRs<TransactionLegacy>> {
        return client!!.updateTransaction(token, transactionUpdateContainer)
    }
}