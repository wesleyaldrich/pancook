package com.wesleyaldrich.pancook.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object MyRecipe : Screen("my_recipe", "My Recipe")
    object Planner : Screen("planner", "Planner")
    object Profile : Screen("profile", "Profile")
}