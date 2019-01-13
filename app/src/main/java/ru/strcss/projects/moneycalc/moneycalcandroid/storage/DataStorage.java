package ru.strcss.projects.moneycalc.moneycalcandroid.storage;

import java.util.List;

import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy;

@Getter
@Setter
@Singleton
public class DataStorage {
    private SettingsLegacy settings;
    private List<SpendingSection> spendingSections;
    private List<FinanceSummaryBySection> financeSummary;
    private List<TransactionLegacy> transactionList;

    private TransactionsSearchFilterLegacy transactionsFilter;
}
