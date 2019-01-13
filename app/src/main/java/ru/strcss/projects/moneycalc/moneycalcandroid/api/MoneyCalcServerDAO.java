package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import java.util.List;

import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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

@Singleton
public class MoneyCalcServerDAO {
    //    static final String BASE_URL = "http://62.181.41.23:8080";
    private static final String IP = "192.168.93.252";
    private static final String BASE_URL = "http://" + IP + ":8080";

    @Getter
    @Setter
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

    public Observable<MoneyCalcRs<Void>> registerPerson(Credentials credentials) {
        return client.registerPerson(credentials);
    }


    public Observable<Response<Void>> login(Access access) {
        return client.login(access);
    }


    public Observable<MoneyCalcRs<SettingsLegacy>> getSettings() {
        return client.getSettings(token);
    }

    public Observable<MoneyCalcRs<SettingsLegacy>> updateSettings(SettingsLegacy settings) {
        return client.updateSettings(token, settings);
    }


    public Observable<MoneyCalcRs<List<SpendingSection>>> getSpendingSections() {
        return client.getSpendingSections(token);
    }


    public Observable<MoneyCalcRs<List<SpendingSection>>> addSpendingSection(SpendingSection spendingSection) {
        return client.addSpendingSection(token, spendingSection);
    }


    public Observable<MoneyCalcRs<List<SpendingSection>>> updateSpendingSection(SpendingSectionUpdateContainer updateContainer) {
        return client.updateSpendingSection(token, updateContainer);
    }


    public Observable<MoneyCalcRs<List<SpendingSection>>> deleteSpendingSection(Integer deleteId) {
        return client.deleteSpendingSection(token, deleteId);
    }


    public Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection() {
        return client.getFinanceSummaryBySection(token);
    }


    public Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(FinanceSummaryFilterLegacy getContainer) {
        return client.getFinanceSummaryBySection(token, getContainer);
    }


    public Observable<MoneyCalcRs<TransactionLegacy>> addTransaction(TransactionLegacy transaction) {
        return client.addTransaction(token, transaction);
    }


    public Observable<MoneyCalcRs<List<TransactionLegacy>>> getTransactions() {
        return client.getTransactions(token);
    }

    public Observable<MoneyCalcRs<List<TransactionLegacy>>> getTransactions(TransactionsSearchFilterLegacy container) {
        return client.getTransactions(token, container);
    }


    public Observable<MoneyCalcRs<Void>> deleteTransaction(Integer transactionId) {
        return client.deleteTransaction(token, transactionId);
    }


    public Observable<MoneyCalcRs<TransactionLegacy>> updateTransaction(TransactionUpdateContainerLegacy transactionUpdateContainer) {
        return client.updateTransaction(token, transactionUpdateContainer);
    }
}