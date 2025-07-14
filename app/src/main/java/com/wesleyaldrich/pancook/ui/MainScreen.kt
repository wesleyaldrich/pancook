package com.wesleyaldrich.pancook.ui

import android.R.attr.bottom
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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

    val screens = listOf(
        Screen.Home,
        Screen.MyRecipe,
        Screen.Add,
        Screen.Planner,
        Screen.Profile
    )

    Scaffold(
        topBar = {
            val currentScreen = screens.find { it.route == currentRoute }
            TopAppBar(title = { Text(currentScreen?.title ?: "") })
        },
        bottomBar = {
            CustomBottomBar(
                navController = navController,
                currentRoute = currentRoute ?: ""
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Add.route)
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .offset(y = 48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Center Action")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        NavigationGraph(navController, modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun CustomBottomBar(navController: NavHostController, currentRoute: String) {
    val items = listOf(Screen.Home, Screen.MyRecipe, Screen.Planner, Screen.Profile)

    NavigationBar(
        tonalElevation = 8.dp,
    ) {
        items.forEachIndexed { index, screen ->
            if (index == 2) {
                // Empty space for center FAB
                Spacer(modifier = Modifier.weight(1f))
            }

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
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) }
            )
        }
    }
}

