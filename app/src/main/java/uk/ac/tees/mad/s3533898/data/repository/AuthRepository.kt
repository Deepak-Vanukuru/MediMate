package uk.ac.tees.mad.s3533898.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import uk.ac.tees.mad.s3533898.core.security.CryptoManager
import uk.ac.tees.mad.s3533898.data.local.datastore.UserPreferencesDataSource
import uk.ac.tees.mad.s3533898.domain.model.UserPreferences
import javax.inject.Inject
import javax.inject.Singleton

/** Single entry point for local-account auth (register, unlock, wipe). */
@Singleton
class AuthRepository @Inject constructor(
    private val dataSource: UserPreferencesDataSource,
    private val crypto: CryptoManager
) {
    val userPreferences: Flow<UserPreferences> = dataSource.preferences

    suspend fun register(displayName: String, pin: String, biometricEnabled: Boolean) {
        val salt = crypto.generateSalt()
        val hash = crypto.hashPin(pin, salt)
        dataSource.saveAccount(displayName.trim(), hash, salt, biometricEnabled)
    }

    suspend fun verifyPin(pin: String): Boolean {
        val prefs = dataSource.preferences.first()
        if (!prefs.hasAccount) return false
        return crypto.verifyPin(pin, prefs.pinSalt, prefs.pinHash)
    }

    suspend fun setBiometricEnabled(enabled: Boolean) = dataSource.setBiometricEnabled(enabled)

    suspend fun clearAccount() = dataSource.clearAll()
}
