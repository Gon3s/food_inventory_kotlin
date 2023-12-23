package com.gones.foodinventorykotlin.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


class KeychainManager<T : Any>(
    private val context: Context,
    private val clazz: Class<T>
) {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
    private val keyChain: String = "keyChain${clazz.simpleName}"

    init {
        keyStore.load(null)
    }

    internal inline fun <reified T : Any> saveDataToKeychain(data: T) {
        val (encryptedData, iv) = encryptData(data)
        Timber.d("DLOG : saveDataToKeychain: ${getKeychainKey(clazz)}")

        val sharedPreferences = context.getSharedPreferences(keyChain, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(getKeychainKey(clazz), Base64.encodeToString(encryptedData, Base64.DEFAULT))
            .putString(getKeychainKey(clazz) + "_iv", Base64.encodeToString(iv, Base64.DEFAULT))
            .apply()
    }

    internal inline fun <reified T : Any> getDataFromKeychain(): T? {
        val sharedPreferences = context.getSharedPreferences(keyChain, Context.MODE_PRIVATE)
        Timber.d("DLOG: getDataFromKeychain: ${getKeychainKey(clazz)}")

        val encryptedData =
            Base64.decode(sharedPreferences.getString(getKeychainKey(clazz), ""), Base64.DEFAULT)
        val iv = Base64.decode(
            sharedPreferences.getString(getKeychainKey(clazz) + "_iv", ""),
            Base64.DEFAULT
        )

        return if (encryptedData.isNotEmpty() && iv.isNotEmpty()) {
            decryptData(encryptedData, iv)
        } else {
            null
        }
    }

    internal inline fun <reified T : Any> removeDataFromKeychain() {
        val sharedPreferences = context.getSharedPreferences(keyChain, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .remove(getKeychainKey(clazz))
            .apply()
    }

    internal inline fun <reified T : Any> encryptData(data: T): Pair<ByteArray, ByteArray> {
        val cipher = getCipher()
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val dataString = Json.encodeToString<T>(data)
        val encryptedData = cipher.doFinal(dataString.toByteArray())

        return Pair(encryptedData, cipher.iv)
    }

    internal inline fun <reified T : Any> decryptData(encryptedData: ByteArray, iv: ByteArray): T? {
        val cipher = getCipher()
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)

        val decryptedData = cipher.doFinal(encryptedData)
        val dataString = String(decryptedData)

        return try {
            Json.decodeFromString<T>(dataString)
        } catch (e: Exception) {
            null
        }
    }


    private fun getCipher(): Cipher {
        return Cipher.getInstance("AES/GCM/NoPadding")
    }

    private fun getKey(): SecretKey {
        val keyAlias = getKeychainKey(clazz)
        if (!keyStore.containsAlias(keyAlias)) {
            generateKey(keyAlias)
        }
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    private fun generateKey(alias: String) {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getKeychainKey(clazz: Class<*>): String {
        return "${clazz.simpleName}_KeychainKey"
    }
}
