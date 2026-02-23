package com.fabianopinto.pingheng.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveCredentials(apiKey: String, apiSecret: String) {
        sharedPreferences.edit()
            .putString("api_key", apiKey)
            .putString("api_secret", apiSecret)
            .apply()
    }

    fun getApiKey(): String = sharedPreferences.getString("api_key", "") ?: ""
    fun getApiSecret(): String = sharedPreferences.getString("api_secret", "") ?: ""
}
