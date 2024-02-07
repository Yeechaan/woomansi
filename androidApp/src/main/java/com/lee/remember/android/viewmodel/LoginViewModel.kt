package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.repository.AuthRepository
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.MemoryRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val fetchSuccess: Boolean = false,
    val message: String = "",
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
    private val memoryRepository: MemoryRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                fetchFromServer()
            }
            _uiState.update { it.copy(isLoading = false, loginSuccess = result.isSuccess) }
        }
    }

    private fun fetchFromServer() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            userRepository.fetchUser()
            friendRepository.fetchFriends()
            memoryRepository.fetchMemories()

            _uiState.update { it.copy(isLoading = false, fetchSuccess = true) }
        }
    }

}