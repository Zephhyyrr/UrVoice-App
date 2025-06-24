package com.firman.capstone.urvoice.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Splash : Screen("splash")

    @Serializable
    object OnBoarding : Screen("onboarding")

    @Serializable
    object Sign: Screen ("sign")

    @Serializable
    object Login : Screen("login")

    @Serializable
    object Register : Screen("register")

    @Serializable
    object Home : Screen("home")

    @Serializable
    object History : Screen("history")

    @Serializable
    object Profile : Screen("profile")

    @Serializable
    object SpeechToText: Screen("speech-to-text")

    @Serializable
    data class Article(val id: String) : Screen("article/{id}") {
        fun articleRoute() = "article/$id"
    }

    @Serializable
    object Main : Screen("main")
}
