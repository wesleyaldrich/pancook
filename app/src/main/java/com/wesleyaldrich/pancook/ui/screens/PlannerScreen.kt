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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.wesleyaldrich.pancook.model.Instruction // Import Instruction
import com.wesleyaldrich.pancook.model.NutritionFact // Import NutritionFact
import com.wesleyaldrich.pancook.model.Comment // Import Comment

@Composable
fun CenteredTextBox(text: String, family: FontFamily, weight: FontWeight = FontWeight.Normal, size: TextUnit, align: TextAlign) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontFamily = family, fontWeight = weight, fontSize = size, textAlign = align)
    }
}

fun getDummyPlannerData(upcoming: Boolean): Map<String, Map<Recipe, Int>> {
    return if (upcoming) {
        mapOf(
            "08-07-2025" to mapOf(
                Recipe(
                    id = 101,
                    title = "Hash Browns",
                    description = "Classic crispy potato breakfast.",
                    image = R.drawable.hash_brown,
                    ingredients = listOf(
                        Ingredient(R.drawable.ingredient_tomato, "Potatoes", "Vegetables", 2.0f, "large"),
                        Ingredient(R.drawable.ingredient_tomato, "Butter", "Dairy", 2.0f, "tbsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Salt", "Seasoning", 0.5f, "tsp"),
                        Ingredient(R.drawable.ingredient_tomato, "Black Pepper", "Seasoning", 0.25f, "tsp")
                    ),
                    steps = listOf(
                        Instruction(1, "Peel and grate potatoes. Rinse grated potatoes thoroughly under cold water until water runs clear."),
                        Instruction(2, "Squeeze out as much excess water as possible from the grated potatoes using a clean kitchen towel or paper towels. This is crucial for crispiness!"),
                        Instruction(3, "Season the dried grated potatoes with salt and pepper."),
                        Instruction(4, "Heat butter in a large non-stick skillet over medium heat until melted and slightly browned."),
                        Instruction(5, "Press the grated potatoes into an even layer in the skillet. Cook for 5-7 minutes per side, pressing occasionally with a spatula, until golden brown and crispy."),
                        Instruction(6, "Flip carefully and cook the other side until also golden and crispy."),
                        Instruction(7, "Serve hot immediately, optionally with ketchup or a fried egg.")
                    ),
                    servings = 2,
                    duration = "20 min",
                    upvoteCount = 500,
                    recipeMaker = "by Chef Emily",
                    nutritionFacts = listOf( // Added for consistency
                        NutritionFact("Calories", "300 kcal"),
                        NutritionFact("Protein", "5g"),
                        NutritionFact("Fat", "18g"),
                        NutritionFact("Carbs", "30g")
                    ),
                    comments = listOf( // Added for consistency
                        Comment("Breakfast King", "Crispy and delicious, a perfect breakfast side!")
                    )
                ) to 2,
                Recipe(
                    id = 102,
                    title = "Fudgy Brownies",
                    description = "Rich, decadent, and perfectly fudgy.",
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
                        Instruction(1, "Preheat oven to 175°C (350°F). Grease and flour an 8x8 inch baking pan."),
                        Instruction(2, "In a medium saucepan, melt butter over low heat. Remove from heat and stir in sugar until combined."),
                        Instruction(3, "Whisk in cocoa powder. Add eggs one at a time, mixing well after each addition. Stir in vanilla extract."),
                        Instruction(4, "Gradually add flour and salt, mixing until just combined. Do not overmix."),
                        Instruction(5, "Pour batter into the prepared baking pan and spread evenly."),
                        Instruction(6, "Bake for 20-25 minutes, or until a toothpick inserted into the center comes out with moist crumbs (not wet batter)."),
                        Instruction(7, "Let cool completely in the pan on a wire rack before cutting into squares.")
                    ),
                    servings = 2,
                    duration = "1 hour",
                    upvoteCount = 750,
                    recipeMaker = "by Baker John",
                    nutritionFacts = listOf( // Added for consistency
                        NutritionFact("Calories", "450 kcal"),
                        NutritionFact("Protein", "5g"),
                        NutritionFact("Fat", "25g"),
                        NutritionFact("Carbs", "60g")
                    ),
                    comments = listOf( // Added for consistency
                        Comment("Sweet Tooth", "The fudgiest brownies ever! A must-try.")
                    )
                ) to 3
            )
        )
    } else {
        mapOf(
            "01-07-2025" to mapOf(
                Recipe(
                    id = 103,
                    title = "French Toast",
                    description = "A sweet and savory breakfast classic.",
                    image = R.drawable.hash_brown,
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
                        Instruction(1, "In a shallow dish, whisk together eggs, milk, sugar, vanilla extract, and cinnamon until well combined."),
                        Instruction(2, "Heat butter in a large non-stick skillet or griddle over medium heat."),
                        Instruction(3, "Dip each slice of bread into the egg mixture, ensuring both sides are fully coated but not soggy."),
                        Instruction(4, "Place bread slices on the hot skillet. Cook for 2-4 minutes per side, or until golden brown and cooked through."),
                        Instruction(5, "Serve hot with your favorite toppings like syrup, fresh fruit, or powdered sugar.")
                    ),
                    servings = 1,
                    duration = "30 min",
                    upvoteCount = 300,
                    recipeMaker = "by Chef Jane",
                    nutritionFacts = listOf( // Added for consistency
                        NutritionFact("Calories", "350 kcal"),
                        NutritionFact("Protein", "10g"),
                        NutritionFact("Fat", "15g"),
                        NutritionFact("Carbs", "40g")
                    ),
                    comments = listOf( // Added for consistency
                        Comment("Brunch Fan", "Best French Toast ever, simple and delicious!")
                    )
                ) to 1
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
                    text = recipe.recipeMaker,
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