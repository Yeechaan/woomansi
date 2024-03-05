package com.lee.remember.android

import android.app.Application
import android.content.Context
import com.lee.remember.android.utils.NetworkUtils
import com.lee.remember.android.viewmodel.FriendProfileViewModel
import com.lee.remember.android.viewmodel.FriendViewModel
import com.lee.remember.android.viewmodel.HistoryViewModel
import com.lee.remember.android.viewmodel.IntroViewModel
import com.lee.remember.android.viewmodel.LoginViewModel
import com.lee.remember.android.viewmodel.MemoryAddViewModel
import com.lee.remember.android.viewmodel.MemoryEditViewModel
import com.lee.remember.android.viewmodel.MemoryFriendViewModel
import com.lee.remember.android.viewmodel.MemoryViewModel
import com.lee.remember.android.viewmodel.MyViewModel
import com.lee.remember.di.appModule
import com.lee.remember.local.datastore.initKoinAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    private val androidModule = module {
        single<Context> { applicationContext }
        viewModel { IntroViewModel(get(), get(), get(), get(), get()) }
        viewModel { LoginViewModel(get(), get(), get(), get()) }
        viewModel { MemoryViewModel(get(), get()) }
        viewModel { MyViewModel(get()) }
        viewModel { FriendViewModel(get(), get()) }
        viewModel { HistoryViewModel(get()) }
        viewModel { parameters -> MemoryAddViewModel(parameters.get(), get(), get(), get()) }
        viewModel { parameters -> MemoryEditViewModel(parameters.get(), get(), get(), get()) }
        viewModel { parameters -> FriendProfileViewModel(parameters.get(), get()) }
        viewModel { parameters -> MemoryFriendViewModel(parameters.get(), get(), get(), get()) }

        single { NetworkUtils(applicationContext) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            // Load modules
            modules(appModule() + androidModule + initKoinAndroid())

//            initKoinAndroid(
//                listOf(
//                    module {
//                        single<Context> { this@MyApplication }
//                    }
//                )
//            )
        }
    }
}