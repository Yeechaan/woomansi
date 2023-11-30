package com.lee.remember.remote

import com.lee.remember.request.LoginRequest
import com.lee.remember.request.LoginResponse
import com.lee.remember.request.SignupRequest
import com.lee.remember.request.SignupResponse
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val baseUrl = "http://101.101.216.129:8080/api/v2/"

class AuthApi {

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val authUrl = baseUrl + "auth/"


    suspend fun signup(request: SignupRequest): SignupResponse? {
        val response = client.post(authUrl + "sign-up") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        Napier.d("### ${response.bodyAsText()}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

    suspend fun login(request: LoginRequest): LoginResponse? {
        val response = client.post(authUrl + "log-in") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        Napier.d("### ${response.bodyAsText()}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }
}