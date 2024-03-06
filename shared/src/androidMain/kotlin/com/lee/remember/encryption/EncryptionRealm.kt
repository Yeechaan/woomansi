package com.lee.remember.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.SecureRandom
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

actual class EncryptionRealm {

    companion object {
        private const val AES_MODE_M = "AES/GCM/NoPadding"
        private const val KEY_ALIAS = "realm_key_test_2"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    }

    private var keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE)

    init {
        keyStore.load(null)
    }

    actual fun isKeyExist(): Boolean = keyStore.containsAlias(KEY_ALIAS)

    actual fun createEncryptedRealmKey(): Pair<String, String> {
        generateEncryptKey()

        val realmKeyByteArray = generateRealmKey()
        val (iv, encryptedRealmKey) = encryptKey(realmKeyByteArray)
        // realmKey 암호화 후 메모리 초기화
        realmKeyByteArray.fill(0)

        return (iv to encryptedRealmKey)
    }

    actual fun getKey(iv: String, encrypted: String): ByteArray {
        return decryptKey(iv, encrypted)
    }


    private fun generateRealmKey(): ByteArray {
        val dbKey = ByteArray(64)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(dbKey)

        return dbKey
    }

    private fun generateEncryptKey() {
        try {
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
                val keyGenParameterSpecBuilder = KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )

                val keyGenParameter = keyGenParameterSpecBuilder
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()

                keyGenerator.init(keyGenParameter)
                keyGenerator.generateKey()
            }
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
    }

    private fun encryptKey(input: ByteArray): Pair<String, String> {
        return try {
            val aesKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
            val cipher: Cipher = Cipher.getInstance(AES_MODE_M)
            cipher.init(Cipher.ENCRYPT_MODE, aesKey)

            val encodedBytes: ByteArray = cipher.doFinal(input)
            Pair(Base64.encodeToString(cipher.iv, Base64.DEFAULT), Base64.encodeToString(encodedBytes, Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
            Pair("", "")
        }
    }

    private fun decryptKey(iv: String, encrypted: String): ByteArray {
        return try {
            val decodedValue = Base64.decode(encrypted.toByteArray(Charsets.UTF_8), Base64.DEFAULT)

            val aesKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
            val cipher: Cipher = Cipher.getInstance(AES_MODE_M)
            val parameterSpec = GCMParameterSpec(128, Base64.decode(iv, Base64.DEFAULT))
            cipher.init(Cipher.DECRYPT_MODE, aesKey, parameterSpec)

            val decryptedVal: ByteArray = cipher.doFinal(decodedValue)
            decryptedVal
        } catch (e: Exception) {
            e.printStackTrace()
            byteArrayOf()
        }
    }
}