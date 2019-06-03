package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings

//@Getter
//@AllArgsConstructor
enum class ApplicationSettingsPreferenceKey(var defaultValue: String) {
    appl_settings_server_ip("192.168.93.252"),
    appl_settings_server_port("8080");
//    val defaultValue: String? = null
}
