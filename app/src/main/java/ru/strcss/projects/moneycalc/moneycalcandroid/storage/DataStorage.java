package ru.strcss.projects.moneycalc.moneycalcandroid.storage;

import java.util.List;

import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.SettingsLegacy;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.TransactionLegacy;

@Getter
@Setter
@Singleton
public class DataStorage {
    private SettingsLegacy settings;
    private List<SpendingSection> spendingSections;
    private List<FinanceSummaryBySection> financeSummary;
    private List<TransactionLegacy> transactionList;
}
