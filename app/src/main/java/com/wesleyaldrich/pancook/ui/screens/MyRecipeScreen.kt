package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Import clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import com.wesleyaldrich.pancook.ui.components.ReusableCard
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController // Import NavController
import androidx.navigation.compose.rememberNavController // For preview only
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.navigation.Screen // Import Screen

@Composable
fun MyRecipeScreen(navController: NavController) { // Accept NavController
    val recipes = remember { mutableStateListOf<Recipe>() }

    LaunchedEffect(Unit) {
        recipes.addAll(
            listOf(
                Recipe(
                    id = 4,
                    title = "Delicious Salad",
                    description = "by Me",
                    image = R.drawable.salad,
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 3, "pcs")
                    ),
                    steps = listOf("Step 1", "Step 2"),
                    servings = 2,
                    duration = "15 min",
                    upvoteCount = 1234 // Changed to upvoteCount
                ),
                Recipe(
                    id = 5,
                    image = R.drawable.salad,
                    title = "Spicy Noodles",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "20 min",
                    upvoteCount = 567 // Changed to upvoteCount
                ),
                Recipe(
                    id = 6,
                    image = R.drawable.salad,
                    title = "Chicken Stir-fry",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "30 min",
                    upvoteCount = 890 // Changed to upvoteCount
                ),
                Recipe(
                    id = 7,
                    image = R.drawable.salad,
                    title = "Vegetable Curry",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "45 min",
                    upvoteCount = 123 // Changed to upvoteCount
                ),
                Recipe(
                    id = 8,
                    image = R.drawable.salad,
                    title = "Creamy Pasta",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "25 min",
                    upvoteCount = 456 // Changed to upvoteCount
                ),
                Recipe(
                    id = 9,
                    image = R.drawable.salad,
                    title = "Grilled Fish",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "35 min",
                    upvoteCount = 789 // Changed to upvoteCount
                ),
                Recipe(
                    id = 11,
                    image = R.drawable.salad,
                    title = "Beef Stew",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "60 min",
                    upvoteCount = 101 // Changed to upvoteCount
                ),
                Recipe(
                    id = 12,
                    image = R.drawable.salad,
                    title = "Tomato Soup",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "10 min",
                    upvoteCount = 202 // Changed to upvoteCount
                )
            )
        )
    }


    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(recipes) { recipe ->
                ReusableCard(
                    imagePainter = painterResource(id = recipe.image),
                    title = recipe.title,
                    description = recipe.description,
                    duration = recipe.duration,
                    upvoteCount = recipe.upvoteCount, // Changed to upvoteCount
                    modifier = Modifier.clickable { // Add clickable modifier
                        navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyRecipeScreenPreview() {
    PancookTheme {
        // Provide a dummy NavController for preview purposes
        MyRecipeScreen(navController = rememberNavController())
    }
}