package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionAddContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionDeleteContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionUpdateContainer;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.Settings;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.Transaction;
import rx.Observable;

public interface MoneyCalcClient {
    @POST("/api/registration/register")
    Observable<AjaxRs<Void>> registerPerson(@Body Credentials credentials);

    @POST("/login")
    Observable<Response<Void>> login(@Body Access access);

    @GET("/api/settings/getSettings")
    Observable<AjaxRs<Settings>> getSettings(@Header("Authorization") String token);

    @GET("/api/settings/getSpendingSections")
    Observable<AjaxRs<List<SpendingSection>>> getSpendingSections(@Header("Authorization") String token);

    /**
     * Statistics
     */
    @POST("/api/statistics/financeSummary/getFinanceSummaryBySection")
    Observable<AjaxRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(@Header("Authorization") String token,
                                                                                 @Body FinanceSummaryGetContainer getContainer);

    /**
     * Transactions
     */
    @POST("/api/finance/transactions/addTransaction")
    Observable<AjaxRs<Transaction>> addTransaction(@Header("Authorization") String token,
                                                   @Body TransactionAddContainer transactionContainer);

    @POST("/api/finance/transactions/deleteTransaction")
    Observable<AjaxRs<Void>> deleteTransaction(@Header("Authorization") String token,
                                               @Body TransactionDeleteContainer transactionContainer);

    @POST("/api/finance/transactions/updateTransaction")
    Observable<AjaxRs<Transaction>> updateTransaction(@Header("Authorization") String token,
                                                      @Body TransactionUpdateContainer transactionUpdateContainer);
}


