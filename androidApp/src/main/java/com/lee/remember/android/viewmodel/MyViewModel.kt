package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.UserRealm
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyUiState(
    val user: UserRealm? = null,
    val loading: Boolean = false,
    val success: Boolean = false,
    val message: String = "",
)

class MyViewModel(
    private val userRepository: UserRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    init {
        val user = userRepository.getUser()
        _uiState.update { it.copy(user = user) }
    }

    fun deleteUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = userRepository.deleteUser()
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(success = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(message = it.message) }
                }
            )
        }
    }
}