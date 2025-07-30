// ui/screens/SavedRecipeScreen.kt
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

// Import for Scaffold, TopAppBar, IconButton, Icon, Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api // Required for TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.res.colorResource
import com.wesleyaldrich.pancook.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wesleyaldrich.pancook.ui.theme.poppins
import androidx.compose.runtime.LaunchedEffect // Required for LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class) // Add this annotation
@Composable
fun SavedRecipeScreen(
    navController: NavController,
    onBackClick: () -> Unit // ADD THIS PARAMETER
) {
    // Ensure upvotedRecipes state is reflected in allRecipes for consistent upvote status display
    LaunchedEffect(upvotedRecipes.toList()) {
        allRecipes.forEach { recipe ->
            recipe.isUpvoted = upvotedRecipes.contains(recipe)
        }
    }

    Scaffold( // Wrap your content with Scaffold
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Saved Recipe",
                        fontSize = 24.sp, // Slightly smaller font for the top bar
                        fontFamily = poppins,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        // Change icon to ArrowBack and remove graphicsLayer if you want it to match GroceryScreen exactly
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, // Changed from PlayArrow
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues -> // Add paddingValues to the content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(horizontal = 8.dp) // Maintain horizontal padding
        ) {
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
                        val isUpvoted = remember(recipe.id) { upvotedRecipes.contains(recipe) } // Get upvote status for the recipe

                        ReusableCard(
                            imagePainter = painterResource(id = recipe.images.first()),
                            title = recipe.title,
                            description = recipe.recipeMaker,
                            duration = recipe.duration,
                            upvoteCount = recipe.upvoteCount,
                            isBookmarked = isBookmarked,
                            onBookmarkClick = {
                                if (isBookmarked) {
                                    bookmarkedRecipes.remove(recipe)
                                } else {
                                    bookmarkedRecipes.add(recipe)
                                }
                            },
                            onDeleteClick = {
                                bookmarkedRecipes.remove(recipe)
                            },
                            hideDeleteButton = true,
                            isUpvoted = isUpvoted, // Pass isUpvoted to ReusableCard
                            onUpvoteClick = { // Add onUpvoteClick lambda for the upvote button
                                if (upvotedRecipes.contains(recipe)) {
                                    upvotedRecipes.remove(recipe)
                                    recipe.upvoteCount--
                                } else {
                                    upvotedRecipes.add(recipe)
                                    recipe.upvoteCount++
                                }
                            },
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
                            }
                        )
                    }
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
        SavedRecipeScreen(navController = rememberNavController(), onBackClick = {}) // Update preview call
    }
}