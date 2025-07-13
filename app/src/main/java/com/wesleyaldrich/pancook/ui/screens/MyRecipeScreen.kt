package com.wesleyaldrich.pancook.ui.screens

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
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe

@Composable
fun MyRecipeScreen() {
    val recipes = remember { mutableStateListOf<Recipe>() }

    LaunchedEffect(Unit) {
        recipes.addAll(
            listOf(
                Recipe(
                    id = 4,
                    image = R.drawable.salad,
                    title = "Delicious Salad",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "15 min",
                    likeCount = 1234 
                ),
                Recipe(
                    id = 5,
                    image = R.drawable.salad,
                    title = "Spicy Noodles",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "20 min",
                    likeCount = 567
                ),
                Recipe(
                    id = 6,
                    image = R.drawable.salad,
                    title = "Chicken Stir-fry",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "30 min",
                    likeCount = 890
                ),
                Recipe(
                    id = 7,
                    image = R.drawable.salad,
                    title = "Vegetable Curry",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "45 min",
                    likeCount = 123
                ),
                Recipe(
                    id = 8,
                    image = R.drawable.salad,
                    title = "Creamy Pasta",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "25 min",
                    likeCount = 456
                ),
                Recipe(
                    id = 9,
                    image = R.drawable.salad,
                    title = "Grilled Fish",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "35 min",
                    likeCount = 789
                ),
                Recipe(
                    id = 11,
                    image = R.drawable.salad,
                    title = "Beef Stew",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "60 min",
                    likeCount = 101
                ),
                Recipe(
                    id = 12,
                    image = R.drawable.salad,
                    title = "Tomato Soup",
                    description = "by Me",
                    steps = listOf("Step 1", "Step 2"),
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    servings = 2,
                    duration = "10 min",
                    likeCount = 202
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
                    likeCount = recipe.likeCount
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyRecipeScreenPreview() {
    PancookTheme {
        MyRecipeScreen()
    }
}