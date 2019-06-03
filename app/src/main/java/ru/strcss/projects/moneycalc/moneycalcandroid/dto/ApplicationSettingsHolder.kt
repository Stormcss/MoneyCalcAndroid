package ru.strcss.projects.moneycalc.moneycalcandroid.dto

import lombok.Data

@Data
class ApplicationSettingsHolder {
    var serverIP: String? = null
    //        set(serverIP) {
//            field = this.serverIP
//        }
    var token: String? = null
//        set(token) {
//            field = this.token
//        }
}
