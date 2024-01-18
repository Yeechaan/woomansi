package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.repository.AuthRepository
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class IntroUiState(
    val isFirst: Boolean? = null,
    val isAuthSuccess: Boolean? = null,
)

class IntroViewModel(
    val authRepository: AuthRepository = AuthRepository(),
    val userRepository: UserRepository = UserRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(IntroUiState())
    val uiState: StateFlow<IntroUiState> = _uiState.asStateFlow()

    fun initUserState() {
        val token = authRepository.getToken()
        if (token == null) {
            _uiState.update {
                it.copy(isFirst = true)
            }
        } else {
            viewModelScope.launch {
                val result = userRepository.fetchUser()
                _uiState.update {
                    it.copy(isAuthSuccess = result.isSuccess)
                }
            }
        }
    }

    fun fetchFriends() {
        viewModelScope.launch {
            FriendRepository().fetchFriends()
        }
    }
}