package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class HistoryUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val friends: List<FriendRealm> = listOf(),
)

class HistoryViewModel(
    private val friendRepository: FriendRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

    init {
        val friends = friendRepository.getFriends()
        _uiState.update { it.copy(friends = friends) }
    }

}