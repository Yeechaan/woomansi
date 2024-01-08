package com.lee.remember.remote

import com.lee.remember.remote.request.MemoryAddResponse
import com.lee.remember.remote.request.MemoryRequest
import com.lee.remember.remote.request.SignupRequest
import com.lee.remember.remote.request.SignupResponse
import com.lee.remember.repository.AuthRepository
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
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

    suspend fun addMemory(token: String, request: MemoryRequest): MemoryAddResponse? {
        val token = AuthRepository().getToken()?.accessToken ?: ""

        val response = client.post(memoryUrl) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        Napier.d("### ${response.bodyAsText()}")

        return response.body()
    }


}