package com.lee.remember

import com.lee.remember.request.FriendAddRequest
import com.lee.remember.request.FriendAddResponse
import com.lee.remember.request.RegisterRequest
import com.lee.remember.request.RegisterResponse
import com.lee.remember.request.SignInRequest
import com.lee.remember.request.SignInResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.Logger
import kotlinx.serialization.json.Json

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}

class GreetingKtor {
//    private val client = HttpClient()

    val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    val url = "http://101.101.216.129:8080/api/v1/users/"
    val requestBody = "{\"email\":\"harry@naver.com\",\"password\":\"1234\"}"

    suspend fun greeting(id: String, password: String): String {
        val response = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(SignInRequest("asd@test.com", "password"))
//            setBody(SignInRequest(id, password))
        }
        return response.bodyAsText()
    }


    suspend fun register(request: RegisterRequest): RegisterResponse? {
        val response = client.post(url + "register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return if (response.status == HttpStatusCode.OK) response.body() else null
//        return response.body()
    }

    suspend fun signIn(request: SignInRequest): SignInResponse? {
        val response = client.post(url + "log-in") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return if (response.status == HttpStatusCode.OK) response.body() else null
//        return response.body()
    }

    suspend fun addFriend(token: String, request: FriendAddRequest): FriendAddResponse? {
        val response = client.post(url + "friends/add") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }

//        return if (response.status == HttpStatusCode.OK) response.body() else null
        return response.body()
    }
}