package com.wesleyaldrich.pancook.ui.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.wesleyaldrich.pancook.model.Instruction
import com.wesleyaldrich.pancook.model.NutritionFact
import com.wesleyaldrich.pancook.model.Comment
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.ui.screens.allRecipes
import kotlin.math.roundToInt

// Imports for ModalBottomSheet
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

// Import missing composables from the same package (assuming they are in PlannerScreen.kt)
import com.wesleyaldrich.pancook.ui.screens.CategorySectionHeader // Added missing import
import com.wesleyaldrich.pancook.ui.screens.IngredientCard // Added missing import
import com.wesleyaldrich.pancook.ui.screens.CircularCheckbox // Added missing import (used in IngredientCard)


// Helper function for bottom border.
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

// Define a unique ID for manually added ingredients recipe
const val MANUAL_ENTRY_RECIPE_ID = -1

fun getDummyRecipes(): Map<Recipe, Int> {
    val selectedRecipesWithServeCount = mutableMapOf<Recipe, Int>()

    Log.d("GroceryScreen", "getDummyRecipes: allRecipes size = ${allRecipes.size}")

    allRecipes.find { it.id == 1 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 4
    }
    allRecipes.find { it.id == 2 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 2
    }
    allRecipes.find { it.id == 4 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 3
    }
    allRecipes.find { it.id == 11 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 1
    }
    allRecipes.find { it.id == 12 }?.let { recipe ->
        selectedRecipesWithServeCount[recipe] = 2
    }

    // Add a placeholder recipe for manual entries if it doesn't exist already
    val manualEntryRecipe = Recipe(
        id = MANUAL_ENTRY_RECIPE_ID,
        title = "Personal Ingredients",
        description = "Ingredients added manually",
        images = listOf(R.drawable.rawon), // Used rawon.jpg as per your files
        ingredients = emptyList(),
        steps = emptyList(),
        servings = 1,
        duration = "",
        upvoteCount = 0,
        recipeMaker = "You",
        nutritionFacts = emptyList(),
        comments = emptyList()
    )
    if (!selectedRecipesWithServeCount.keys.any { it.id == MANUAL_ENTRY_RECIPE_ID }) {
        selectedRecipesWithServeCount[manualEntryRecipe] = 1
    }

    Log.d("GroceryScreen", "getDummyRecipes: Selected ${selectedRecipesWithServeCount.size} dummy recipes.")
    return selectedRecipesWithServeCount
}

// Data class to hold processed ingredient information
data class GroceryIngredient(
    val imageRes: Int,
    val name: String,
    val category: String,
    val totalUnit: Double, // Changed to Double
    val measurement: String,
    val recipeNames: List<String>
)

// Helper function for unit conversion (example, you might need a more comprehensive one)
fun convertToGram(qty: Double, unit: String): Double { // Changed qty to Double, return to Double
    return when (unit.lowercase()) {
        "g" -> qty
        "kg" -> qty * 1000
        "ml" -> qty // Assuming 1ml ~ 1g for simplicity for some liquids
        "l" -> qty * 1000 // Assuming 1l ~ 1000g for simplicity
        "tbsp" -> qty * 15 // Roughly 15g per tablespoon
        "tsp" -> qty * 5 // Roughly 5g per teaspoon
        "pieces", "piece", "cloves", "head", "medium", "pcs", "stalks", "large", "cup" -> qty * 50.0 // Placeholder for arbitrary units, adjust as needed, added .0 for Double
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
            convertToGram(ingredient.qty, ingredient.unitMeasurement)
        }
        // DEFINE recipeNames HERE, BEFORE GroceryIngredient constructor
        val recipeNames = ingredientPairs.map { it.first }.distinct() // This line was missing or misplaced

        GroceryIngredient(
            imageRes = firstIngredient.imageRes,
            name = firstIngredient.name,
            category = firstIngredient.category,
            totalUnit = totalUnitInGrams,
            measurement = "gram", // Standardize to gram after conversion
            recipeNames = recipeNames // Now recipeNames is resolved
        )
    }.toSortedMap()
}

fun groupGroceryIngredientsByCategory(groceryIngredients: Map<String, GroceryIngredient>): Map<String, List<GroceryIngredient>> {
    return groceryIngredients.values.groupBy { it.category }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryScreen(
    onBackClick: () -> Unit,
    navController: NavController,
    onRemoveClick: (Recipe) -> Unit
) {
    var recipes by remember { mutableStateOf(getDummyRecipes()) }
    var checkedStates by remember { mutableStateOf(mapOf<String, Boolean>()) }
    var showAddIngredientForm by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val allGroceryIngredientsMap by remember(recipes) {
        Log.d("GroceryScreen", "Recomputing allGroceryIngredientsMap (triggered by recipes change).")
        mutableStateOf(mapIngredientsToGroceryList(recipes))
    }

    val neededIngredientsGrouped = remember(allGroceryIngredientsMap, checkedStates) {
        Log.d("GroceryScreen", "Recomputing neededIngredientsGrouped. Current checked items count: ${checkedStates.filter { it.value }.size}")
        allGroceryIngredientsMap.values
            .filter { !checkedStates[it.name].orFalse() }
            .groupBy { it.category }
            .toSortedMap()
    }
    val gottenIngredientsGrouped = remember(allGroceryIngredientsMap, checkedStates) {
        Log.d("GroceryScreen", "Recomputing gottenIngredientsGrouped. Current checked items count: ${checkedStates.filter { it.value }.size}")
        allGroceryIngredientsMap.values
            .filter { checkedStates[it.name].orFalse() }
            .groupBy { it.category }
            .toSortedMap()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(50.dp)
                                .graphicsLayer {
                                    scaleX = -1f
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
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Groceries To Get",
                fontSize = 24.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(0.dp))

            Text(
                text = "Recipes",
                fontSize = 18.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            LazyRow (
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = recipes.entries.toList(),
                    key = { it.key.id }
                ) { (recipe, serveCount) ->
                    RecipeCardMini(
                        recipe = recipe,
                        serveCount = serveCount,
                        imagePainter = painterResource(id = recipe.images.first()), // Use the first image from the list
                        onClick = {
                            Log.d("GroceryScreen", "Attempting navigation to DetailRecipe with ID: ${recipe.id}")
                            navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
                        },
                        onRemoveClick = {
                            val removedRecipeTitle = recipe.title
                            recipes = recipes.toMutableMap().also { it.remove(recipe) }.toMap()
                            Log.d("GroceryScreen", "Recipe '$removedRecipeTitle' removed. Recipes map new size: ${recipes.size}")
                        },
                        onServeCountChange = { newCount ->
                            recipes = recipes.toMutableMap().also { it[recipe] = newCount }.toMap()
                            Log.d("GroceryScreen", "Recipe '${recipe.title}' serve count changed to $newCount. Recipes map new size: ${recipes.size}")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Button(
                onClick = { showAddIngredientForm = true },
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                if (neededIngredientsGrouped.isEmpty()) {
                    Text(
                        text = "All groceries acquired! Add more recipes to get started.",
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontFamily = nunito,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                } else {
                    neededIngredientsGrouped.forEach { (category, ingredientList) ->
                        CategorySectionHeader(title = category)
                        ingredientList.forEach { ingredient ->
                            IngredientCard(
                                ingredient = ingredient,
                                isChecked = checkedStates[ingredient.name].orFalse(),
                                onCheckedChange = { newValue: Boolean -> // Explicitly specify type for clarity
                                    checkedStates = checkedStates.toMutableMap().also { it[ingredient.name] = newValue }.toMap()
                                    Log.d("GroceryScreen", "Ingredient ${ingredient.name} checked: $newValue. New checkedStates value: ${checkedStates[ingredient.name]}")
                                }
                            )
                        }
                    }
                }
            }

            if (gottenIngredientsGrouped.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Gray.copy(alpha = 0.5f))
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Acquired Ingredients",
                    fontSize = 20.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    gottenIngredientsGrouped.forEach { (category, ingredientList) ->
                        CategorySectionHeader(title = category)
                        ingredientList.forEach { ingredient ->
                            IngredientCard(
                                ingredient = ingredient,
                                isChecked = checkedStates[ingredient.name].orFalse(),
                                onCheckedChange = { newValue: Boolean -> // Explicitly specify type for clarity
                                    checkedStates = checkedStates.toMutableMap().also { it[ingredient.name] = newValue }.toMap()
                                    Log.d("GroceryScreen", "Ingredient ${ingredient.name} unchecked from Gotten: $newValue. New checkedStates value: ${checkedStates[ingredient.name]}")
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showAddIngredientForm) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showAddIngredientForm = false
                        }
                    }
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                var ingredientNameInput by remember { mutableStateOf("") }
                var unitAmountInput by remember { mutableStateOf("") }
                val unitMeasurements = listOf(
                    "gram", "kilogram", "onz", "ml", "cup", "for taste", "as needed", "oz", "l", "bar", "pcs", "piece", "cloves", "head", "medium", "stalks", "large"
                )
                var selectedUnitMeasurement by remember { mutableStateOf(unitMeasurements[0]) }

                val categories = listOf(
                    "Vegetable", "Pasta", "Meat", "Spices", "Condiments", "Dairy", "Grains", "Herbs", "Fruit", "Seafood", "Bakery", "Sweeteners", "Baking", "Flavoring", "Seasoning", "Other"
                )
                var selectedCategory by remember { mutableStateOf(categories[0]) }
                var expandedCategoryDropdown by remember { mutableStateOf(false) }

                var expandedUnitDropdown by remember { mutableStateOf(false) }

                val formFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.primary),
                    unfocusedContainerColor = colorResource(R.color.primary),
                    disabledContainerColor = colorResource(R.color.primary),
                    errorContainerColor = colorResource(R.color.primary),

                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    errorTextColor = Color.White,

                    focusedLabelColor = Color.White.copy(alpha = 0.7f),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    disabledLabelColor = Color.White.copy(alpha = 0.5f),
                    errorLabelColor = Color.White,

                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    focusedTrailingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    disabledTrailingIconColor = Color.White,
                    errorTrailingIconColor = Color.White,
                )

                val formOutlinedFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.primary),
                    unfocusedContainerColor = colorResource(R.color.primary),
                    disabledContainerColor = colorResource(R.color.primary),
                    errorContainerColor = colorResource(R.color.primary),

                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    errorTextColor = Color.White,

                    focusedLabelColor = Color.White.copy(alpha = 0.7f),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    disabledLabelColor = Color.White.copy(alpha = 0.5f),
                    errorLabelColor = Color.White,

                    cursorColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,

                    focusedTrailingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    disabledTrailingIconColor = Color.White,
                    errorTrailingIconColor = Color.White,
                )


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Add New Ingredient",
                        style = MaterialTheme.typography.headlineSmall.copy(fontFamily = nunito),
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.primary)
                    )

                    TextField(
                        value = ingredientNameInput,
                        onValueChange = { ingredientNameInput = it },
                        label = { Text("Ingredient Name", fontFamily = nunito) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        colors = formFieldColors
                    )

                    OutlinedTextField(
                        value = unitAmountInput,
                        onValueChange = { newValue ->
                            // Allow only digits and a single decimal point
                            if (newValue.all { it.isDigit() || (it == '.' && !unitAmountInput.contains('.')) }) {
                                unitAmountInput = newValue
                            }
                        },
                        label = { Text("Unit Amount", fontFamily = nunito) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        colors = formOutlinedFieldColors
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedCategoryDropdown,
                        onExpandedChange = { expandedCategoryDropdown = !expandedCategoryDropdown },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category", fontFamily = nunito) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryDropdown)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            colors = formOutlinedFieldColors
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCategoryDropdown,
                            onDismissRequest = { expandedCategoryDropdown = false }
                        ) {
                            categories.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption, fontFamily = nunito) },
                                    onClick = {
                                        selectedCategory = selectionOption
                                        expandedCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedUnitDropdown,
                        onExpandedChange = { expandedUnitDropdown = !expandedUnitDropdown },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedUnitMeasurement,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Unit Measurement", fontFamily = nunito) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnitDropdown)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            colors = formOutlinedFieldColors
                        )
                        ExposedDropdownMenu(
                            expanded = expandedUnitDropdown,
                            onDismissRequest = { expandedUnitDropdown = false }
                        ) {
                            unitMeasurements.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption, fontFamily = nunito) },
                                    onClick = {
                                        selectedUnitMeasurement = selectionOption
                                        expandedUnitDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                ingredientNameInput = ""
                                unitAmountInput = ""
                                selectedUnitMeasurement = unitMeasurements[0]
                                selectedCategory = categories[0]
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showAddIngredientForm = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cancel", fontFamily = nunito)
                        }

                        Button(
                            onClick = {
                                val name = ingredientNameInput.trim()
                                val amount = unitAmountInput.toDoubleOrNull()
                                val measurement = selectedUnitMeasurement
                                val category = selectedCategory

                                if (name.isNotEmpty() && amount != null && amount > 0) {
                                    val newIngredient = Ingredient(
                                        imageRes = R.drawable.ingredient_tomato, // Keeping generic tomato for all ingredients
                                        name = name,
                                        category = category,
                                        qty = amount,
                                        unitMeasurement = measurement
                                    )

                                    val currentManualEntryRecipe = recipes.keys.find { it.id == MANUAL_ENTRY_RECIPE_ID }
                                    if (currentManualEntryRecipe != null) {
                                        val updatedIngredients = currentManualEntryRecipe.ingredients.toMutableList().apply {
                                            add(newIngredient)
                                        }
                                        val updatedManualEntryRecipe = currentManualEntryRecipe.copy(ingredients = updatedIngredients)
                                        recipes = recipes.toMutableMap().also {
                                            it[updatedManualEntryRecipe] = recipes[currentManualEntryRecipe] ?: 1
                                        }.toMap()
                                        Log.d("GroceryScreen", "Added personal ingredient: $name. Personal Ingredients recipe updated.")
                                    } else {
                                        val newManualEntryRecipe = Recipe(
                                            id = MANUAL_ENTRY_RECIPE_ID,
                                            title = "Personal Ingredients",
                                            description = "Ingredients added manually",
                                            images = listOf(R.drawable.rawon), // Use rawon.jpg as per your files
                                            ingredients = listOf(newIngredient),
                                            steps = emptyList(), servings = 1, duration = "", upvoteCount = 0, recipeMaker = "You", nutritionFacts = emptyList(), comments = emptyList()
                                        )
                                        recipes = recipes.toMutableMap().also { it[newManualEntryRecipe] = 1 }.toMap()
                                        Log.d("GroceryScreen", "Added personal ingredient: $name. New Personal Ingredients recipe created.")
                                    }

                                    ingredientNameInput = ""
                                    unitAmountInput = ""
                                    selectedUnitMeasurement = unitMeasurements[0]
                                    selectedCategory = categories[0]
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showAddIngredientForm = false
                                        }
                                    }
                                } else {
                                    Log.w("GroceryScreen", "Invalid input for adding ingredient: Name:'$name', Amount:'$amount'.")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary))
                        ) {
                            Text("Add Ingredient", fontFamily = nunito)
                        }
                    }
                }
            }
        }
    }
}

fun Boolean?.orFalse(): Boolean = this ?: false

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
                                        originalOffset.copy(x = originalOffset.x - 500)
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
                            Icon(
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
                            .clickable { onRemoveClick() },
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

// These composables are assumed to be in the same package (com.wesleyaldrich.pancook.ui.screens)
// or a common components package. If they are in a different package,
// you would need to adjust the import statements accordingly.
@Composable
fun CategorySectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .bottomBorder2(1.dp, Color.Gray) // Using bottomBorder2 as defined in GroceryScreen.kt
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
                // Only toggle expansion if ingredient is used by > 1 recipes
                if (ingredient.recipeNames.size > 1) {
                    expanded = !expanded
                }
            }
            .animateContentSize()
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
                        .padding(start = 10.dp)
                        .alpha(textAlpha),
                    verticalArrangement = Arrangement.Top,
                ) {
                    // Recipe name/count - Conditional display
                    Text(
                        text = if (ingredient.recipeNames.size > 1) {
                            "Used in ${ingredient.recipeNames.size} recipes"
                        } else {
                            ingredient.recipeNames.firstOrNull() ?: "N/A"
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
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.5f))
                        .padding(start = 70.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // --- Accordion Section (list of recipe names) ---
            if (expanded && ingredient.recipeNames.size > 1) {
                Column(modifier = Modifier.padding(start = 70.dp)) {
                    ingredient.recipeNames.forEachIndexed { index, recipeName ->
                        if (index > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.White.copy(alpha = 0.5f))
                            )
                            Spacer(modifier = Modifier.height(2.dp))
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
            onBackClick = {},
            navController = rememberNavController(),
            onRemoveClick = {},
        )
    }
}