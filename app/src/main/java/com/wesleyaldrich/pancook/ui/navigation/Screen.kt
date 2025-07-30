package com.wesleyaldrich.pancook.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.annotation.DrawableRes
import com.wesleyaldrich.pancook.R

sealed class IconResource {
    data class Drawable(@DrawableRes val id: Int) : IconResource()
    data class Vector(val imageVector: ImageVector) : IconResource()
}

// A sealed class for screen routes
sealed class Screen(val route: String, val title: String, val icon: IconResource) {

    object Home : Screen("home", "Home", IconResource.Drawable(R.drawable.nav_home))
    object MyRecipe : Screen("recipe", "My Recipe", IconResource.Drawable(R.drawable.nav_my_recipe))
    object Add : Screen("add", "Add Recipe", IconResource.Vector(Icons.Default.Add))
    object Planner : Screen("planner", "Planner", IconResource.Drawable(R.drawable.nav_planner))
    object Profile : Screen("profile", "Profile", IconResource.Drawable(R.drawable.nav_profile))

    object GroceryList : Screen("grocery_list", "Grocery List", IconResource.Drawable(R.drawable.nav_grocery_list))
    object SavedRecipe : Screen("saved_recipe", "Saved Recipe", IconResource.Drawable(R.drawable.nav_saved_recipe))

    object Login : Screen("login", "Login", IconResource.Vector(Icons.Default.Lock))
    object Main : Screen("main", "Main", IconResource.Vector(Icons.Default.Home))

    object DetailRecipe : Screen("detail_recipe/{recipeId}", "Detail Recipe", IconResource.Vector(Icons.Default.Info)) {
        fun createRoute(recipeId: Int) = "detail_recipe/$recipeId"
    }

    object Instruction : Screen("instruction/{recipeId}", "Instruction", IconResource.Vector(Icons.Default.PlayArrow)) {
        fun createRoute(recipeId: Int) = "instruction/$recipeId"
    }

    object RecipeCompletion : Screen("recipe_completion/{recipeId}", "Recipe Completion", IconResource.Vector(Icons.Default.Home)) {
        fun createRoute(recipeId: Int) = "recipe_completion/$recipeId"
    }

    object Category : Screen("category/{categoryName}", "Category", IconResource.Vector(Icons.Default.Category)) {
        fun createRoute(categoryName: String) = "category/$categoryName"
    }
}
