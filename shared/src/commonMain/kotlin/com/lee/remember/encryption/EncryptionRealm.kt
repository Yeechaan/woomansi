package com.lee.remember.encryption

expect class EncryptionRealm() {
    fun isKeyExist(): Boolean
    fun createEncryptedRealmKey(): Pair<String, String>
    fun getKey(publicIv: String, encrypted: String): ByteArray
}