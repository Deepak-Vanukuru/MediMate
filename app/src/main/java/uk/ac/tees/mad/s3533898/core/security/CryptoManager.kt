package uk.ac.tees.mad.s3533898.core.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Hashes and verifies the user's unlock PIN.
 *
 * Security decision: the PIN is NEVER stored in plain text. We derive a hash
 * with PBKDF2 (HMAC-SHA256, 120k iterations) over a per-user random salt, so a
 * leaked DataStore file does not reveal the PIN and brute-forcing is slow.
 */
@Singleton
class CryptoManager @Inject constructor() {

    fun generateSalt(): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return Base64.encodeToString(salt, Base64.NO_WRAP)
    }

    fun hashPin(pin: String, saltBase64: String): String {
        val salt = Base64.decode(saltBase64, Base64.NO_WRAP)
        val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }

    /** Constant-time comparison to avoid timing attacks. */
    fun verifyPin(pin: String, saltBase64: String, expectedHash: String): Boolean {
        val computed = hashPin(pin, saltBase64)
        return constantTimeEquals(computed, expectedHash)
    }

    private fun constantTimeEquals(a: String, b: String): Boolean {
        val x = a.toByteArray()
        val y = b.toByteArray()
        if (x.size != y.size) return false
        var result = 0
        for (i in x.indices) result = result or (x[i].toInt() xor y[i].toInt())
        return result == 0
    }

    private companion object {
        const val ITERATIONS = 120_000
        const val KEY_LENGTH = 256
    }
}
