package com.lee.remember.remote

import com.lee.remember.remote.request.EmailRequest
import com.lee.remember.remote.request.EmailResponse
import com.lee.remember.remote.request.LoginRequest
import com.lee.remember.remote.request.LoginResponse
import com.lee.remember.remote.request.SignupRequest
import com.lee.remember.remote.request.SignupResponse
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

//const val baseUrl = "https://api.our-memory.store/v2/"
const val baseUrl = "http://118.67.133.148:8080/v2/"

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


    suspend fun signup(request: SignupRequest): Result<SignupResponse> {
        val response = client.post(authUrl + "sign-up") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        Napier.d("### ${response.bodyAsText()}")

        return if (response.status == HttpStatusCode.OK) {
            Result.success(response.body())
        } else {
            // Todo
//            val resultCode = (response.body() as SignupResponse).resultCode
//            Result.failure(Exception(resultCode))
            Result.failure(Exception("Network response status : ${response.status}"))
        }
    }

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        try {
            val response = client.post(authUrl + "log-in") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            Napier.d("### ${response.bodyAsText()}")

            return if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Network response status : ${response.status}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun sendEmailCode(request: EmailRequest): Result<EmailResponse> {
        val response = client.post(authUrl + "validate-email") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        Napier.d("### ${response.bodyAsText()}")

        return if (response.status == HttpStatusCode.OK) {
            Result.success(response.body())
        } else {
            Result.failure(Exception("Network response status : ${response.status}"))
        }
    }
}