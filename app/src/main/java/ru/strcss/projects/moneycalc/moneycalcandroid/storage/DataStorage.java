package ru.strcss.projects.moneycalc.moneycalcandroid.storage;

import android.content.SharedPreferences;

import java.util.List;

import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy;

import static ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_login;
import static ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_username;

@Getter
@Setter
@Singleton
public class DataStorage {
    private final SharedPreferences sharedPreferences;
    private final ActiveUserData activeUserData = new ActiveUserData();

    private SettingsLegacy settings;
    private List<SpendingSection> spendingSections;
    private List<FinanceSummaryBySection> financeSummary;
    private List<TransactionLegacy> transactionList;

    private TransactionsSearchFilterLegacy transactionsFilter;

    public DataStorage(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        fillUserDataFromSharedPreferences();
    }

    public void clearStorage() {
        this.settings = null;
        this.spendingSections = null;
        this.financeSummary = null;
        this.transactionList = null;
        this.transactionsFilter = null;
        this.activeUserData.clearData();
    }

    private void fillUserDataFromSharedPreferences(){
        this.activeUserData.userLogin = sharedPreferences.getString(appl_storage_login.name(), null);
        this.activeUserData.userName = sharedPreferences.getString(appl_storage_username.name(), null);
    }

    @Getter
    public class ActiveUserData {
        private String userLogin;
        private String userName;

        public void setUserLogin(String userLogin) {
            this.userLogin = userLogin;
            sharedPreferences.edit().putString(appl_storage_login.name(), userLogin).apply();
        }

        public void setUserName(String userName) {
            this.userName = userName;
            sharedPreferences.edit().putString(appl_storage_username.name(), userName).apply();
        }

        public void clearData(){
            this.userLogin = null;
            this.userName = null;
        }
    }
}
