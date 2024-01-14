package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.MemoryDao
import com.lee.remember.local.model.MemoryRealm
import com.lee.remember.remote.MemoryApi
import com.lee.remember.remote.request.MemoryAddResponse
import com.lee.remember.remote.request.MemoryGetListResponse
import com.lee.remember.remote.request.MemoryGetResponse
import com.lee.remember.remote.request.MemoryRequest
import io.github.aakira.napier.Napier
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class MemoryRepository(
    val memoryApi: MemoryApi = MemoryApi(),
    val memoryDao: MemoryDao = MemoryDao(),
    val authDao: AuthDao = AuthDao(),
) {
    private val token = authDao.getToken() ?: ""

    suspend fun addMemory(request: MemoryRequest): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val result = memoryApi.addMemory(token, request)
            result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        saveMemory(it.result!!)
                        Result.success(true)
                    } else {
                        Result.failure(Exception(it.resultCode))
                    }
                },
                onFailure = {
                    Result.failure(Exception(it))
                }
            )
        }

    suspend fun getMemory(id: Int): Result<MemoryGetResponse> =
        withContext(Dispatchers.IO) {
            val result = memoryApi.getMemory(token, id)
            result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        Result.success(it)
                    } else {
                        Result.failure(Exception(it.resultCode))
                    }
                },
                onFailure = {
                    Result.failure(Exception(it))
                }
            )
        }

    suspend fun getMemoryList(): Result<MemoryGetListResponse> =
        withContext(Dispatchers.IO) {
            val result = memoryApi.getMemoryList(token)

            result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        Result.success(it)
                    } else {
                        Result.failure(Exception(it.resultCode))
                    }
                },
                onFailure = {
                    Result.failure(Exception(it))
                }
            )
        }

    private suspend fun saveMemory(response: MemoryAddResponse.Result) {
        val friendId = response.id
        val memoryRealm = MemoryRealm().apply {
            this.title = response.title ?: ""
            this.description = response.description ?: ""
            this.date = response.date ?: ""
            this.friendTags.addAll(response.friends?.map { it.name ?: "" }?.toRealmList() ?: realmListOf())
            this.images.addAll(response.images?.map { it.image ?: "" }?.toRealmList() ?: realmListOf())
        }

        memoryDao.setMemoryByFriendId(friendId, memoryRealm)
    }

}