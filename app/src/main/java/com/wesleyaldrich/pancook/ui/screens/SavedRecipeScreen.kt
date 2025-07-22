// SavedRecipeScreen.kt
package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.ui.components.ReusableCard
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import androidx.compose.runtime.remember
import com.wesleyaldrich.pancook.ui.screens.bookmarkedRecipes // Updated import
import com.wesleyaldrich.pancook.ui.screens.allRecipes // Updated import

@Composable
fun SavedRecipeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (bookmarkedRecipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No saved recipes yet. Bookmark some recipes to see them here!")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(bookmarkedRecipes) { recipe ->
                    val isBookmarked = remember(recipe.id) { bookmarkedRecipes.contains(recipe) }
                    ReusableCard(
                        imagePainter = painterResource(id = recipe.images.first()), // Use first image from list
                        title = recipe.title,
                        description = recipe.recipeMaker,
                        duration = recipe.duration,
                        upvoteCount = recipe.upvoteCount,
                        isBookmarked = isBookmarked,
                        onBookmarkClick = {
                            // Toggle bookmark status for this recipe
                            if (isBookmarked) {
                                bookmarkedRecipes.remove(recipe)
                            } else {
                                bookmarkedRecipes.add(recipe)
                            }
                        },
                        // The onDeleteClick lambda is still provided, but the button will be hidden
                        onDeleteClick = {
                            // Deleting from saved recipes means removing from bookmarkedRecipes
                            bookmarkedRecipes.remove(recipe)
                            // If you want deleting from saved also to delete from allRecipes:
                            // allRecipes.remove(recipe)
                        },
                        hideDeleteButton = true, // Set this to true to hide the delete button
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 2100
)
@Composable
fun SavedRecipeScreenPreview() {
    PancookTheme {
        SavedRecipeScreen(navController = rememberNavController())
    }
}
