package com.wesleyaldrich.pancook.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType // Import NavType
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.ui.screens.AddRecipeScreen
import com.wesleyaldrich.pancook.ui.screens.HomeScreen
import com.wesleyaldrich.pancook.ui.screens.MyRecipeScreen
import com.wesleyaldrich.pancook.ui.screens.PlannerScreen
import com.wesleyaldrich.pancook.ui.screens.GroceryScreen
import com.wesleyaldrich.pancook.ui.screens.ProfileScreen
import com.wesleyaldrich.pancook.ui.screens.DetailRecipeScreen
import com.wesleyaldrich.pancook.ui.screens.SavedRecipeScreen // Import SavedRecipeScreen
import com.wesleyaldrich.pancook.ui.screens.InstructionScreen
import com.wesleyaldrich.pancook.ui.screens.RecipeCompletionScreen

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
        composable(Screen.Home.route) {
            HomeScreen(onRecipeClick = { recipeId ->
                // Ini adalah implementasi callback.
                // Saat kartu diklik di HomeScreen, kode ini yang akan berjalan.
                navController.navigate("detail_recipe/$recipeId")
            })
        }
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
        composable(Screen.GroceryList.route) {
            GroceryScreen(
                onBackClick = { navController.popBackStack() },
                navController = navController,
                onRemoveClick = {},
            )
        }
        composable(Screen.Profile.route) { ProfileScreen() }
        composable(Screen.SavedRecipe.route) { SavedRecipeScreen(navController = navController) } // Pass navController
        // Instruction Screen now only takes recipeId
        composable(
            route = Screen.Instruction.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            InstructionScreen(recipeId = recipeId, navController = navController) // Removed instructions list from parameter
        }
        composable(
            route = Screen.RecipeCompletion.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            RecipeCompletionScreen(recipeId = recipeId, navController = navController)
        }
    }
}
