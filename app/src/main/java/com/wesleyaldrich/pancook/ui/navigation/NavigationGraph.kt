package com.wesleyaldrich.pancook.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
// Import the navigation builder
import androidx.navigation.navigation
import com.wesleyaldrich.pancook.ui.screens.*

// A new route for the nested graph
const val BOTTOM_BAR_ROUTE = "bottom_bar"

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        // The start destination is now the entire nested graph
        startDestination = BOTTOM_BAR_ROUTE,
        modifier = modifier,
        enterTransition    = { EnterTransition.None  },
        exitTransition     = { ExitTransition.None   },
        popEnterTransition = { EnterTransition.None  },
        popExitTransition  = { ExitTransition.None   },
    ) {
        // Nested graph for all screens accessible from the Bottom Bar
        navigation(
            startDestination = Screen.Home.route,
            route = BOTTOM_BAR_ROUTE
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.MyRecipe.route) {
                MyRecipeScreen(navController = navController)
            }
            composable(Screen.Planner.route) {
                PlannerScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = onLogout)
            }
        }

        // --- Screens outside the bottom bar flow remain here ---

        composable(Screen.Add.route) {
            AddRecipeScreen(onBackPressed = { navController.popBackStack() })
        }
        composable(Screen.GroceryList.route) {
            GroceryScreen(
                onBackClick = { navController.popBackStack() },
                navController = navController,
                onRemoveClick = {},
            )
        }
        composable(Screen.SavedRecipe.route) {
            SavedRecipeScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.DetailRecipe.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            DetailRecipeScreen(recipeId = recipeId, navController = navController)
        }
        composable(
            route = Screen.Instruction.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            InstructionScreen(recipeId = recipeId, navController = navController)
        }
        composable(
            route = Screen.RecipeCompletion.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            RecipeCompletionScreen(recipeId = recipeId, navController = navController)
        }
        composable(
            route = Screen.Category.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryScreen(categoryName = categoryName, navController = navController)
        }
    }
}