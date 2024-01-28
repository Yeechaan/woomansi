package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.UserRealm
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.UserApi
import com.lee.remember.remote.request.UserInfoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UserRepository(
    val userDao: UserDao = UserDao(),
    val userApi: UserApi = UserApi(),
    val authDao: AuthDao = AuthDao(),
) {

    fun getUser() = userDao.getUser()

    suspend fun updateUserName(name: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val auth = authDao.getAuth() ?: return@withContext Result.failure(Exception(""))
            val user = getUser() ?: return@withContext Result.failure(Exception(""))
            val userInfoRequest = UserInfoRequest(user.email, name, user.phoneNumber, user.profileImage)
            val result = userApi.updateUser(auth.accessToken, userInfoRequest)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        val userRealm = it.asRealm()
                        userDao.deleteUser()
                        userDao.setUser(userRealm)

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

    suspend fun fetchUser(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val token = authDao.getToken() ?: return@withContext Result.failure(Exception(""))
            val result = userApi.getUser(token)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        val userRealm = it.asRealm()
                        userDao.deleteUser()
                        userDao.setUser(userRealm)

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

    suspend fun deleteUser(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val token = authDao.getToken() ?: return@withContext Result.failure(Exception(""))
            val result = userApi.deleteUser(token)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
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

    suspend fun addTestUser(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val user = UserRealm().apply {
                this.name = "test_user"
            }
            userDao.setUser(user)

            Result.success(true)
        }
}