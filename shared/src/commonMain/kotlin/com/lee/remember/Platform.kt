package com.lee.remember

interface Platform {
    val name: String

    fun getSecretKey(): ByteArray
}

expect fun getPlatform(): Platform