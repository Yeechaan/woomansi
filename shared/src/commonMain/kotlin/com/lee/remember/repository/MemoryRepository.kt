package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.MemoryDao
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.MemoryApi
import com.lee.remember.remote.request.MemoryGetListResponse
import com.lee.remember.remote.request.MemoryGetResponse
import com.lee.remember.remote.request.MemoryRequest
import com.lee.remember.remote.request.MemoryUpdateRequest
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
                    if (it.resultCode == "SUCCESS" && it.result != null) {
                        memoryDao.setMemory(it.result.asRealm())
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

    suspend fun deleteMemory(memoryId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val result = memoryApi.deleteMemory(token, memoryId)
            result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        memoryDao.deleteMemory(memoryId)
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

    suspend fun updateMemory(memoryId: Int, request: MemoryUpdateRequest): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val result = memoryApi.updateMemory(token, memoryId, request)
            result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS" && it.result != null) {
                        memoryDao.updateMemory(memoryId, it.result.asRealm())
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


    fun getMemory(memoryId: Int) = memoryDao.getMemory(memoryId).firstOrNull()
    fun getMemories() = memoryDao.getMemories().asFlow()

    suspend fun fetchMemories() = withContext(Dispatchers.IO) {
        val result = memoryApi.getMemoryList(token)
        result.getOrNull()?.result?.let { response ->
            val memories = response.map { it.asRealm() }.toRealmList()
            memoryDao.setMemories(memories)
        }
    }

}