package com.example.util

import java.security.MessageDigest

object SecurityUtils {
    fun hashPasscode(passcode: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(passcode.trim().toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun verifyPasscode(input: String, storedHash: String): Boolean {
        if (storedHash.isEmpty()) return false
        val inputHash = hashPasscode(input)
        return inputHash.equals(storedHash, ignoreCase = true)
    }

    // Default seed hash for initial setup (corresponds to default PIN "7007")
    // Leader Aryaveer can reset/change this anytime from settings in Admin Dashboard!
    val DEFAULT_LEADER_PIN_HASH: String = hashPasscode("7007")
}
