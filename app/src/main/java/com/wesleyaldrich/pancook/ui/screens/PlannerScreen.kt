package com.wesleyaldrich.pancook.ui.screens

import androidx.annotation.ColorRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextDecoration
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.theme.inter
import com.wesleyaldrich.pancook.ui.theme.montserrat
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.nunito
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
// Removed unused import: import kotlin.random.Random

@Composable
fun CenteredTextBox(text: String, family: FontFamily, weight: FontWeight = FontWeight.Normal, size: TextUnit, align: TextAlign) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontFamily = family, fontWeight = weight, fontSize = size, textAlign = align)
    }
}

// Removed getRandomRecipeMakerForPlanner() as it's no longer needed

fun getDummyPlannerData(upcoming: Boolean): Map<String, Map<Recipe, Int>> {
    return if (upcoming) {
        mapOf(
            "08-07-2025" to mapOf(
                Recipe(
                    id = 101, // Consistent ID with MyRecipeScreen
                    title = "Hash Browns",
                    description = "Classic crispy potato breakfast.", // Consistent description
                    image = R.drawable.hash_brown,
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 2.0f, "large"), // Consistent qty and unit
                        Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 2.0f, "tbsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25f, "tsp")
                    ),
                    steps = listOf(
                        "Peel and grate potatoes. Rinse grated potatoes thoroughly under cold water until water runs clear.",
                        "Squeeze out as much excess water as possible from the grated potatoes using a clean kitchen towel or paper towels. This is crucial for crispiness!",
                        "Season the dried grated potatoes with salt and pepper.",
                        "Heat butter in a large non-stick skillet over medium heat until melted and slightly browned.",
                        "Press the grated potatoes into an even layer in the skillet. Cook for 5-7 minutes per side, pressing occasionally with a spatula, until golden brown and crispy.",
                        "Flip carefully and cook the other side until also golden and crispy.",
                        "Serve hot immediately, optionally with ketchup or a fried egg."
                    ),
                    servings = 2, // Consistent servings
                    duration = "20 min", // Consistent duration
                    upvoteCount = 500, // Fixed upvote count for Hash Browns
                    recipeMaker = "by Chef Emily" // Fixed recipeMaker for Hash Browns
                ) to 2, // Serving count for planner entry
                Recipe(
                    id = 102, // Consistent ID with MyRecipeScreen
                    title = "Fudgy Brownies",
                    description = "Rich, decadent, and perfectly fudgy.", // Consistent description
                    image = R.drawable.fudgy_brownies,
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Unsalted Butter", "Dairy", 100.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Granulated Sugar", "Sweeteners", 200.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Unsweetened Cocoa Powder", "Baking", 50.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Large Eggs", "Dairy", 2.0f, "pcs"),
                        Ingredient(R.drawable.ingredient_tomato, "All-Purpose Flour", "Baking", 60.0f, "g"),
                        Ingredient(R.drawable.ingredient_tomato, "Vanilla Extract", "Flavoring", 1.0f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.25f, "tsp")
                    ),
                    steps = listOf(
                        "Preheat oven to 175°C (350°F). Grease and flour an 8x8 inch baking pan.",
                        "In a medium saucepan, melt butter over low heat. Remove from heat and stir in sugar until combined.",
                        "Whisk in cocoa powder. Add eggs one at a time, mixing well after each addition. Stir in vanilla extract.",
                        "Gradually add flour and salt, mixing until just combined. Do not overmix.",
                        "Pour batter into the prepared baking pan and spread evenly.",
                        "Bake for 20-25 minutes, or until a toothpick inserted into the center comes out with moist crumbs (not wet batter).",
                        "Let cool completely in the pan on a wire rack before cutting into squares."
                    ),
                    servings = 2, // Consistent servings
                    duration = "1 hour", // Consistent duration
                    upvoteCount = 750, // Fixed upvote count for Fudgy Brownies
                    recipeMaker = "by Baker John" // Fixed recipeMaker for Fudgy Brownies
                ) to 3 // Serving count for planner entry
            )
        )
    } else {
        mapOf(
            "01-07-2025" to mapOf(
                Recipe(
                    id = 103, // Consistent ID with MyRecipeScreen
                    title = "French Toast",
                    description = "A sweet and savory breakfast classic.", // Consistent description
                    image = R.drawable.hash_brown, // Placeholder
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Bread", "Bakery", 4.0f, "slices"),
                        Ingredient(R.drawable.ingredient_tomato, "Large Eggs", "Dairy", 2.0f, "pcs"),
                        Ingredient(R.drawable.ingredient_tomato, "Milk", "Dairy", 100.0f, "ml"),
                        Ingredient(R.drawable.ingredient_tomato, "Granulated Sugar", "Sweeteners", 1.0f, "tbsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Vanilla Extract", "Flavoring", 0.5f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Cinnamon", "Spices", 0.25f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 1.0f, "tbsp")
                    ),
                    steps = listOf(
                        "In a shallow dish, whisk together eggs, milk, sugar, vanilla extract, and cinnamon until well combined.",
                        "Heat butter in a large non-stick skillet or griddle over medium heat.",
                        "Dip each slice of bread into the egg mixture, ensuring both sides are fully coated but not soggy.",
                        "Place bread slices on the hot skillet. Cook for 2-4 minutes per side, or until golden brown and cooked through.",
                        "Serve hot with your favorite toppings like syrup, fresh fruit, or powdered sugar."
                    ),
                    servings = 1, // Consistent servings
                    duration = "30 min", // Consistent duration
                    upvoteCount = 300, // Fixed upvote count for French Toast
                    recipeMaker = "by Chef Jane" // Fixed recipeMaker for French Toast
                ) to 1 // Serving count for planner entry
            )
        )
    }
}

enum class PlannerTab(val title: String) {
    UPCOMING("Upcoming"),
    PASSED("Passed")
}

@Composable
fun PlannerScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "Passed")

    Column {
        // Tab Container with rounded corners
        Box(
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = colorResource(R.color.primary))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Color.White else Color.Transparent
                            )
                            .clickable { selectedTabIndex = index }
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if(isSelected) Color.Black else Color.White,
                            fontFamily = nunito,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        // Content below tabs
        val data = if (selectedTabIndex == 0) {
            getDummyPlannerData(upcoming = true)
        } else {
            getDummyPlannerData(upcoming = false)
        }

        PlannerList(data = data)
    }
}

@Composable
fun PlannerList(modifier: Modifier = Modifier, data: Map<String, Map<Recipe, Int>>) {
    val checkedStates = remember {
        mutableStateMapOf<Int, Boolean>() // key = recipe.id, value = checked
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        data.forEach { (dateStr, recipeMap) ->
            item {
                DateSectionHeader(title = formatDateHeader(dateStr))
            }
            recipeMap.forEach { (recipe, serveCount) ->
                item {
                    PlanCard(
                        recipe = recipe,
                        serveCount = serveCount,
                        isChecked = checkedStates[recipe.id] ?: false,
                        onCheckedChange = { newValue ->
                            checkedStates[recipe.id] = newValue
                        }
                    )
                }
            }
        }
    }
}


fun formatDateHeader(dateString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())
    return try {
        val date = LocalDate.parse(dateString, inputFormatter)
        date.format(outputFormatter)
    } catch (e: Exception) {
        dateString // fallback
    }
}

fun Modifier.bottomBorder(strokeWidth: Dp = 1.dp, color: Color = Color.Black): Modifier = this.then(
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

@Composable
fun DateSectionHeader(title: String) {
    Box(modifier = Modifier
//        .wrapContentWidth()
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .bottomBorder(1.dp, Color.Black)
    ) {
        Text(
            text = title,
            fontFamily = nunito,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
//                .padding(horizontal = 14.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun CircularCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    size: Dp = 25.dp,
    checkedColor: Color = Color(0xFF3F51B5),
    uncheckedColor: Color = Color.LightGray,
    borderColor: Color = Color.Gray
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (checked) checkedColor else Color.Transparent)
            .border(width = 2.dp, color = borderColor, shape = CircleShape)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = Color.White,
                modifier = Modifier.size(size / 1.5f)
            )
        }
    }
}

@Composable
fun PlanCard(
    recipe: Recipe,
    serveCount: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val textAlpha by animateFloatAsState(
        targetValue = if (isChecked) 0.5f else 1f,
        label = "TextAlpha"
    )
    val textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None

    Box(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .background(
                color = colorResource(R.color.primary),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Container
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = if (isChecked) 0.3f else 1f))
            ) {
                Image(
                    painter = painterResource(id = recipe.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp)
                    .alpha(textAlpha),
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = recipe.title,
                    fontFamily = nunito,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.White,
                    textDecoration = textDecoration
                )
                Text(
                    text = recipe.recipeMaker, // Display recipe maker
                    fontFamily = nunito,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.White,
                    textDecoration = textDecoration
                )
                Text(
                    text = "$serveCount people",
                    fontFamily = nunito,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.White,
                    textDecoration = textDecoration
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            CircularCheckbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PlannerPagePreview () {
    PlannerScreen()
}