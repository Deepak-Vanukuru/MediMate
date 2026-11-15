package uk.ac.tees.mad.s3533898.domain.model

/** Local account + settings. No personal data ever leaves the device. */
data class UserPreferences(
    val hasAccount: Boolean = false,
    val displayName: String = "",
    val pinHash: String = "",
    val pinSalt: String = "",
    val biometricEnabled: Boolean = false
)
