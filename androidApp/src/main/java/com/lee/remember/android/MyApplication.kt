package com.lee.remember.android

import android.app.Application
import com.lee.remember.android.viewmodel.IntroViewModel
import com.lee.remember.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    val androidModule = module {
        viewModel { IntroViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            // Load modules
            modules(appModule() + androidModule)
        }
    }
}