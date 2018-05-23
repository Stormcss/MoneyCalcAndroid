package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.statistics.FinanceSummaryGetContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionAddContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionDeleteContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionUpdateContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionsSearchContainer;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.Settings;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.Transaction;
import rx.Observable;

public interface MoneyCalcClient {
    @POST("/api/registration/register")
    Observable<MoneyCalcRs<Void>> registerPerson(@Body Credentials credentials);

    @POST("/login")
    Observable<Response<Void>> login(@Body Access access);

    @GET("/api/settings/getSettings")
    Observable<MoneyCalcRs<Settings>> getSettings(@Header("Authorization") String token);

    @GET("/api/settings/getSpendingSections")
    Observable<MoneyCalcRs<List<SpendingSection>>> getSpendingSections(@Header("Authorization") String token);

    /**
     * Statistics
     */
    @POST("/api/statistics/financeSummary/getFinanceSummaryBySection")
    Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(@Header("Authorization") String token,
                                                                                      @Body FinanceSummaryGetContainer getContainer);

    /**
     * Transactions
     */
    @POST("/api/finance/transactions/addTransaction")
    Observable<MoneyCalcRs<Transaction>> addTransaction(@Header("Authorization") String token,
                                                        @Body TransactionAddContainer transactionContainer);

    @POST("/api/finance/transactions/getTransactions")
    Observable<MoneyCalcRs<List<Transaction>>> getTransactions(@Header("Authorization") String token,
                                                               @Body TransactionsSearchContainer container);

    @POST("/api/finance/transactions/updateTransaction")
    Observable<MoneyCalcRs<Transaction>> updateTransaction(@Header("Authorization") String token,
                                                           @Body TransactionUpdateContainer transactionUpdateContainer);

    @POST("/api/finance/transactions/deleteTransaction")
    Observable<MoneyCalcRs<Void>> deleteTransaction(@Header("Authorization") String token,
                                                    @Body TransactionDeleteContainer transactionContainer);
}


