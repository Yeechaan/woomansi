package com.lee.remember.remote

import com.lee.remember.remote.request.FriendResponse
import com.lee.remember.remote.request.MemoryAddResponse
import com.lee.remember.remote.request.MemoryGetListResponse
import com.lee.remember.remote.request.MemoryGetResponse
import com.lee.remember.remote.request.MemoryRequest
import com.lee.remember.remote.request.MemoryUpdateRequest
import com.lee.remember.remote.request.MemoryUpdateResponse
import com.lee.remember.remote.request.SignupRequest
import com.lee.remember.remote.request.SignupResponse
import com.lee.remember.repository.AuthRepository
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MemoryApi {

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val memoryUrl = baseUrl + "memories"

    suspend fun addMemory(token: String, request: MemoryRequest): Result<MemoryAddResponse> {
        val response = client.post(memoryUrl) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return if (response.status == HttpStatusCode.OK) {
            Result.success(response.body())
        } else {
            Result.failure(Exception("Network response status : ${response.status}"))
        }
    }

    suspend fun updateMemory(token: String, request: MemoryUpdateRequest): Result<MemoryUpdateResponse> {
        val response = client.post(memoryUrl) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return if (response.status == HttpStatusCode.OK) {
            Result.success(response.body())
        } else {
            Result.failure(Exception("Network response status : ${response.status}"))
        }
    }

    suspend fun getMemory(token: String, id: Int): Result<MemoryGetResponse> {
        val response = client.get("$memoryUrl/$id") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
        }

        return if (response.status == HttpStatusCode.OK) {
            Result.success(response.body())
        } else {
            Result.failure(Exception("Network response status : ${response.status}"))
        }
    }

    suspend fun getMemoryList(token: String): Result<MemoryGetListResponse> {
        val response = client.get(memoryUrl) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
        }

        return if (response.status == HttpStatusCode.OK) {
            Result.success(response.body())
        } else {
            Result.failure(Exception("Network response status : ${response.status}"))
        }
    }
}