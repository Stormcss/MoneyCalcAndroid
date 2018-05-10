package ru.strcss.projects.moneycalc.moneycalcandroid.storage;

import java.util.List;

import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.Settings;
import ru.strcss.projects.moneycalc.enitities.Transaction;

@Singleton
public class DataStorage {
    private Settings settings;
    private List<FinanceSummaryBySection> financeSummary;
    private List<Transaction> transactionList;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<FinanceSummaryBySection> getFinanceSummary() {
        return financeSummary;
    }

    public void setFinanceSummary(List<FinanceSummaryBySection> financeSummary) {
        this.financeSummary = financeSummary;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
