package com.wesleyaldrich.pancook.ui.screens

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins

fun getDummyRecipes(): List<Recipe> {
    val ingredientList1 = listOf(
        Ingredient("Spaghetti", 200, "g"),
        Ingredient("Ground Beef", 150, "g"),
        Ingredient("Tomato Sauce", 100, "ml"),
        Ingredient("Onion", 1, "piece"),
        Ingredient("Garlic", 2, "cloves")
    )

    val ingredientList2 = listOf(
        Ingredient("Lettuce", 1, "head"),
        Ingredient("Tomato", 2, "pieces"),
        Ingredient("Cucumber", 1, "piece"),
        Ingredient("Olive oil", 2, "tbsp"),
        Ingredient("Feta cheese", 50, "g")
    )

    val ingredientList3 = listOf(
        Ingredient("Chicken Breast", 200, "g"),
        Ingredient("Salt", 1, "tsp"),
        Ingredient("Pepper", 1/2, "tsp"),
        Ingredient("Rosemary", 1, "tsp"),
        Ingredient("Olive oil", 1, "tbsp")
    )

    return listOf(
        Recipe(
            id = 1,
            title = "Spaghetti Bolognese",
            description = "A classic Italian pasta dish with meat sauce.",
            image = R.drawable.hash_brown,
            ingredients = ingredientList1,
            steps = listOf("Boil pasta", "Cook beef", "Add sauce", "Mix together"),
            servings = 2,
            duration = "30 min",
            likeCount = 124
        ),
        Recipe(
            id = 2,
            title = "Fresh Garden Salad",
            description = "A healthy and fresh salad mix.",
            image = R.drawable.salad,
            ingredients = ingredientList2,
            steps = listOf("Chop veggies", "Toss with oil", "Add cheese"),
            servings = 1,
            duration = "15 min",
            likeCount = 89
        ),
        Recipe(
            id = 3,
            title = "Grilled Chicken",
            description = "Juicy grilled chicken breast with herbs.",
            image = R.drawable.fudgy_brownies,
            ingredients = ingredientList3,
            steps = listOf("Season chicken", "Heat pan", "Grill 7 min each side"),
            servings = 2,
            duration = "25 min",
            likeCount = 176
        )
    )
}

@Composable
fun GroceryScreen(
//    recipe: List<Recipe>,
    onBackClick: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    onRemoveClick: (Recipe) -> Unit
)
{
    val recipes = getDummyRecipes()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Back Button
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Back",
                modifier = Modifier
                    .size(50.dp)
                    .graphicsLayer {
                        scaleX = -1f
                    }
            )
        }

        Spacer(modifier = Modifier.height(0.dp))

        // 94. Grocery list header
        Text(
            text = "Groceries To Get",
            fontSize = 24.sp,
            fontFamily = poppins,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(0.dp))

        // 95. Recipe Section Header
        Text(
            text = "Recipes",
            fontSize = 18.sp,
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Horizontal carousel
        LazyRow (
//            contentPadding = PaddingValues(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            recipes.forEach { recipe ->
                item {
                    RecipeCardMini(
                        recipe = recipe,
                        imagePainter = painterResource(id = recipe.image),
                        onClick = {onRecipeClick(recipe)},
                        onRemoveClick = { onRemoveClick(recipe) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeCardMini(
    recipe: Recipe,
    imagePainter: Painter,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Card Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
//                        .height(180.dp)
                )

                // Top Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.TopCenter),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row (modifier = Modifier
                        .background(
                            colorResource(R.color.primary).copy(alpha = 0.8f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Likes",
                            tint = colorResource(R.color.accent_yellow),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = recipe.duration,
                            fontFamily = nunito,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = colorResource(R.color.accent_yellow),
                        )
                    }
                    Row {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(colorResource(R.color.primary).copy(alpha = 0.75f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Delete",
                                tint = colorResource(R.color.accent_yellow),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Like row
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 8.dp)
                        .background(
                            colorResource(R.color.primary).copy(alpha = 0.75f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Likes",
                        tint = colorResource(R.color.accent_yellow),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = recipe.likeCount.toString(),
                        fontFamily = nunito,
                        color = colorResource(R.color.accent_yellow),
                        fontSize = 12.sp
                    )
                }
            }

            // Recipe detail
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = recipe.title,
                    fontFamily = poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
                Text(
                    text = "by Nunuk",
                    fontFamily = nunito,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun GroceryPreview() {
    GroceryScreen(
        onBackClick = {},
        onRecipeClick = {},
        onRemoveClick = {},
    )
}