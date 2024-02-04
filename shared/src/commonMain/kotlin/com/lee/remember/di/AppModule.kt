package com.lee.remember.di

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.remote.AuthApi
import com.lee.remember.remote.UserApi
import com.lee.remember.repository.AuthRepository
import com.lee.remember.repository.UserRepository
import org.koin.dsl.module

fun appModule() = module {
    single<AuthApi> { AuthApi() }
    single<AuthDao> { AuthDao() }
    single<UserApi> { UserApi() }
    single<UserDao> { UserDao() }

    factory<UserRepository> { UserRepository() }
    factory<AuthRepository> { AuthRepository() }
}