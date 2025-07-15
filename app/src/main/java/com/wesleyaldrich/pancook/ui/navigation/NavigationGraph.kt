package com.wesleyaldrich.pancook.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.wesleyaldrich.pancook.ui.screens.AddRecipeScreen
import com.wesleyaldrich.pancook.ui.screens.HomeScreen
import com.wesleyaldrich.pancook.ui.screens.MyRecipeScreen
import com.wesleyaldrich.pancook.ui.screens.PlannerScreen
import com.wesleyaldrich.pancook.ui.screens.GroceryScreen
import com.wesleyaldrich.pancook.ui.screens.ProfileScreen
import com.wesleyaldrich.pancook.ui.screens.DetailRecipeScreen // Import the DetailRecipeScreen
import com.wesleyaldrich.pancook.ui.screens.SavedRecipeScreen

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition    = { EnterTransition.None  },
        exitTransition     = { ExitTransition.None   },
        popEnterTransition = { EnterTransition.None  },
        popExitTransition  = { ExitTransition.None   },
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.MyRecipe.route) { MyRecipeScreen(navController = navController) }
        composable(
            route = Screen.DetailRecipe.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            DetailRecipeScreen(recipeId = recipeId, navController = navController)
        }
        composable(Screen.Add.route) { AddRecipeScreen() }
        composable(Screen.Planner.route) { PlannerScreen() }
        composable(Screen.GroceryList.route) { GroceryScreen(
            onBackClick = {},
            onRecipeClick = {},
            onRemoveClick = {},
        ) }
        composable(Screen.Profile.route) { ProfileScreen() }
        composable(Screen.SavedRecipe.route) { SavedRecipeScreen() }
    

    }
}