package com.lee.oneweekonebook.ui.suggest.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.ui.suggest.SuggestBookApi
import com.lee.oneweekonebook.ui.suggest.model.RecommendBook
import com.lee.oneweekonebook.ui.suggest.model.SuggestBook
import com.lee.oneweekonebook.ui.suggest.model.asBookList
import com.lee.oneweekonebook.ui.suggest.model.getRandomCategory
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class SuggestBookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<SuggestBook>>()
    val books: LiveData<List<SuggestBook>>
        get() = _books

    init {
        viewModelScope.launch {
            Logger.d("SuggestBookViewModel")

            // TODO 카테고리 별 도서 10개 보여주기 (소설, 인문, 역사, ...) 새로고침 새로운 카테고리의 도서 표시
            val responseCategory = SuggestBookApi.suggestBookApiService.getSuggestBookAsync(categoryId = RecommendBook().getRandomCategory()).await()
            Logger.d(responseCategory)
            _books.value = responseCategory.asBookList()
        }
    }

    fun refreshBooks() {
        viewModelScope.launch {
            val responseCategory = SuggestBookApi.suggestBookApiService.getSuggestBookAsync(categoryId = RecommendBook().getRandomCategory()).await()
            Logger.d(responseCategory)
            _books.value = responseCategory.asBookList()
        }
    }

}

class SuggestBookViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuggestBookViewModel::class.java)) {
            return SuggestBookViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}