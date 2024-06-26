package com.lee.remember.android.viewmodel.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.model.Friend
import com.lee.remember.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FriendProfileUiState(
    val friend: Friend? = null,
)

class FriendProfileViewModel(
    private val friendId: Int?,
    private val friendRepository: FriendRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendProfileUiState())
    val uiState: StateFlow<FriendProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            friendRepository.getFriendAsFlow(friendId ?: -1)?.collectLatest {
                val friend = it
                _uiState.update { it.copy(friend = friend) }
            }
        }
    }
}