package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class FriendProfileUiState(
    val friend: FriendRealm? = null
)

class FriendProfileViewModel(
    private val friendId: Int?,
    private val friendRepository: FriendRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(FriendProfileUiState())
    val uiState: StateFlow<FriendProfileUiState> = _uiState

    init {
        val friend = friendRepository.getFriend(friendId ?: -1)
        _uiState.update { it.copy(friend = friend) }
    }
}