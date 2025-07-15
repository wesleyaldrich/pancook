package com.wesleyaldrich.pancook.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.ui.navigation.NavigationGraph
import com.wesleyaldrich.pancook.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val screens = listOf(Screen.Home, Screen.MyRecipe, Screen.Planner, Screen.Profile)

    // Determine if the current route is the DetailRecipe screen
    val isDetailRecipeScreen = currentRoute?.startsWith(Screen.DetailRecipe.route.substringBefore('/')) == true

    Scaffold(
        topBar = {
            if (!isDetailRecipeScreen) { // Conditionally show TopAppBar
                val currentScreen = screens.find { it.route == currentRoute }
                TopAppBar(title = { Text(currentScreen?.title ?: "") })
            }
        },
        bottomBar = {
            if (!isDetailRecipeScreen) { // Conditionally show NavigationBar
                NavigationBar {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.Home, contentDescription = screen.title) },
                            label = { Text(screen.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavigationGraph(navController, modifier = Modifier.padding(paddingValues))
    }
}