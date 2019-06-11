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
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.settings.SpendingSectionUpdateContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.spendingsections.SpendingSectionsSearchRs
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.StatisticsFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchLegacyRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.*
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDateLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDateSectionLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumBySection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SummaryBySection
import rx.Observable
import javax.inject.Singleton

@Singleton
class MoneyCalcServerDAO(private val sharedPreferences: SharedPreferences) {
    var token: String? = null
        set(value) {
            field = value
            sharedPreferences.edit().putString(appl_storage_token.name, token).apply()
        }
    lateinit var client: MoneyCalcClient

    val settings: Observable<SettingsLegacy>
        get() = client.getSettings(token)


    val spendingSections: Observable<SpendingSectionsSearchRs>
        get() = client.getSpendingSections(token)

    val transactions: Observable<TransactionsSearchLegacyRs>
        get() = client.getTransactions(token)

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

    fun registerPerson(credentials: Credentials): Observable<Person> {
        return client.registerPerson(credentials)
    }

    fun login(access: Access): Observable<Response<Void>> {
        return client.login(access)
    }

    fun updateSettings(settings: SettingsLegacy): Observable<SettingsLegacy> {
        return client.updateSettings(token, settings)
    }

    fun addSpendingSection(spendingSection: SpendingSection): Observable<SpendingSectionsSearchRs> {
        return client.addSpendingSection(token, spendingSection)
    }

    fun updateSpendingSection(updateContainer: SpendingSectionUpdateContainer): Observable<SpendingSectionsSearchRs> {
        return client.updateSpendingSection(token, updateContainer)
    }

    fun deleteSpendingSection(deleteId: Int?): Observable<SpendingSectionsSearchRs> {
        return client.deleteSpendingSection(token, deleteId)
    }

    fun getStatsBySectionSummary(): Observable<ItemsContainer<SummaryBySection>> {
        return client.getStatsBySectionSummary(token)
    }

    fun getStatsBySectionSum(statisticsFilter: StatisticsFilterLegacy): Observable<ItemsContainer<SumBySection>> {
        return client.getStatsBySectionSum(token, statisticsFilter)
    }

    fun getStatsByDateSum(statisticsFilter: StatisticsFilterLegacy): Observable<ItemsContainer<SumByDateLegacy>> {
        return client.getStatsByDateSum(token, statisticsFilter)
    }

    fun getStatsByDateSection(statisticsFilter: StatisticsFilterLegacy): Observable<ItemsContainer<SumByDateSectionLegacy>> {
        return client.getStatsByDateSumBySection(token, statisticsFilter)
    }

    fun addTransaction(transaction: TransactionLegacy): Observable<TransactionLegacy> {
        return client.addTransaction(token, transaction)
    }

    fun getTransactions(container: TransactionsSearchFilterLegacy): Observable<TransactionsSearchLegacyRs> {
        return client.getTransactions(token, container)
    }

    fun getTransactionsFiltered(container: TransactionsSearchFilterLegacy): Observable<TransactionsSearchLegacyRs> {
        return client.getTransactionsFiltered(token, container)
    }

    fun deleteTransaction(transactionId: Int?): Observable<Void> {
        return client.deleteTransaction(token, transactionId)
    }


    fun updateTransaction(transactionUpdateContainer: TransactionUpdateContainerLegacy): Observable<TransactionLegacy> {
        return client.updateTransaction(token, transactionUpdateContainer)
    }
}