package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import java.util.List;

import javax.inject.Singleton;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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

@Singleton
public class MoneyCalcServerDAO implements MoneyCalcClient {
    static final String BASE_URL = "http://192.168.1.101:8080";

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

    @Override
    public Observable<AjaxRs<Void>> registerPerson(Credentials credentials) {
        return client.registerPerson(credentials);
    }

    @Override
    public Observable<Response<Void>> login(Access access) {
        return client.login(access);
    }

    @Override
    public Observable<AjaxRs<Settings>> getSettings(String token) {
        return client.getSettings(token);
    }

    @Override
    public Observable<AjaxRs<List<SpendingSection>>> getSpendingSections(String token) {
        return client.getSpendingSections(token);
    }

    @Override
    public Observable<AjaxRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(String token, FinanceSummaryGetContainer getContainer) {
        return client.getFinanceSummaryBySection(token, getContainer);
    }

    @Override
    public Observable<AjaxRs<Transaction>> addTransaction(String token, TransactionAddContainer transactionContainer) {
        return client.addTransaction(token, transactionContainer);
    }

    @Override
    public Observable<AjaxRs<Void>> deleteTransaction(String token, TransactionDeleteContainer transactionContainer) {
        return client.deleteTransaction(token, transactionContainer);
    }

    @Override
    public Observable<AjaxRs<Transaction>> updateTransaction(String token, TransactionUpdateContainer transactionUpdateContainer) {
        return client.updateTransaction(token, transactionUpdateContainer);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
