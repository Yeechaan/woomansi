package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.utils.parseUtcString
import com.lee.remember.local.dao.UserDao
import com.lee.remember.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedUiState(
    val name: String = "",
    val memories: List<FriendHistory> = listOf()
)

class FeedViewModel(
    val memoryRepository: MemoryRepository = MemoryRepository(),
    val userDao: UserDao = UserDao(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    fun initFeedState() {
        viewModelScope.launch {
            val name = userDao.getUser()?.name ?: ""
            _uiState.update { it.copy(name = name) }

            memoryRepository.getMemories().collectLatest {
                val memories = it.list.map {
                    val friends = it.friendTags.map { it.name }

                    FriendHistory(
                        id = it.id,
                        title = it.title ?: "",
                        contents = it.description ?: "",
                        image = it.images.firstOrNull() ?: "",
                        date = parseUtcString(it.date ?: ""),
                        ownerFriendName = friends.firstOrNull() ?: "",
                        friendNames = if (friends.isNotEmpty()) friends.subList(1, friends.size) else listOf()
                    )
                }

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