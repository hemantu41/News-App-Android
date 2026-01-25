package com.example.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.data.api.models.Article

@Entity(tableName = "favorite_articles")
data class FavoriteArticle(
    @PrimaryKey
    val url: String,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val author: String?,
    val publishedAt: String?,
    val content: String?,
    val sourceName: String?,
    val addedAt: Long = System.currentTimeMillis()
)

fun Article.toFavoriteArticle(): FavoriteArticle {
    return FavoriteArticle(
        url = this.url,
        title = this.title,
        description = this.description,
        urlToImage = this.urlToImage,
        author = this.author,
        publishedAt = this.publishedAt,
        content = this.content,
        sourceName = this.source?.name
    )
}

fun FavoriteArticle.toArticle(): Article {
    return Article(
        source = com.example.newsapp.data.api.models.Source(null, this.sourceName),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}
