package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.remote.request.FriendRequest
import com.lee.remember.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FriendUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val message: String = "",
)

class FriendViewModel(
    val friendRepository: FriendRepository = FriendRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendUiState())
    val uiState: StateFlow<FriendUiState> = _uiState.asStateFlow()

    fun getFriend(friendId: Int) = friendRepository.getFriend(friendId)

    fun addFriends(friends: List<FriendRequest>) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = friendRepository.addFriend(friends)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(loading = false, success = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(loading = false, message = it.message ?: "") }
                }
            )
        }
    }

    fun resetUiState() {
        _uiState.value = FriendUiState()
    }

}