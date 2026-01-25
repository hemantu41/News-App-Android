package com.example.newsapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.api.models.Article
import com.example.newsapp.data.local.entity.FavoriteArticle
import com.example.newsapp.data.local.entity.toArticle
import com.example.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favorites: List<Article> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            newsRepository.getAllFavorites().collect { favoriteArticles ->
                _uiState.update {
                    it.copy(
                        favorites = favoriteArticles.map(FavoriteArticle::toArticle),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun removeFromFavorites(article: Article) {
        viewModelScope.launch {
            newsRepository.removeFromFavorites(article.url)
        }
    }
}
