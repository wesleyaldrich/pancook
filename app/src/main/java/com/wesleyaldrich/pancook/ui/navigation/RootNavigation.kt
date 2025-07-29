package com.wesleyaldrich.pancook.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.ui.MainScreen
import com.wesleyaldrich.pancook.ui.screens.LoginScreen

@Composable
fun RootNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // The app will start at the Login screen
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Main.route) {
            // When we navigate to "main", we show the entire MainScreen composable,
            // which contains its own internal navigation.
            MainScreen()
        }
    }
}