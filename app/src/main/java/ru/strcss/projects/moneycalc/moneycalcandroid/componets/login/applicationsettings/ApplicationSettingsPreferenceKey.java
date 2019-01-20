package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationSettingsPreferenceKey {
    appl_settings_server_ip("192.168.93.252"),
    appl_settings_server_port("8080"),
    appl_settings_token(null);

    private String defaultValue;
}
