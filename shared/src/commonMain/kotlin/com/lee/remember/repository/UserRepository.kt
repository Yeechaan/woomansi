package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UserRepository(
    val userDao: UserDao = UserDao(),
    val userApi: UserApi = UserApi(),
    val authDao: AuthDao = AuthDao(),
) {

    fun getUser() = userDao.getUser()

    suspend fun fetchUser(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val auth = authDao.getToken()
            val token = auth?.accessToken ?: return@withContext Result.failure(Exception(""))
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
}

data class UserUpdate(
    val name: String,
    val phoneNumber: String,
    val profileImage: String,
)