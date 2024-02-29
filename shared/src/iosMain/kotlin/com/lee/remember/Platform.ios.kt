package com.lee.remember

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override fun getSecretKey(): ByteArray {
        TODO("Not yet implemented")
    }
}

actual fun getPlatform(): Platform = IOSPlatform()