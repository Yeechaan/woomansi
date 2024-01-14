package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.AuthApi
import com.lee.remember.remote.request.LoginRequest
import com.lee.remember.remote.request.LoginResponse
import com.lee.remember.remote.request.SignupRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthRepository(
    val authApi: AuthApi = AuthApi(),
    val authDao: AuthDao = AuthDao(),
    val userDao: UserDao = UserDao(),
) {

    fun getToken() = authDao.getToken()

    suspend fun signUp(email: String, password: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val request = SignupRequest(email, password)
            val result = authApi.signup(request)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        userDao.setUser(it.asRealm())
                        authDao.updateAuth(it.result?.jwtToken ?: "", password)

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

    suspend fun login(email: String, password: String): Result<LoginResponse> =
        withContext(Dispatchers.IO) {
            val request = LoginRequest(email = email, password = password)
            val result = authApi.login(request)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        authDao.updateAuth(it.result?.jwtToken ?: "", password)
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

}