package ru.strcss.projects.moneycalc.moneycalcandroid.storage;

import java.util.List;

import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.Settings;

@Singleton
public class DataStorage {
    private Settings settings;
    private List<FinanceSummaryBySection> financeSummary;

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
}
