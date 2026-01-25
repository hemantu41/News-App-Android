package com.example.newsapp.data.repository

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.api.NewsApiService
import com.example.newsapp.data.api.models.Article
import com.example.newsapp.data.local.dao.FavoriteDao
import com.example.newsapp.data.local.entity.FavoriteArticle
import com.example.newsapp.data.local.entity.toFavoriteArticle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

@Singleton
class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService,
    private val favoriteDao: FavoriteDao
) {

    suspend fun getTopHeadlines(): Result<List<Article>> {
        return try {
            val response = newsApiService.getTopHeadlines(
                apiKey = BuildConfig.NEWS_API_KEY
            )
            if (response.status == "ok") {
                Result.Success(response.articles)
            } else {
                Result.Error("Failed to fetch news")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred", e)
        }
    }

    suspend fun searchNews(query: String): Result<List<Article>> {
        return try {
            val response = newsApiService.searchNews(
                query = query,
                apiKey = BuildConfig.NEWS_API_KEY
            )
            if (response.status == "ok") {
                Result.Success(response.articles)
            } else {
                Result.Error("Failed to search news")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred", e)
        }
    }

    // Favorites operations
    fun getAllFavorites(): Flow<List<FavoriteArticle>> {
        return favoriteDao.getAllFavorites()
    }

    fun isFavorite(url: String): Flow<Boolean> {
        return favoriteDao.isFavorite(url)
    }

    suspend fun addToFavorites(article: Article) {
        favoriteDao.insertFavorite(article.toFavoriteArticle())
    }

    suspend fun removeFromFavorites(url: String) {
        favoriteDao.deleteFavoriteByUrl(url)
    }

    suspend fun toggleFavorite(article: Article): Boolean {
        val existing = favoriteDao.getFavoriteByUrl(article.url)
        return if (existing != null) {
            favoriteDao.deleteFavoriteByUrl(article.url)
            false
        } else {
            favoriteDao.insertFavorite(article.toFavoriteArticle())
            true
        }
    }
}
