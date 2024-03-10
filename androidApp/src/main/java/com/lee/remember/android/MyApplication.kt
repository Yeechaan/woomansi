package com.lee.remember.android

import android.app.Application
import android.content.Context
import com.lee.remember.android.utils.NetworkUtils
import com.lee.remember.android.viewmodel.friend.FriendProfileViewModel
import com.lee.remember.android.viewmodel.friend.FriendViewModel
import com.lee.remember.android.viewmodel.home.HistoryViewModel
import com.lee.remember.android.viewmodel.intro.IntroViewModel
import com.lee.remember.android.viewmodel.intro.LoginViewModel
import com.lee.remember.android.viewmodel.memory.MemoryAddViewModel
import com.lee.remember.android.viewmodel.memory.MemoryEditViewModel
import com.lee.remember.android.viewmodel.memory.MemoryFriendViewModel
import com.lee.remember.android.viewmodel.memory.MemoryViewModel
import com.lee.remember.android.viewmodel.my.MyViewModel
import com.lee.remember.android.viewmodel.intro.SignUpViewModel
import com.lee.remember.android.viewmodel.intro.SplashViewModel
import com.lee.remember.di.appModule
import com.lee.remember.local.datastore.initKoinAndroid
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    private val androidModule = module {
        single<Context> { applicationContext }

        viewModel { SplashViewModel(get(), get(), get(), get(), get(), get())}
        viewModel { SignUpViewModel(get(), get()) }

        viewModel { IntroViewModel(get()) }
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
        }

        Napier.base(DebugAntilog())
    }
}