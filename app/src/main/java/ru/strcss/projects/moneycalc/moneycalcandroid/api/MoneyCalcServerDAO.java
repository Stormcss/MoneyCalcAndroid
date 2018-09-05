package ru.strcss.projects.moneycalc.moneycalcandroid.api;

import java.util.List;

import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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

@Singleton
public class MoneyCalcServerDAO {
    //    static final String BASE_URL = "http://62.181.41.23:8080";
    private static final String IP = "192.168.93.253";
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

    public Observable<MoneyCalcRs<SettingsLegacy>> updateSettings(SettingsUpdateContainer updateContainer) {
        return client.updateSettings(token, updateContainer);
    }


    public Observable<MoneyCalcRs<List<SpendingSection>>> getSpendingSections() {
        return client.getSpendingSections(token);
    }


    public Observable<MoneyCalcRs<List<SpendingSection>>> addSpendingSection(SpendingSectionAddContainer spendingSectionContainer) {
        return client.addSpendingSection(token, spendingSectionContainer);
    }


    public Observable<MoneyCalcRs<List<SpendingSection>>> updateSpendingSection(SpendingSectionUpdateContainer updateContainer) {
        return client.updateSpendingSection(token, updateContainer);
    }


    public Observable<MoneyCalcRs<List<SpendingSection>>> deleteSpendingSection(SpendingSectionDeleteContainer deleteContainer) {
        return client.deleteSpendingSection(token, deleteContainer);
    }


    public Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection() {
        return client.getFinanceSummaryBySection(token);
    }


    public Observable<MoneyCalcRs<List<FinanceSummaryBySection>>> getFinanceSummaryBySection(FinanceSummaryGetContainerLegacy getContainer) {
        return client.getFinanceSummaryBySection(token, getContainer);
    }


    public Observable<MoneyCalcRs<TransactionLegacy>> addTransaction(TransactionAddContainerLegacy transactionContainer) {
        return client.addTransaction(token, transactionContainer);
    }


    public Observable<MoneyCalcRs<List<TransactionLegacy>>> getTransactions() {
        return client.getTransactions(token);
    }

    public Observable<MoneyCalcRs<List<TransactionLegacy>>> getTransactions(TransactionsSearchContainerLegacy container) {
        return client.getTransactions(token, container);
    }


    public Observable<MoneyCalcRs<Void>> deleteTransaction(TransactionDeleteContainer transactionContainer) {
        return client.deleteTransaction(token, transactionContainer);
    }


    public Observable<MoneyCalcRs<TransactionLegacy>> updateTransaction(TransactionUpdateContainerLegacy transactionUpdateContainer) {
        return client.updateTransaction(token, transactionUpdateContainer);
    }
}