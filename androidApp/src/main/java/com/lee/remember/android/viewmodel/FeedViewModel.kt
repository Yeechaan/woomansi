package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.model.Memory
import com.lee.remember.model.asData
import com.lee.remember.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedUiState(
    val name: String = "",
    val memories: List<Memory> = listOf(),
)

class FeedViewModel(
    val memoryRepository: MemoryRepository = MemoryRepository(),
    val friendDao: FriendDao = FriendDao(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

//    init {
//        initFeedState()
//    }

    fun initFeedState() {
        viewModelScope.launch {
            memoryRepository.getMemories().collectLatest {
                val memories = it.list.map { it.asData() }
                    .sortedByDescending { it.date }

                _uiState.update {
                    it.copy(memories = memories)
                }
            }
        }
    }

    fun initFeedFriendState(friendId: Int) {
        viewModelScope.launch {
            val name = friendDao.getFriend(friendId)?.name ?: ""
            _uiState.update { it.copy(name = name) }

            memoryRepository.getMemories().collectLatest {
                val memories = it.list.map { it.asData() }
                    .sortedByDescending { it.date }
                    .filter { it.ownerFriend?.id == friendId }

                _uiState.update {
                    it.copy(memories = memories)
                }
            }
        }
    }

    fun deleteMemory(memoryId: Int) {
        viewModelScope.launch {
            memoryRepository.deleteMemory(memoryId)
        }
    }

}