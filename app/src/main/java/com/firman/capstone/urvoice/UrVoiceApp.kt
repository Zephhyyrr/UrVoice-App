package com.firman.capstone.urvoice

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.firman.capstone.urvoice.ui.navigation.Screen
import com.firman.capstone.urvoice.ui.pages.HomeScreen
import com.firman.capstone.urvoice.ui.pages.LoginScreen
import com.firman.capstone.urvoice.ui.pages.OnBoardingScreen
import com.firman.capstone.urvoice.ui.pages.RegisterScreen
import com.firman.capstone.urvoice.ui.pages.SignScreen
import com.firman.capstone.urvoice.ui.pages.SplashScreen
import com.firman.capstone.urvoice.ui.theme.UrVoiceTheme

@Composable
fun UrVoiceApp() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.onSurface
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues)
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
                    },
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUrVoiceApp() {
    UrVoiceTheme {
        UrVoiceApp()
    }
}