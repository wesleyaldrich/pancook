package com.wesleyaldrich.pancook.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object MyRecipe : Screen("my_recipe", "My Recipe")
    object Planner : Screen("planner", "Planner")
    object Profile : Screen("profile", "Profile")
    object DetailRecipe : Screen("detail_recipe/{recipeId}", "Detail Recipe") {
        fun createRoute(recipeId: Int) = "detail_recipe/$recipeId"
    }
}