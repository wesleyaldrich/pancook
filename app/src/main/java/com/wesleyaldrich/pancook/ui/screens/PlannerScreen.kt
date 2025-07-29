package com.wesleyaldrich.pancook.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.ui.screens.allRecipes
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

sealed class PlannerItem {
    data class Header(val title: String) : PlannerItem()
    data class Card(val date: String, val recipe: Recipe, val serveCount: Int, var isChecked: Boolean = false) : PlannerItem()
}

@Composable
fun CenteredTextBox(text: String, family: FontFamily, weight: FontWeight = FontWeight.Normal, size: TextUnit, align: TextAlign) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontFamily = family, fontWeight = weight, fontSize = size, textAlign = align)
    }
}

fun getPlannerData(upcoming: Boolean): MutableMap<String, MutableMap<Recipe, Int>> {
    val spaghettiBolognese = allRecipes.find { it.id == 1 }
    val freshGardenSalad = allRecipes.find { it.id == 2 }
    val grilledChicken = allRecipes.find { it.id == 3 }
    val deliciousSalad = allRecipes.find { it.id == 4 }
    val spicyNoodles = allRecipes.find { it.id == 5 }
    val chickenStirFry = allRecipes.find { it.id == 6 }
    val vegetableCurry = allRecipes.find { it.id == 7 }
    val creamyPasta = allRecipes.find { it.id == 8 }
    val grilledFish = allRecipes.find { it.id == 9 }
    val beefStew = allRecipes.find { it.id == 11 }
    val tomatoSoup = allRecipes.find { it.id == 12 }
    val hashBrowns = allRecipes.find { it.id == 101 }
    val fudgyBrownies = allRecipes.find { it.id == 102 }
    val frenchToast = allRecipes.find { it.id == 103 }

    return if (upcoming) {
        val upcomingMap = mutableMapOf<String, MutableMap<Recipe, Int>>()
        if (spaghettiBolognese != null && grilledChicken != null) {
            upcomingMap["28-07-2025"] = mutableMapOf(
                spaghettiBolognese to 2,
                grilledChicken to 2
            )
        }
        if (hashBrowns != null && fudgyBrownies != null) {
            upcomingMap["30-07-2025"] = mutableMapOf(
                hashBrowns to 2,
                fudgyBrownies to 3
            )
        }
        if (chickenStirFry != null && spicyNoodles != null) {
            upcomingMap["31-07-2025"] = mutableMapOf(
                chickenStirFry to 2,
                spicyNoodles to 1
            )
        }
        if (freshGardenSalad != null) {
            upcomingMap["01-08-2025"] = mutableMapOf(
                freshGardenSalad to 1
            )
        }
        if (grilledFish != null && vegetableCurry != null) {
            upcomingMap["02-08-2025"] = mutableMapOf(
                grilledFish to 2,
                vegetableCurry to 2
            )
        }
        if (beefStew != null && tomatoSoup != null) {
            upcomingMap["03-08-2025"] = mutableMapOf(
                beefStew to 3,
                tomatoSoup to 2
            )
        }
        upcomingMap
    } else {
        val passedMap = mutableMapOf<String, MutableMap<Recipe, Int>>()
        if (spaghettiBolognese != null) {
            passedMap["07-07-2025"] = mutableMapOf(
                spaghettiBolognese to 2
            )
        }
        if (grilledFish != null && creamyPasta != null) {
            passedMap["08-07-2025"] = mutableMapOf(
                grilledFish to 2,
                creamyPasta to 2
            )
        }
        if (spicyNoodles != null) {
            passedMap["09-07-2025"] = mutableMapOf(
                spicyNoodles to 1
            )
        }
        if (freshGardenSalad != null && grilledChicken != null) {
            passedMap["11-07-2025"] = mutableMapOf(
                freshGardenSalad to 1,
                grilledChicken to 2
            )
        }
        if (frenchToast != null) {
            passedMap["12-07-2025"] = mutableMapOf(
                frenchToast to 2
            )
        }
        if (vegetableCurry != null && hashBrowns != null && fudgyBrownies != null) {
            passedMap["13-07-2025"] = mutableMapOf(
                vegetableCurry to 2,
                hashBrowns to 2,
                fudgyBrownies to 3
            )
        }
        passedMap
    }
}

fun createPlannerItems(data: Map<String, Map<Recipe, Int>>): List<PlannerItem> {
    val items = mutableListOf<PlannerItem>()
    data.forEach { (dateStr, recipeMap) ->
        items.add(PlannerItem.Header(formatDateHeader(dateStr)))
        recipeMap.forEach { (recipe, serveCount) ->
            items.add(PlannerItem.Card(dateStr, recipe, serveCount))
        }
    }
    return items
}

enum class PlannerTab(val title: String) {
    UPCOMING("Upcoming"),
    PASSED("Passed")
}

@Composable
fun PlannerScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "Passed")

    var upcomingItems by remember { mutableStateOf(createPlannerItems(getPlannerData(true))) }
    var passedItems by remember { mutableStateOf(createPlannerItems(getPlannerData(false))) }

    val currentItems = if (selectedTabIndex == 0) upcomingItems else passedItems
    val setCurrentItems = if (selectedTabIndex == 0) {
        { newItems: List<PlannerItem> -> upcomingItems = newItems }
    } else {
        { newItems: List<PlannerItem> -> passedItems = newItems }
    }

    Column {
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

        PlannerList(
            data = currentItems,
            onRemoveRecipe = { itemToRemove: PlannerItem.Card ->
                val updatedList = currentItems.toMutableList()
                updatedList.remove(itemToRemove)

                val cardsForDate = updatedList.filterIsInstance<PlannerItem.Card>().count { it.date == itemToRemove.date }
                if (cardsForDate == 0) {
                    val headerToRemove = updatedList.firstOrNull { it is PlannerItem.Header && it.title == formatDateHeader(itemToRemove.date) }
                    if (headerToRemove != null) {
                        updatedList.remove(headerToRemove)
                    }
                }

                setCurrentItems(updatedList)
            },
            onCardCheckedChanged = { changedItem: PlannerItem.Card, isChecked: Boolean ->
                val currentList = currentItems.toMutableList()

                val updatedListWithCheck = currentList.map { item ->
                    if (item is PlannerItem.Card && item.recipe.id == changedItem.recipe.id && item.date == changedItem.date) {
                        item.copy(isChecked = isChecked)
                    } else {
                        item
                    }
                }

                val dateCards = updatedListWithCheck.filterIsInstance<PlannerItem.Card>().filter { it.date == changedItem.date }
                val uncheckedCards = dateCards.filter { !it.isChecked }
                val checkedCards = dateCards.filter { it.isChecked }

                val sortedDateCards = uncheckedCards + checkedCards

                val newList = updatedListWithCheck.toMutableList()
                val startIndex = newList.indexOfFirst { it is PlannerItem.Card && it.date == changedItem.date }
                val endIndex = newList.indexOfLast { it is PlannerItem.Card && it.date == changedItem.date }

                if (startIndex != -1 && endIndex != -1) {
                    newList.subList(startIndex, endIndex + 1).clear()
                    newList.addAll(startIndex, sortedDateCards)
                }

                setCurrentItems(newList)
            },
            navController = navController
        )
    }
}

@Composable
fun PlannerList(
    modifier: Modifier = Modifier,
    data: List<PlannerItem>,
    onRemoveRecipe: (PlannerItem.Card) -> Unit,
    onCardCheckedChanged: (PlannerItem.Card, Boolean) -> Unit,
    navController: NavController
) {
    val checkedStates = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        items(
            items = data,
            key = { item ->
                when (item) {
                    is PlannerItem.Header -> "header-${item.title}"
                    is PlannerItem.Card -> "card-${item.date}-${item.recipe.id}"
                }
            }
        ) { item ->
            when (item) {
                is PlannerItem.Header -> {
                    DateSectionHeader(title = item.title)
                }
                is PlannerItem.Card -> {
                    PlanCard(
                        recipe = item.recipe,
                        serveCount = item.serveCount,
                        isChecked = item.isChecked,
                        onCheckedChange = { newValue ->
                            onCardCheckedChanged(item, newValue)
                        },
                        navController = navController,
                        onDeleteClick = {
                            onRemoveRecipe(item)
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
        dateString
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
    onCheckedChange: (Boolean) -> Unit,
    navController: NavController,
    onDeleteClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (isChecked) 0.5f else 1f,
        label = "PlanCardAlpha"
    )
    val textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None

    Box(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .alpha(alpha)
            .background(
                color = colorResource(R.color.primary),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                navController.navigate(Screen.DetailRecipe.createRoute(recipe.id))
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Gray.copy(alpha = if (isChecked) 0.3f else 1f))
            ) {
                Image(
                    painter = painterResource(id = recipe.images.first()),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp)
                    .alpha(alpha),
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

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .clickable { onDeleteClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PlannerPagePreview () {
    PlannerScreen(navController = rememberNavController())
}