package com.lee.remember.di

import com.lee.remember.encryption.EncryptionRealm
import com.lee.remember.local.BaseRealm
import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.local.dao.MemoryDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.datastore.RealmDataStore
import com.lee.remember.remote.AuthApi
import com.lee.remember.remote.FriendApi
import com.lee.remember.remote.MemoryApi
import com.lee.remember.remote.UserApi
import com.lee.remember.repository.AuthRepository
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.MemoryRepository
import com.lee.remember.repository.UserRepository
import org.koin.dsl.module

fun appModule() = module {
    single { RealmDataStore(get()) }
    single { EncryptionRealm() }
    single<BaseRealm> { BaseRealm(get(), get()) }
    single<AuthApi> { AuthApi() }
    single<AuthDao> { AuthDao(get()) }
    single<UserApi> { UserApi() }
    single<UserDao> { UserDao(get()) }
    single<FriendApi> { FriendApi() }
    single<FriendDao> { FriendDao(get()) }
    single<MemoryApi> { MemoryApi() }
    single<MemoryDao> { MemoryDao(get()) }

    factory<UserRepository> { UserRepository(get(), get(), get()) }
    factory<AuthRepository> { AuthRepository(get(), get(), get()) }
    factory<FriendRepository> { FriendRepository(get(), get(), get()) }
    factory<MemoryRepository> { MemoryRepository(get(), get(), get(), get()) }
}