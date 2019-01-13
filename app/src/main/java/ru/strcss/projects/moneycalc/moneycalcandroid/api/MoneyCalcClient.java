package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.settings.SpendingSectionUpdateContainer;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.FinanceSummaryFilterLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.Access;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy;
import rx.Observable;

public interface MoneyCalcClient {
    @POST("/api/registration/register")
    Observable<MoneyCalcRs<Void>> registerPerson(@Body Credentials credentials);

    @POST("/login")
    Observable<Response<Void>> login(@Body Access access);

    @GET("/api/settings")
    Observable<MoneyCalcRs<SettingsLegacy>> getSettings(@Header("Authorization") String token);

    @PUT("/api/settings")
    Observable<MoneyCalcRs<SettingsLegacy>> updateSettings(@Header("Authorization") String token,
                                                           @Body SettingsLegacy settings);

    /**
     * Spending Sections
     */

    @GET("/api/spendingSections")
    Observable<MoneyCalcRs<List<SpendingSection>>> getSpendingSections(@Header("Authorization") String token);

    @POST("/api/spendingSections")
    Observable<MoneyCalcRs<List<SpendingSection>>> addSpendingSection(@Header("Authorization") String token,
                                                                      @Body SpendingSection spendingSection);

    @PUT("/api/spendingSections")
    Observable<MoneyCalcRs<List<SpendingSection>>> updateSpendingSection(@Header("Authorization") String token,
                                                                         @Body SpendingSectionUpdateContainer updateContainer);

    @DELETE("/api/spendingSections/{sectionId}")
    Observable<MoneyCalcRs<List<SpendingSection>>> deleteSpendingSection(@Header("Authorization") String token,
                                                                         Integer sectionId);

    /**
     * Statistics
     */
    @GET("/api/stats/summaryBySection")
    Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(@Header("Authorization") String token);

    @POST("/api/stats/summaryBySection/getFiltered")
    Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(@Header("Authorization") String token,
                                                                                      @Body FinanceSummaryFilterLegacy getContainer);

    /**
     * Transactions
     */
    @POST("/api/transactions")
    Observable<MoneyCalcRs<TransactionLegacy>> addTransaction(@Header("Authorization") String token,
                                                              @Body TransactionLegacy transaction);

    @GET("/api/transactions")
    Observable<MoneyCalcRs<List<TransactionLegacy>>> getTransactions(@Header("Authorization") String token);

    @POST("/api/transactions/getFiltered")
    Observable<MoneyCalcRs<List<TransactionLegacy>>> getTransactions(@Header("Authorization") String token,
                                                                     @Body TransactionsSearchFilterLegacy container);

    @PUT("/api/transactions")
    Observable<MoneyCalcRs<TransactionLegacy>> updateTransaction(@Header("Authorization") String token,
                                                                 @Body TransactionUpdateContainerLegacy transactionUpdateContainer);

    @DELETE("/api/transactions/{transactionId}")
    Observable<MoneyCalcRs<Void>> deleteTransaction(@Header("Authorization") String token,
                                                    Integer transactionId);
}


