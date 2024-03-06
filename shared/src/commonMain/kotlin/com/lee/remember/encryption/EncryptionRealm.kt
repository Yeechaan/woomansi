package com.lee.remember.encryption

expect class EncryptionRealm() {
    fun isKeyExist(): Boolean
    fun createEncryptedRealmKey(): Pair<String, String>
    fun getKey(iv: String, encrypted: String): ByteArray
}