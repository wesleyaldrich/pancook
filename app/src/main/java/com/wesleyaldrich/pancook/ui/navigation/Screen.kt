package com.wesleyaldrich.pancook.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object MyRecipe : Screen("recipe", "My Recipe", Icons.AutoMirrored.Filled.List)
    object Add : Screen("add", "Add Recipe", Icons.Default.Add)
    object Planner : Screen("planner", "Planner", Icons.Default.Event)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object GroceryList : Screen("grocery_list", "Grocery List", Icons.AutoMirrored.Filled.List)
    object SavedRecipe : Screen("saved_recipe", "Saved Recipe", Icons.Default.SaveAlt)
    object DetailRecipe : Screen("detail_recipe/{recipeId}", "Detail Recipe", Icons.Default.Info) {
        fun createRoute(recipeId: Int) = "detail_recipe/$recipeId"
    }
    // New Instruction Screen - simplified route
    object Instruction : Screen("instruction/{recipeId}", "Instruction", Icons.Default.PlayArrow) {
        fun createRoute(recipeId: Int) = "instruction/$recipeId" // No stepIndex in route
    }
    // New RecipeCompletion Screen
    object RecipeCompletion : Screen("recipe_completion/{recipeId}", "Recipe Completion", Icons.Default.Home) {
        fun createRoute(recipeId: Int) = "recipe_completion/$recipeId"
    }
}