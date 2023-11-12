package com.lee.remember

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform