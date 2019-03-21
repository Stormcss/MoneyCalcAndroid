package ru.strcss.projects.moneycalc.moneycalcandroid

import lombok.Getter

/**
 * Preference keys for values which represent data for active user
 */
@Getter
enum class ApplicationStoragePreferenceKey {
    appl_storage_login,
    appl_storage_username,
    appl_storage_token
}
