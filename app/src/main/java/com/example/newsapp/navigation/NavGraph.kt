package com.example.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsapp.data.api.models.Article
import com.example.newsapp.ui.details.NewsDetailsScreen
import com.example.newsapp.ui.home.HomeScreen
import com.example.newsapp.ui.login.LoginScreen
import com.example.newsapp.ui.favorites.FavoritesScreen
import com.example.newsapp.ui.search.SearchScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Favorites : Screen("favorites")
    data object NewsDetails : Screen(
        "news_details/{url}/{title}/{description}/{urlToImage}/{author}/{publishedAt}/{content}/{sourceName}"
    ) {
        fun createRoute(article: Article): String {
            val url = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
            val title = URLEncoder.encode(article.title ?: "", StandardCharsets.UTF_8.toString())
            val description = URLEncoder.encode(article.description ?: "", StandardCharsets.UTF_8.toString())
            val urlToImage = URLEncoder.encode(article.urlToImage ?: "", StandardCharsets.UTF_8.toString())
            val author = URLEncoder.encode(article.author ?: "", StandardCharsets.UTF_8.toString())
            val publishedAt = URLEncoder.encode(article.publishedAt ?: "", StandardCharsets.UTF_8.toString())
            val content = URLEncoder.encode(article.content ?: "", StandardCharsets.UTF_8.toString())
            val sourceName = URLEncoder.encode(article.source?.name ?: "", StandardCharsets.UTF_8.toString())

            return "news_details/$url/$title/$description/$urlToImage/$author/$publishedAt/$content/$sourceName"
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                onArticleClick = { article ->
                    navController.navigate(Screen.NewsDetails.createRoute(article))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        composable(route = Screen.Search.route) {
            SearchScreen(
                onArticleClick = { article ->
                    navController.navigate(Screen.NewsDetails.createRoute(article))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                onArticleClick = { article ->
                    navController.navigate(Screen.NewsDetails.createRoute(article))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.NewsDetails.route,
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("urlToImage") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType },
                navArgument("publishedAt") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType },
                navArgument("sourceName") { type = NavType.StringType }
            )
        ) {
            NewsDetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
