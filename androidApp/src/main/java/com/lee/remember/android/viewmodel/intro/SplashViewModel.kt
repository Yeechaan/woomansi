package com.lee.remember.android.viewmodel.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.android.utils.NetworkUtils
import com.lee.remember.local.BaseRealm
import com.lee.remember.repository.AuthRepository
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.MemoryRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SplashUiState(
    val isFirst: Boolean? = null,
    val isLocalMode: Boolean? = null,
    val isAuthSuccess: Boolean? = null,
)

class SplashViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
    private val memoryRepository: MemoryRepository,
    private val networkUtils: NetworkUtils,
    private val baseRealm: BaseRealm,
): ViewModel() {

    private val _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState: StateFlow<SplashUiState> = _splashUiState.asStateFlow()

    fun initUserState() {
        viewModelScope.launch {
            baseRealm.initRealm()

            if (!networkUtils.isNetworkAvailable()) {
                _splashUiState.update {
                    it.copy(isLocalMode = true)
                }
                return@launch
            }

            val user = userRepository.getUser()
            if (user != null && user.isLocalMode) {
                _splashUiState.update {
                    it.copy(isLocalMode = true)
                }
                return@launch
            }

            val token = authRepository.getToken()
            if (token == null) {
                _splashUiState.update {
                    it.copy(isFirst = true)
                }
            } else {
//            viewModelScope.launch {
//                val result = userRepository.fetchUser()
//                _uiState.update {
//                    it.copy(isAuthSuccess = result.isSuccess)
//                }
//            }
                fetchUser()
            }
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val result = userRepository.fetchUser()

            result.fold(
                onSuccess = {
                    friendRepository.fetchFriends()
                    memoryRepository.fetchMemories()

                    _splashUiState.update {
                        it.copy(isAuthSuccess = true)
                    }
                },
                onFailure = {
                    _splashUiState.update {
                        it.copy(isAuthSuccess = false)
                    }
                }
            )
        }
    }

    fun resetSplashUiState() {
        _splashUiState.value = SplashUiState()
    }
}