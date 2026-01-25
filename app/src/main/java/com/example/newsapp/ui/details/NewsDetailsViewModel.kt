package com.example.newsapp.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.api.models.Article
import com.example.newsapp.data.api.models.Source
import com.example.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

data class NewsDetailsUiState(
    val article: Article? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsDetailsUiState())
    val uiState: StateFlow<NewsDetailsUiState> = _uiState.asStateFlow()

    init {
        // Retrieve article data from navigation arguments
        val url = savedStateHandle.get<String>("url")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        } ?: ""
        val title = savedStateHandle.get<String>("title")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }
        val description = savedStateHandle.get<String>("description")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }
        val urlToImage = savedStateHandle.get<String>("urlToImage")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }
        val author = savedStateHandle.get<String>("author")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }
        val publishedAt = savedStateHandle.get<String>("publishedAt")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }
        val content = savedStateHandle.get<String>("content")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }
        val sourceName = savedStateHandle.get<String>("sourceName")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }

        val article = Article(
            source = Source(null, sourceName),
            author = author,
            title = title,
            description = description,
            url = url,
            urlToImage = urlToImage,
            publishedAt = publishedAt,
            content = content
        )

        _uiState.update { it.copy(article = article) }

        // Observe favorite status
        observeFavoriteStatus(url)
    }

    private fun observeFavoriteStatus(url: String) {
        viewModelScope.launch {
            newsRepository.isFavorite(url).collect { isFavorite ->
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }

    fun toggleFavorite() {
        val article = _uiState.value.article ?: return
        viewModelScope.launch {
            newsRepository.toggleFavorite(article)
        }
    }
}
