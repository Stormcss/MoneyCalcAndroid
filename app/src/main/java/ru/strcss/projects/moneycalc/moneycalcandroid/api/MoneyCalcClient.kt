package ru.strcss.projects.moneycalc.moneycalcandroid.api

import retrofit2.Response
import retrofit2.http.*
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.settings.SpendingSectionUpdateContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.FinanceSummaryFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.*
import rx.Observable

interface MoneyCalcClient {
    @POST("/api/registration/register")
    fun registerPerson(@Body credentials: Credentials): Observable<MoneyCalcRs<Void>>

    @POST("/login")
    fun login(@Body access: Access): Observable<Response<Void>>

    @GET("/api/settings")
    fun getSettings(@Header("Authorization") token: String?): Observable<MoneyCalcRs<SettingsLegacy>>

    @PUT("/api/settings")
    fun updateSettings(@Header("Authorization") token: String?,
                       @Body settings: SettingsLegacy): Observable<MoneyCalcRs<SettingsLegacy>>

    /**
     * Spending Sections
     */

    @GET("/api/spendingSections")
    fun getSpendingSections(@Header("Authorization") token: String?): Observable<MoneyCalcRs<List<SpendingSection>>>

    @POST("/api/spendingSections")
    fun addSpendingSection(@Header("Authorization") token: String?,
                           @Body spendingSection: SpendingSection): Observable<MoneyCalcRs<List<SpendingSection>>>

    @PUT("/api/spendingSections")
    fun updateSpendingSection(@Header("Authorization") token: String?,
                              @Body updateContainer: SpendingSectionUpdateContainer): Observable<MoneyCalcRs<List<SpendingSection>>>

    @DELETE("/api/spendingSections/{sectionId}")
    fun deleteSpendingSection(@Header("Authorization") token: String?,
                              @Path("sectionId") sectionId: Int?): Observable<MoneyCalcRs<List<SpendingSection>>>

    /**
     * Statistics
     */
    @GET("/api/stats/summaryBySection")
    fun getFinanceSummaryBySection(@Header("Authorization") token: String?): Observable<MoneyCalcRs<List<FinanceSummaryBySection>>>

    @POST("/api/stats/summaryBySection/getFiltered")
    fun getFinanceSummaryBySection(@Header("Authorization") token: String?,
                                   @Body getContainer: FinanceSummaryFilterLegacy): Observable<MoneyCalcRs<List<FinanceSummaryBySection>>>

    /**
     * Transactions
     */
    @POST("/api/transactions")
    fun addTransaction(@Header("Authorization") token: String?,
                       @Body transaction: TransactionLegacy): Observable<MoneyCalcRs<TransactionLegacy>>

    @GET("/api/transactions")
    fun getTransactions(@Header("Authorization") token: String?): Observable<MoneyCalcRs<MutableList<TransactionLegacy>>>

    @POST("/api/transactions/getFiltered")
    fun getTransactions(@Header("Authorization") token: String?,
                        @Body container: TransactionsSearchFilterLegacy): Observable<MoneyCalcRs<MutableList<TransactionLegacy>>>

    @PUT("/api/transactions")
    fun updateTransaction(@Header("Authorization") token: String?,
                          @Body transactionUpdateContainer: TransactionUpdateContainerLegacy): Observable<MoneyCalcRs<TransactionLegacy>>

    @DELETE("/api/transactions/{transactionId}")
    fun deleteTransaction(@Header("Authorization") token: String?,
                          @Path("transactionId") transactionId: Int?): Observable<MoneyCalcRs<Void>>
}


