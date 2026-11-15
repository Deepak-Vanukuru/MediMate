package uk.ac.tees.mad.s3533898.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.s3533898.domain.model.UserPreferences
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "medimate_user")

/** Reads/writes the local account to Preferences DataStore. */
@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val HAS_ACCOUNT = booleanPreferencesKey("has_account")
        val DISPLAY_NAME = stringPreferencesKey("display_name")
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val PIN_SALT = stringPreferencesKey("pin_salt")
        val BIOMETRIC = booleanPreferencesKey("biometric_enabled")
    }

    val preferences: Flow<UserPreferences> = context.dataStore.data.map { p ->
        UserPreferences(
            hasAccount = p[Keys.HAS_ACCOUNT] ?: false,
            displayName = p[Keys.DISPLAY_NAME] ?: "",
            pinHash = p[Keys.PIN_HASH] ?: "",
            pinSalt = p[Keys.PIN_SALT] ?: "",
            biometricEnabled = p[Keys.BIOMETRIC] ?: false
        )
    }

    suspend fun saveAccount(
        displayName: String,
        pinHash: String,
        pinSalt: String,
        biometricEnabled: Boolean
    ) {
        context.dataStore.edit { p ->
            p[Keys.HAS_ACCOUNT] = true
            p[Keys.DISPLAY_NAME] = displayName
            p[Keys.PIN_HASH] = pinHash
            p[Keys.PIN_SALT] = pinSalt
            p[Keys.BIOMETRIC] = biometricEnabled
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.BIOMETRIC] = enabled }
    }

    /** GDPR erasure: wipe the local account. */
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
