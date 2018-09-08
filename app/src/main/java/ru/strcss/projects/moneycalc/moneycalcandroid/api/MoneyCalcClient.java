package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SettingsUpdateContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionAddContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionDeleteContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionUpdateContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainerLegacy;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionAddContainerLegacy;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionDeleteContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionsSearchContainerLegacy;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.SettingsLegacy;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;
import rx.Observable;

public interface MoneyCalcClient {
    @POST("/api/registration/register")
    Observable<MoneyCalcRs<Void>> registerPerson(@Body Credentials credentials);

    @POST("/login")
    Observable<Response<Void>> login(@Body Access access);

    @GET("/api/settings/get")
    Observable<MoneyCalcRs<SettingsLegacy>> getSettings(@Header("Authorization") String token);

    @POST("/api/settings/update")
    Observable<MoneyCalcRs<SettingsLegacy>> updateSettings(@Header("Authorization") String token,
                                                           @Body SettingsUpdateContainer updateContainer);

    /**
     * Spending Sections
     */

    @GET("/api/settings/spendingSection/get")
    Observable<MoneyCalcRs<List<SpendingSection>>> getSpendingSections(@Header("Authorization") String token);

    @POST("/api/settings/spendingSection/add")
    Observable<MoneyCalcRs<List<SpendingSection>>> addSpendingSection(@Header("Authorization") String token,
                                                                      @Body SpendingSectionAddContainer spendingSectionContainer);

    @POST("/api/settings/spendingSection/update")
    Observable<MoneyCalcRs<List<SpendingSection>>> updateSpendingSection(@Header("Authorization") String token,
                                                                         @Body SpendingSectionUpdateContainer updateContainer);

    @POST("/api/settings/spendingSection/delete")
    Observable<MoneyCalcRs<List<SpendingSection>>> deleteSpendingSection(@Header("Authorization") String token,
                                                                         @Body SpendingSectionDeleteContainer deleteContainer);

    /**
     * Statistics
     */
    @GET("/api/stats/summaryBySection/get")
    Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(@Header("Authorization") String token);

    @POST("/api/stats/summaryBySection/getFiltered")
    Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(@Header("Authorization") String token,
                                                                                      @Body FinanceSummaryGetContainerLegacy getContainer);

    /**
     * Transactions
     */
    @POST("/api/transactions/add")
    Observable<MoneyCalcRs<TransactionLegacy>> addTransaction(@Header("Authorization") String token,
                                                              @Body TransactionAddContainerLegacy transactionContainer);

    @GET("/api/transactions/get")
    Observable<MoneyCalcRs<List<TransactionLegacy>>> getTransactions(@Header("Authorization") String token);

    @POST("/api/transactions/getFiltered")
    Observable<MoneyCalcRs<List<TransactionLegacy>>> getTransactions(@Header("Authorization") String token,
                                                                     @Body TransactionsSearchContainerLegacy container);

    @POST("/api/transactions/update")
    Observable<MoneyCalcRs<TransactionLegacy>> updateTransaction(@Header("Authorization") String token,
                                                                 @Body TransactionUpdateContainerLegacy transactionUpdateContainer);

    @POST("/api/transactions/delete")
    Observable<MoneyCalcRs<Void>> deleteTransaction(@Header("Authorization") String token,
                                                    @Body TransactionDeleteContainer transactionContainer);
}


