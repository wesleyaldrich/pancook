package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import com.wesleyaldrich.pancook.ui.theme.poppins
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import com.wesleyaldrich.pancook.ui.components.RecipeReusableCard
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.remember
import com.wesleyaldrich.pancook.ui.screens.allRecipes // Import allRecipes for data source
import com.wesleyaldrich.pancook.ui.screens.bookmarkedRecipes // Import bookmarkedRecipes for bookmarking logic
import com.wesleyaldrich.pancook.ui.navigation.Screen // Import Screen for navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(categoryName: String, navController: NavController) {
    // Filter recipes based on the category name from the global allRecipes list
    val filteredRecipes = remember(categoryName) {
        allRecipes.filter { recipe ->
            // Check if any ingredient in the recipe matches the selected category name
            recipe.ingredients.any { ingredient ->
                ingredient.category.equals(categoryName, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = categoryName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppins,
                        color = colorResource(R.color.primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = colorResource(R.color.primary)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display a message if no recipes are found for the category
            if (filteredRecipes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No recipes found for '$categoryName' category.",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            } else {
                // Display the filtered recipes in a grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredRecipes) { recipe ->
                        RecipeReusableCard(
                            imagePainter = painterResource(id = recipe.images.first()),
                            title = recipe.title,
                            description = recipe.recipeMaker,
                            duration = recipe.duration,
                            likeCount = recipe.upvoteCount,
                            onClick = { navController.navigate(Screen.DetailRecipe.createRoute(recipe.id)) },
                            // Check bookmark status against the canonical allRecipes list
                            isBookmarked = bookmarkedRecipes.contains(allRecipes.find { it.id == recipe.id }),
                            onBookmarkClick = {
                                // Find the canonical recipe object to ensure consistent bookmarking
                                val canonicalRecipe = allRecipes.find { it.id == recipe.id }
                                if (canonicalRecipe != null) {
                                    if (bookmarkedRecipes.contains(canonicalRecipe)) {
                                        bookmarkedRecipes.remove(canonicalRecipe)
                                    } else {
                                        bookmarkedRecipes.add(canonicalRecipe)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 800)
@Composable
fun CategoryScreenPreview() {
    PancookTheme {
        CategoryScreen(categoryName = "Pasta", navController = rememberNavController())
    }
}
