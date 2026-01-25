package com.example.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.api.models.Article
import com.example.newsapp.data.local.entity.FavoriteArticle
import com.example.newsapp.data.local.entity.toArticle
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val articles: List<Article> = emptyList(),
    val favorites: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadNews()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            newsRepository.getAllFavorites().collect { favoriteArticles ->
                _uiState.update {
                    it.copy(favorites = favoriteArticles.map(FavoriteArticle::toArticle))
                }
            }
        }
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = newsRepository.getTopHeadlines()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            articles = result.data.filter { article ->
                                !article.title.isNullOrBlank() && article.title != "[Removed]"
                            },
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadNews()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
