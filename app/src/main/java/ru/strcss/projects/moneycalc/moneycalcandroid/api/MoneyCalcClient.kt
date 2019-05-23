package ru.strcss.projects.moneycalc.moneycalcandroid.api

import retrofit2.Response
import retrofit2.http.*
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.settings.SpendingSectionUpdateContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.spendingsections.SpendingSectionsSearchRs
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.StatisticsFilter
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchLegacyRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.*
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDate
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDateSection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumBySection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SummaryBySection
import rx.Observable

interface MoneyCalcClient {
    @POST("/api/registration/register")
    fun registerPerson(@Body credentials: Credentials): Observable<Person>

    @POST("/login")
    fun login(@Body access: Access): Observable<Response<Void>>

    @GET("/api/settings")
    fun getSettings(@Header("Authorization") token: String?): Observable<SettingsLegacy>

    @PUT("/api/settings")
    fun updateSettings(@Header("Authorization") token: String?,
                       @Body settings: SettingsLegacy): Observable<SettingsLegacy>

    /**
     * Spending Sections
     */

    @GET("/api/spendingSections")
    fun getSpendingSections(@Header("Authorization") token: String?): Observable<SpendingSectionsSearchRs>

    @POST("/api/spendingSections")
    fun addSpendingSection(@Header("Authorization") token: String?,
                           @Body spendingSection: SpendingSection): Observable<SpendingSectionsSearchRs>

    @PUT("/api/spendingSections")
    fun updateSpendingSection(@Header("Authorization") token: String?,
                              @Body updateContainer: SpendingSectionUpdateContainer): Observable<SpendingSectionsSearchRs>

    @DELETE("/api/spendingSections/{sectionId}")
    fun deleteSpendingSection(@Header("Authorization") token: String?,
                              @Path("sectionId") sectionId: Int?): Observable<SpendingSectionsSearchRs>

    /**
     * Statistics By Section
     */
    @GET("/api/stats/bySection/summary")
    fun getStatsBySectionSummary(@Header("Authorization") token: String?): Observable<ItemsContainer<SummaryBySection>>

    @POST("/api/stats/bySection/sum")
    fun getStatsBySectionSum(@Header("Authorization") token: String?,
                             @Body getContainer: StatisticsFilter): Observable<ItemsContainer<SumBySection>>

    /**
     * Statistics By Date
     */
    @POST("/api/stats/byDate/sum")
    fun getStatsByDateSum(@Header("Authorization") token: String?,
                          @Body getContainer: StatisticsFilter): Observable<ItemsContainer<SumByDate>>

    @POST("/api/stats/byDate/sumBySection")
    fun getStatsByDateSumBySection(@Header("Authorization") token: String?,
                                   @Body getContainer: StatisticsFilter): Observable<ItemsContainer<SumByDateSection>>

    /**
     * Transactions
     */
    @GET("/api/transactions")
    fun getTransactions(@Header("Authorization") token: String?): Observable<TransactionsSearchLegacyRs>

    @POST("/api/transactions")
    fun addTransaction(@Header("Authorization") token: String?,
                       @Body transaction: TransactionLegacy): Observable<TransactionLegacy>

    @POST("/api/transactions/getFiltered")
    fun getTransactions(@Header("Authorization") token: String?,
                        @Body container: TransactionsSearchFilterLegacy): Observable<TransactionsSearchLegacyRs>

    @PUT("/api/transactions")
    fun updateTransaction(@Header("Authorization") token: String?,
                          @Body transactionUpdateContainer: TransactionUpdateContainerLegacy): Observable<TransactionLegacy>

    @DELETE("/api/transactions/{transactionId}")
    fun deleteTransaction(@Header("Authorization") token: String?,
                          @Path("transactionId") transactionId: Int?): Observable<Void>
}


