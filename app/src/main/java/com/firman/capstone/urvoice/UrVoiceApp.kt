package com.firman.capstone.urvoice

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.firman.capstone.urvoice.ui.navigation.BottomAppBarWithFab
import com.firman.capstone.urvoice.ui.navigation.BottomNavItem
import com.firman.capstone.urvoice.ui.navigation.Screen
import com.firman.capstone.urvoice.ui.pages.ArticleDetailScreen
import com.firman.capstone.urvoice.ui.pages.ArticleScreen
import com.firman.capstone.urvoice.ui.pages.EditProfileScreen
import com.firman.capstone.urvoice.ui.pages.HomeScreen
import com.firman.capstone.urvoice.ui.pages.LoginScreen
import com.firman.capstone.urvoice.ui.pages.OnBoardingScreen
import com.firman.capstone.urvoice.ui.pages.ProfileScreen
import com.firman.capstone.urvoice.ui.pages.RegisterScreen
import com.firman.capstone.urvoice.ui.pages.SignScreen
import com.firman.capstone.urvoice.ui.pages.RecordScreen
import com.firman.capstone.urvoice.ui.pages.SpeechToTextScreen
import com.firman.capstone.urvoice.ui.pages.SplashScreen
import com.firman.capstone.urvoice.ui.theme.UrVoiceTheme
import com.firman.capstone.urvoice.ui.viewmodel.ArticleViewModel
import com.firman.capstone.urvoice.ui.viewmodel.ProfileViewModel
import com.firman.capstone.urvoice.ui.viewmodel.SpeechViewModel

@Composable
fun UrVoiceRootApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val routesWithBottomNav = listOf(
        Screen.Home.route,
        "article",
        Screen.Record.route,
        Screen.History.route,
        Screen.Profile.route
    )

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, "Home", painterResource(R.drawable.ic_home)),
        BottomNavItem("article", "Article", painterResource(R.drawable.ic_articles)),
        BottomNavItem(
            Screen.Record.route,
            "Voice",
            painterResource(R.drawable.ic_mic),
            isMainFeature = true
        ),
        BottomNavItem(Screen.History.route, "History", painterResource(R.drawable.ic_history)),
        BottomNavItem(Screen.Profile.route, "Profile", painterResource(R.drawable.ic_profile))
    )

    val sharedSpeechViewModel: SpeechViewModel = hiltViewModel()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }
            composable(Screen.OnBoarding.route) {
                OnBoardingScreen(
                    navController = navController,
                    onFinishOnboarding = {
                        navController.navigate(Screen.Sign.route) {
                            popUpTo(Screen.OnBoarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Sign.route) {
                SignScreen(navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable("article") {
                val viewModel: ArticleViewModel = hiltViewModel()
                ArticleScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("article/{id}") { backStackEntry ->
                val viewModel: ArticleViewModel = hiltViewModel()
                val articleId =
                    backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable

                ArticleDetailScreen(
                    viewModel = viewModel,
                    articleId = articleId,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Record.route) {
                RecordScreen(
                    viewModel = sharedSpeechViewModel,
                    onNavigateToSpeechToText = {
                        navController.navigate(Screen.SpeechToText.route)
                    }
                )
            }

            composable(Screen.SpeechToText.route) {
                SpeechToTextScreen(
                    viewModel = sharedSpeechViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateRecordScreen = {
                        navController.navigate(Screen.Record.route)
                    },
                    onNavigateAnalyzeScreen = {
                        navController.navigate(Screen.Analzye.route) {
                            popUpTo(Screen.SpeechToText.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.History.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("History Screen", fontSize = 24.sp)
                }
            }

            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = hiltViewModel()
                val shouldRefresh = navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.getLiveData<Boolean>("refreshProfile")

                LaunchedEffect(shouldRefresh?.value) {
                    if (shouldRefresh?.value == true) {
                        viewModel.getCurrentUser()
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("refreshProfile", false)
                    }
                }

                ProfileScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onEditProfileClick = {
                        navController.navigate(Screen.EditProfile.route)
                    }
                )
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("refreshProfile", true)
                        navController.popBackStack()
                    }
                )
            }
        }

        if (currentRoute in routesWithBottomNav) {
            BottomAppBarWithFab(
                items = bottomNavItems,
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun UrVoiceApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BackHandler(enabled = true) {
        when (currentRoute) {
            Screen.Home.route,
            "article",
            Screen.Record.route,
            Screen.History.route,
            Screen.Profile.route,
            Screen.Login.route,
            Screen.Register.route -> {
                (context as? Activity)?.finish()
            }

            else -> {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            }
        }
    }
    UrVoiceRootApp(navController)
}

@Preview(showBackground = true)
@Composable
fun UrVoiceAppPreview() {
    UrVoiceTheme {
        UrVoiceRootApp()
    }
}