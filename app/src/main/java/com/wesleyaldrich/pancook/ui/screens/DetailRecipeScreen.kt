package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Ingredient
import kotlin.math.floor
import kotlin.math.log
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.navigation.Screen // Import the Screen object

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRecipeScreen(recipeId: Int, navController: NavController) {
    var servingCount by remember { mutableStateOf(1) }

    // This should ideally come from a ViewModel or a data source based on recipeId
    val deliciousSaladRecipe = remember {
        Recipe(
            id = 4,
            title = "Delicious Salad",
            description = "A refreshing mix of garden greens.",
            image = R.drawable.salad,
            ingredients = listOf(
                Ingredient(R.drawable.ingredient_tomato, "Fresh Lettuce", "Vegetables", 1.0f, "head"),
                Ingredient(R.drawable.ingredient_tomato, "Cherry Tomatoes", "Vegetables", 150.0f, "g"),
                Ingredient(R.drawable.ingredient_tomato, "Cucumber", "Vegetables", 0.5f, "pcs"),
                Ingredient(R.drawable.ingredient_tomato, "Red Onion", "Vegetables", 0.25f, "pcs"),
                Ingredient(R.drawable.ingredient_tomato, "Feta Cheese", "Dairy", 50.0f, "g"),
                Ingredient(R.drawable.ingredient_tomato, "Olive Oil", "Condiments", 2.0f, "tbsp"),
                Ingredient(R.drawable.ingredient_tomato, "Lemon Juice", "Condiments", 1.0f, "tbsp"),
                Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5f, "tsp"),
                Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25f, "tsp"),
                Ingredient(R.drawable.ingredient_tomato, "Egg", "Seasoning", 2.05f, "pcs")
            ),
            steps = listOf(
                "Wash and dry the lettuce. Tear into bite-sized pieces and place in a large salad bowl.",
                "Halve the cherry tomatoes. Dice the cucumber and finely slice the red onion.",
                "Add the tomatoes, cucumber, and red onion to the salad bowl with the lettuce.",
                "Crumble the feta cheese over the vegetables.",
                "Boil 2 large eggs for 8-10 minutes for a hard-boiled consistency. Let them cool, peel, and quarter them.", // NEW STEP
                "In a small bowl, whisk together the olive oil, lemon juice, salt, and black pepper to make the dressing.",
                "Pour the dressing over the salad. Toss gently to combine all ingredients evenly.",
                "Add the quartered boiled eggs to the salad.", // Updated to include eggs
                "Serve immediately as a refreshing side or light meal.",
                "Enjoy your delicious salad!"
            ),
            servings = 2,
            duration = "15 min",
            upvoteCount = 1234,
            recipeMaker = "by Chef Ana"
        )
    }

    val currentRecipe = deliciousSaladRecipe // Using the hardcoded recipe for now

    val upvoteCount = currentRecipe.upvoteCount
    val bookmarkCount = 3200
    val shareCount = 567

    fun formatCount(count: Int): String {
        if (count < 1000) return count.toString()
        val exp = floor(log(count.toDouble(), 1000.0)).toInt()
        val units = arrayOf("", "k", "M", "B", "T")
        val formattedValue = String.format("%.1f", count / Math.pow(1000.0, exp.toDouble()))
        return formattedValue.replace(".0", "") + units[exp]
    }

    val nutritionFacts = remember {
        listOf(
            "250 kcal",
            "20g",
            "15g",
            "10g"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = "Back",
                            tint = colorResource(R.color.primary).copy(alpha = 1f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                        ) {
                            LazyRow(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(listOf(currentRecipe.image)) { imgResId ->
                                    Image(
                                        painter = painterResource(id = imgResId),
                                        contentDescription = "Recipe Image",
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .fillMaxHeight(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(colorResource(R.color.primary).copy(alpha = if (it == 0) 0.8f else 0.4f)) // Changed color
                                )
                                if (it < 2) Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentRecipe.title,
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.nopal),
                                            contentDescription = "Creator Profile",
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surfaceVariant), // Changed color
                                            contentScale = ContentScale.Crop,
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = currentRecipe.recipeMaker,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 16.sp, color = colorResource(R.color.primary).copy(alpha = 1f))
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_clock),
                                            contentDescription = "Duration",
                                            modifier = Modifier.size(20.dp),
                                            tint = colorResource(R.color.primary).copy(alpha = 1f)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(text = currentRecipe.duration, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp))
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { /* Handle Upvote */ },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.ThumbUp, contentDescription = "Upvote", tint = colorResource(R.color.primary).copy(alpha = 1f))
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(upvoteCount),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { /* Handle Saved */ },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.Bookmark, contentDescription = "Saved", tint = colorResource(R.color.primary).copy(alpha = 1f))
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(bookmarkCount),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { /* Handle Share */ },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.Share, contentDescription = "Share", tint = colorResource(R.color.primary).copy(alpha = 1f))
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(shareCount),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                    )
                                }
                            }
                        }

                        // Adjusted from 16.dp to 6.dp to create a 12dp gap with the next card's top padding
                        Text(
                            text = currentRecipe.description,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Changed vertical padding from 8.dp to 6.dp
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Ingredients",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                    modifier = Modifier.weight(1f)
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_serving),
                                        contentDescription = "Serving Count",
                                        modifier = Modifier.size(20.dp),
                                        tint = colorResource(R.color.primary).copy(alpha = 1f)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))

                                    Button(
                                        onClick = { if (servingCount > 1) servingCount-- },
                                        modifier = Modifier.size(20.dp),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Serving", tint = colorResource(R.color.accent_yellow).copy(alpha = 1f), modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "$servingCount",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    Button(
                                        onClick = { servingCount++ },
                                        modifier = Modifier.size(20.dp),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Serving", tint = colorResource(R.color.accent_yellow).copy(alpha = 1f), modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            val ingredientValueBoxWidth = 50.dp
                            val indentSpacerWidth = 12.dp

                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                currentRecipe.ingredients.forEach { ingredient ->
                                    val multipliedQty = ingredient.qty * servingCount
                                    val formattedQty = "%.2f".format(multipliedQty)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(width = ingredientValueBoxWidth, height = 20.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Text(
                                                text = formattedQty,
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                                                color = colorResource(R.color.accent_yellow).copy(alpha = 1f),
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.padding(end = 8.dp),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = ingredient.unitMeasurement,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.width(40.dp)
                                        )
                                        Spacer(modifier = Modifier.width(indentSpacerWidth))

                                        Text(
                                            text = ingredient.name,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    // Nutrition Section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Changed vertical padding from 8.dp to 6.dp
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) { // Uniform padding for content
                            Text(
                                text = "Nutrition",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.Top
                            ) {
                                val nutritionLabels = listOf("Calories", "Protein", "Fat", "Carbs")
                                val fixedNutritionItemWidth = 80.dp

                                nutritionLabels.forEachIndexed { index, label ->
                                    val value = nutritionFacts[index]
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(fixedNutritionItemWidth)
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .height(32.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = value,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                                                color = colorResource(R.color.accent_yellow).copy(alpha = 1f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Instructions Section (Now includes steps)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Changed vertical padding from 8.dp to 6.dp
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) { // Uniform padding for content
                            Text(
                                text = "Instructions",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                modifier = Modifier.padding(bottom = 8.dp) // Maintain a small gap below header
                            )

                            // Instructions steps (now within this Column, inside the Card)
                            currentRecipe.steps.forEach { step ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp) // Reduced vertical padding between steps for a tighter look
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(CircleShape)
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "${currentRecipe.steps.indexOf(step) + 1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = step,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    // Comments Section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Changed vertical padding from 8.dp to 6.dp
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) { // Uniform padding for content
                            Text(
                                text = "Comments",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_serving),
                                    contentDescription = "User Avatar",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(colorResource(R.color.accent_yellow).copy(alpha = 1f)), // Changed color
                                    contentScale = ContentScale.Crop,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "John Doe", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(text = "This is a placeholder comment. Great recipe!", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_serving),
                                    contentDescription = "User Avatar",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant), // Changed color
                                    contentScale = ContentScale.Crop,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Jane Smith", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(text = "Another placeholder comment. I would try this!", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
                                }
                            }
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                MaterialTheme.colorScheme.surface
                            ),
                            startY = 0f,
                            endY = 300f
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Handle add to planner */ },
                    modifier = Modifier
                        .weight(0.5f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add to Planner")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Add to Planner", fontSize = 15.5.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        // Navigate to the Instruction screen, starting at step 0
                        navController.navigate(Screen.Instruction.createRoute(recipeId)) // No stepIndex here
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Start Instruction", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 1700
)
@Composable
fun DetailRecipeScreenPreview() {
    PancookTheme {
        DetailRecipeScreen(recipeId = 1, navController = rememberNavController())
    }
}