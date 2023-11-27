package com.lee.remember

import com.lee.remember.request.FriendMultiAddRequest
import com.lee.remember.request.FriendMultiAddResponse
import com.lee.remember.request.FriendAddRequest
import com.lee.remember.request.FriendAddResponse
import com.lee.remember.request.RegisterRequest
import com.lee.remember.request.RegisterResponse
import com.lee.remember.request.LoginRequest
import com.lee.remember.request.LoginResponse
import com.lee.remember.request.UserInfoRequest
import com.lee.remember.request.UserInfoResponse
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

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

    val userUrl = "http://101.101.216.129:8080/api/v1/users/"
    val friendUrl = "http://101.101.216.129:8080/api/v1/friends/"
    val requestBody = "{\"email\":\"harry@naver.com\",\"password\":\"1234\"}"

    suspend fun greeting(id: String, password: String): String {
        val response = client.post(userUrl) {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest("asd@test.com", "password"))
//            setBody(SignInRequest(id, password))
        }
        return response.bodyAsText()
        //        return response.body()
    }


    suspend fun register(request: RegisterRequest): RegisterResponse? {
        val response = client.post(userUrl + "register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

    suspend fun login(request: LoginRequest): LoginResponse? {
        val response = client.post(userUrl + "log-in") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        Napier.d("### ${response.bodyAsText()}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

    suspend fun setUser(token: String, request: UserInfoRequest): UserInfoResponse? {
        val response = client.put(userUrl + "me") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        Napier.d("### ${response.bodyAsText()}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

    suspend fun getUser(token: String): UserInfoResponse? {
        val response = client.get(userUrl + "me") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
        }

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

}