package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.local.model.UserRealm
import com.lee.remember.remote.request.FriendRequest
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.UserRepository
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
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendUiState())
    val uiState: StateFlow<FriendUiState> = _uiState.asStateFlow()

    fun getFriend(friendId: Int) = friendRepository.getFriend(friendId)

    val user = userRepository.getUser() ?: UserRealm()

    fun addFriends(friends: List<FriendRequest>) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = if (user.isLocalMode) {
                friendRepository.addFriendsToLocal(friends)
            } else {
                friendRepository.addFriends(friends)
            }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(loading = false, success = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(loading = false, message = it.message) }
                }
            )
        }
    }

    fun updateFriend(friendId: String, friend: FriendRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = if (user.isLocalMode) {
                friendRepository.updateFriendToLocal(friendId, friend)
            } else {
                friendRepository.updateFriend(friendId, friend)
            }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(loading = false, success = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(loading = false, message = it.message) }
                }
            )
        }
    }

    fun resetUiState() {
        _uiState.value = FriendUiState()
    }

}