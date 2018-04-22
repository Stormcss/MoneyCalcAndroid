package ru.strcss.projects.moneycalc.moneycalcandroid.storage;

import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.enitities.Settings;

@Singleton
public class DataStorage {
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
