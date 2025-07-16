package com.wesleyaldrich.pancook.ui.screens

import android.util.Log // Import Log for debugging
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Import this for LazyRow items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api // Import ExperimentalMaterial3Api for TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold // Import Scaffold
import androidx.compose.material3.TopAppBar // Import TopAppBar
import androidx.compose.material3.TopAppBarDefaults // Import TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins
import com.wesleyaldrich.pancook.model.Instruction // Import Instruction
import com.wesleyaldrich.pancook.model.NutritionFact // Import NutritionFact
import com.wesleyaldrich.pancook.model.Comment // Import Comment
import com.wesleyaldrich.pancook.ui.navigation.Screen // Ensure this import path is correct
// IMPORTANT: Import allRecipes from the MyRecipeScreen file
import com.wesleyaldrich.pancook.ui.screens.allRecipes
import kotlin.math.roundToInt


// Helper function for bottom border. Renamed from bottomBorder2 to bottomBorder
fun Modifier.bottomBorder2(strokeWidth: Dp, color: Color) = this.then(
    Modifier.drawBehind {
        val strokePx = strokeWidth.toPx()
        val y = size.height - strokePx / 2
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokePx
        )
    }
)

fun getDummyRecipes(): Map<Recipe, Int> {
    // This function now fetches recipes directly from the 'allRecipes' list
    // defined in MyRecipeScreen.kt, ensuring consistent IDs.
    val selectedRecipesWithServeCount = mutableMapOf<Recipe, Int>()

    // Log the size of allRecipes when getDummyRecipes is called
    Log.d("GroceryScreen", "getDummyRecipes: allRecipes size = ${allRecipes.size}")

    // Select specific recipes by their IDs from the 'allRecipes' list.
    // These IDs correspond to the recipes provided in MyRecipeScreen.kt.
    allRecipes.find { it.id == 1 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 4 // Spaghetti Bolognese
    }
    allRecipes.find { it.id == 2 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 2 // Fresh Garden Salad (Grocery)
    }
    allRecipes.find { it.id == 4 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 3 // Delicious Salad
    }
    allRecipes.find { it.id == 11 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 1 // Beef Stew
    }
    allRecipes.find { it.id == 12 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 2 // Tomato Soup
    }

    Log.d("GroceryScreen", "getDummyRecipes: Selected ${selectedRecipesWithServeCount.size} dummy recipes.")
    return selectedRecipesWithServeCount
}

// Data class to hold processed ingredient information
data class GroceryIngredient(
    val imageRes: Int,
    val name: String,
    val category: String,
    val totalUnit: Float,
    val measurement: String,
    val recipeNames: List<String>
)

// Helper function for unit conversion (example, you might need a more comprehensive one)
fun convertToGram(qty: Float, unit: String): Float {
    return when (unit.lowercase()) {
        "g" -> qty
        "kg" -> qty * 1000
        "ml" -> qty // Assuming 1ml ~ 1g for simplicity for some liquids
        "l" -> qty * 1000 // Assuming 1l ~ 1000g for simplicity
        "tbsp" -> qty * 15 // Roughly 15g per tablespoon
        "tsp" -> qty * 5 // Roughly 5g per teaspoon
        "pieces", "piece", "cloves", "head", "medium", "pcs", "stalks", "large", "cup" -> qty * 50 // Placeholder for arbitrary units, adjust as needed
        else -> qty // Default to original quantity if unit is unknown
    }
}

fun mapIngredientsToGroceryList(recipeMap: Map<Recipe, Int>): Map<String, GroceryIngredient> {
    val groceryList = mutableMapOf<String, MutableList<Pair<String, Ingredient>>>()

    recipeMap.forEach { (recipe, serveCount) ->
        recipe.ingredients.forEach { ingredient ->
            val key = ingredient.name.lowercase()
            if (groceryList.containsKey(key)) {
                groceryList[key]?.add(Pair(recipe.title, ingredient.copy(qty = ingredient.qty * serveCount)))
            } else {
                groceryList[key] = mutableListOf(Pair(recipe.title, ingredient.copy(qty = ingredient.qty * serveCount)))
            }
        }
    }

    return groceryList.mapValues { (_, ingredientPairs) ->
        val firstIngredient = ingredientPairs.first().second
        val totalUnitInGrams = ingredientPairs.sumOf { (recipeName, ingredient) ->
            convertToGram(ingredient.qty, ingredient.unitMeasurement).toDouble()
        }.toFloat()
        val recipeNames = ingredientPairs.map { it.first }.distinct()

        GroceryIngredient(
            imageRes = firstIngredient.imageRes,
            name = firstIngredient.name,
            category = firstIngredient.category,
            totalUnit = totalUnitInGrams,
            measurement = "gram", // Standardize to gram after conversion
            recipeNames = recipeNames
        )
    }.toSortedMap() // Optional: Sort by ingredient name
}

fun groupGroceryIngredientsByCategory(groceryIngredients: Map<String, GroceryIngredient>): Map<String, List<GroceryIngredient>> {
    return groceryIngredients.values.groupBy { it.category }
}

@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar
@Composable
fun GroceryScreen(
    onBackClick: () -> Unit,
    navController: NavController,
    onRemoveClick: (Recipe) -> Unit
) {
    // FIX: recipes should be a 'var' state to allow re-assignment and trigger recomposition
    var recipes by remember { mutableStateOf(getDummyRecipes()) }

    // These derived states will recompose automatically when 'recipes' changes
    val groceryIngredientsMap by remember(recipes) {
        Log.d("GroceryScreen", "Recomputing groceryIngredientsMap (triggered by recipes change). New recipes size: ${recipes.size}")
        mutableStateOf(mapIngredientsToGroceryList(recipes))
    }
    val groupedIngredients by remember(groceryIngredientsMap) {
        Log.d("GroceryScreen", "Recomputing groupedIngredients (triggered by groceryIngredientsMap change). Num categories: ${groupGroceryIngredientsByCategory(groceryIngredientsMap).size}")
        mutableStateOf(groupGroceryIngredientsByCategory(groceryIngredientsMap))
    }

    val checkedStates = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* No title needed based on your current UI */ },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(50.dp)
                                .graphicsLayer {
                                    scaleX = -1f // Flips the arrow to point left
                                }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(horizontal = 16.dp) // Re-apply horizontal padding
        ) {
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Use items from LazyRow dsl directly for more stable list items
                items(
                    items = recipes.entries.toList(), // Use items parameter
                    key = { it.key.id } // Provide a unique key for each item
                ) { (recipe, serveCount) -> // Destructure the Map.Entry
                    RecipeCardMini(
                        recipe = recipe,
                        serveCount = serveCount,
                        imagePainter = painterResource(id = recipe.image),
                        onClick = {
                            Log.d("GroceryScreen", "Attempting navigation to DetailRecipe with ID: ${recipe.id}")
                            navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
                        },
                        onRemoveClick = {
                            val removedRecipeTitle = recipe.title
                            // FIX: To trigger recomposition reliably, create a new map instance
                            // by filtering out the removed recipe and re-assign it.
                            recipes = recipes.toMutableMap().also { it.remove(recipe) }.toMap()
                            Log.d("GroceryScreen", "Recipe '$removedRecipeTitle' removed. Recipes map new size: ${recipes.size}")
                        },
                        onServeCountChange = { newCount ->
                            // FIX: Update serve count by creating a new map instance
                            recipes = recipes.toMutableMap().also { it[recipe] = newCount }.toMap()
                            Log.d("GroceryScreen", "Recipe '${recipe.title}' serve count changed to $newCount. Recipes map new size: ${recipes.size}")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Add ingredient button
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary),
                    contentColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Ingredient",
                        modifier = Modifier
                            .size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Add Ingredient",
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }

            // Column for Ingredient section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                groupedIngredients.forEach { (category, ingredientList) ->
                    CategorySectionHeader(title = category)
                    ingredientList.forEach { ingredient ->
                        IngredientCard(
                            ingredient = ingredient,
                            isChecked = checkedStates[ingredient.name] ?: false,
                            onCheckedChange = { newValue ->
                                checkedStates[ingredient.name] = newValue
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCardMini(
    recipe: Recipe,
    serveCount: Int,
    imagePainter: Painter,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onServeCountChange: (Int) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val dropdownAnchor = remember { mutableStateOf<Offset?>(null) }
    val density = LocalDensity.current

    Card(
        modifier = Modifier
            .width(185.dp)
            .height(165.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .clickable { onClick() }
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Card Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.TopCenter),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                colorResource(R.color.primary).copy(alpha = 0.8f),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(start = 12.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Serve Count",
                            tint = colorResource(R.color.accent_yellow),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = serveCount.toString(),
                            fontFamily = nunito,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = colorResource(R.color.accent_yellow),
                        )
                        Spacer(modifier = Modifier.width(2.dp))

                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.TopStart)
                                .onGloballyPositioned { coordinates ->
                                    val originalOffset = coordinates.localToWindow(Offset.Zero)
                                    val shiftedOffset =
                                        originalOffset.copy(x = originalOffset.x - 500) // shift left by 50 pixels
                                    dropdownAnchor.value = shiftedOffset
                                }
                        ) {
                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false },
                                modifier = Modifier
                                    .background(colorResource(R.color.primary))
                                    .width(70.dp)
                                    .heightIn(max = 200.dp)
                            ) {
                                (1..15).forEach { count ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "$count",
                                                fontFamily = nunito,
                                                color = Color.White
                                            )
                                        },
                                        onClick = {
                                            onServeCountChange(count)
                                            expanded.value = false
                                        }
                                    )
                                }
                            }
                            Icon( // Moved Icon inside the Box with onGloballyPositioned
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Change Serve Count",
                                tint = colorResource(R.color.accent_yellow),
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { expanded.value = true }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(R.color.primary).copy(alpha = 0.75f))
                            .clickable { onRemoveClick() }, // This calls the onRemoveClick lambda
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

                // Like count
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 8.dp)
                        .background(
                            colorResource(R.color.primary).copy(alpha = 0.75f),
                            RoundedCornerShape(20.dp)
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
                        text = recipe.upvoteCount.toString(),
                        fontFamily = nunito,
                        color = colorResource(R.color.accent_yellow),
                        fontSize = 12.sp
                    )
                }
            }

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
                    lineHeight = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 8.dp)
                )
                Text(
                    text = recipe.recipeMaker,
                    fontFamily = nunito,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CategorySectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .bottomBorder(1.dp, Color.Gray)
    ) {
        Text(
            text = title,
            fontFamily = poppins,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Composable
fun IngredientCard(
    ingredient: GroceryIngredient,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val textAlpha by animateFloatAsState(
        targetValue = if (isChecked) 0.5f else 1f,
        label = "IngredientCardAlpha"
    )
    val textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .background(
                color = colorResource(R.color.primary),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                // Only toggle expansion if ingredient is used in more than 1 recipe
                if (ingredient.recipeNames.size > 1) {
                    expanded = !expanded
                }
            }
            .animateContentSize() // This provides the animation for accordion
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image Container
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = if (isChecked) 0.3f else 1f))
                ) {
                    Image(
                        painter = painterResource(id = ingredient.imageRes),
                        contentDescription = ingredient.name,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 10.dp) // Apply left padding here for content alignment
                        .alpha(textAlpha),
                    verticalArrangement = Arrangement.Top,
                ) {
                    // Recipe name/count - Conditional display
                    Text(
                        text = if (ingredient.recipeNames.size > 1) {
                            "Used in ${ingredient.recipeNames.size} recipes"
                        } else {
                            ingredient.recipeNames.firstOrNull() ?: "N/A" // Display only recipe name
                        },
                        fontFamily = nunito,
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        color = Color.White,
                        textDecoration = textDecoration
                    )

                    // Ingredient Name
                    Text(
                        text = ingredient.name,
                        fontFamily = nunito,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        color = Color.White,
                        textDecoration = textDecoration
                    )

                    // Ingredient unit and measurement total
                    Text(
                        text = "${ingredient.totalUnit.roundToInt()} ${ingredient.measurement}",
                        fontFamily = nunito,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        color = Color.White,
                        textDecoration = textDecoration,
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                CircularCheckbox(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange
                )
            }

            // --- First Separator Line (appears when expanded, below main info) ---
            if (expanded && ingredient.recipeNames.size > 1) {
                Spacer(modifier = Modifier.height(8.dp)) // Space above the line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.5f))
                        .padding(start = 70.dp) // Indent the line
                )
                Spacer(modifier = Modifier.height(4.dp)) // Space after the line
            }

            // --- Accordion Section (list of recipe names) ---
            if (expanded && ingredient.recipeNames.size > 1) {
                Column(modifier = Modifier.padding(start = 70.dp)) { // Indent the whole accordion block
                    ingredient.recipeNames.forEachIndexed { index, recipeName ->
                        // Add separator line before each recipe name, except the first one
                        if (index > 0) {
                            Spacer(modifier = Modifier.height(4.dp)) // Space before the line
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.White.copy(alpha = 0.5f))
                            )
                            Spacer(modifier = Modifier.height(2.dp)) // Space between line and text
                        }
                        Text(
                            text = "Used in: $recipeName",
                            fontFamily = nunito,
                            fontSize = 12.sp,
                            color = Color.White,
                            textDecoration = textDecoration,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, device = Devices.PIXEL_2)
@Composable
private fun GroceryPreview() {
    PancookTheme {
        GroceryScreen(
            onBackClick = {}, // For preview, this can remain empty as it's not a real navigation context
            navController = rememberNavController(),
            onRemoveClick = {},
        )
    }
}