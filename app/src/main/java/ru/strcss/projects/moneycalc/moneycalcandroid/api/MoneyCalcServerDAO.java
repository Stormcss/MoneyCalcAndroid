package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import java.util.List;

import javax.inject.Singleton;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.dto.MoneyCalcRs;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionAddContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionDeleteContainer;
import ru.strcss.projects.moneycalc.dto.crudcontainers.settings.SpendingSectionUpdateContainer;
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

@Singleton
public class MoneyCalcServerDAO implements MoneyCalcClient {
    static final String BASE_URL = "http://62.181.41.23:8080";
//    static final String BASE_URL = "http://192.168.1.100:8080";

    private String token;
    private MoneyCalcClient client;

    public MoneyCalcServerDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        client = retrofit.create(MoneyCalcClient.class);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Observable<MoneyCalcRs<Void>> registerPerson(Credentials credentials) {
        return client.registerPerson(credentials);
    }

    @Override
    public Observable<Response<Void>> login(Access access) {
        return client.login(access);
    }

    @Override
    public Observable<MoneyCalcRs<Settings>> getSettings(String token) {
        return client.getSettings(token);
    }

    @Override
    public Observable<MoneyCalcRs<List<SpendingSection>>> getSpendingSections(String token) {
        return client.getSpendingSections(token);
    }

    @Override
    public Observable<MoneyCalcRs<List<SpendingSection>>> addSpendingSection(String token, SpendingSectionAddContainer spendingSectionContainer) {
        return client.addSpendingSection(token, spendingSectionContainer);
    }

    @Override
    public Observable<MoneyCalcRs<List<SpendingSection>>> updateSpendingSection(String token, SpendingSectionUpdateContainer updateContainer) {
        return client.updateSpendingSection(token, updateContainer);
    }

    @Override
    public Observable<MoneyCalcRs<List<SpendingSection>>> deleteSpendingSection(String token, SpendingSectionDeleteContainer deleteContainer) {
        return client.deleteSpendingSection(token, deleteContainer);
    }

    @Override
    public Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(String token, FinanceSummaryGetContainer getContainer) {
        return client.getFinanceSummaryBySection(token, getContainer);
    }

    @Override
    public Observable<MoneyCalcRs<Transaction>> addTransaction(String token, TransactionAddContainer transactionContainer) {
        return client.addTransaction(token, transactionContainer);
    }

    @Override
    public Observable<MoneyCalcRs<List<Transaction>>> getTransactions(String token, TransactionsSearchContainer container) {
        return client.getTransactions(token, container);
    }

    @Override
    public Observable<MoneyCalcRs<Void>> deleteTransaction(String token, TransactionDeleteContainer transactionContainer) {
        return client.deleteTransaction(token, transactionContainer);
    }

    @Override
    public Observable<MoneyCalcRs<Transaction>> updateTransaction(String token, TransactionUpdateContainer transactionUpdateContainer) {
        return client.updateTransaction(token, transactionUpdateContainer);
    }
}