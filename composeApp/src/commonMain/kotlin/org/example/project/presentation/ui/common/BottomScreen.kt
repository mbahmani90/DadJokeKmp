package org.example.project.presentation.ui.common


sealed class BottomScreen(
    val route: String,
    val label: String,
    val icon: String
) {
    object Joke : BottomScreen("joke", "Menu", "😂")
    object Favorite : BottomScreen("favorite", "Favorites", "❤️")
}