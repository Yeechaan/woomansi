package com.lee.remember.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import org.koin.core.module.Module

class RealmDataStore(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        const val DEFAULT_INITIAL_VECTOR = ""
        const val DEFAULT_ENCRYPTED_KEY = ""
    }

    private val initialVectorName = stringPreferencesKey("initial_vector")
    private val encryptedKeyName = stringPreferencesKey("encrypted_key")

    val settings: Flow<RealmSettings> = dataStore.data.map {
        RealmSettings(
            it[initialVectorName] ?: DEFAULT_INITIAL_VECTOR,
            it[encryptedKeyName] ?: DEFAULT_ENCRYPTED_KEY,
        )
    }

    suspend fun saveSettings(
        initialVector: String,
        encryptedKey: String,
    ) {
        dataStore.edit {
            it[initialVectorName] = initialVector
            it[encryptedKeyName] = encryptedKey
        }
    }
}

data class RealmSettings(
    val initialVector: String,
    val encryptedKey: String,
)

fun createDataStore(
    producePath: () -> String,
): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
    corruptionHandler = null,
    migrations = emptyList(),
    produceFile = { producePath().toPath() },
)

internal const val dataStoreFileName = "meetings.preferences_pb"
fun initKoinAndroid() = getBaseModules()


//fun initKoiniOS(appConfig: AppConfig) {
//    initKoin(listOf(module { single { appConfig } }))
//}

expect val platformModule: Module
internal fun getBaseModules() = platformModule