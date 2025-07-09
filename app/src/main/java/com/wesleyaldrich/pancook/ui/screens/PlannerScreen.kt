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
import androidx.compose.ui.text.style.TextDecoration
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Ingredient
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.theme.inter
import com.wesleyaldrich.pancook.ui.theme.montserrat
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
                    id = 1,
                    title = "Hash Browns",
                    description = "Crispy and golden",
                    imageUrl = "",
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    steps = listOf("Step 1", "Step 2"),
                    servings = 2
                ) to 2,
                Recipe(
                    id = 2,
                    title = "Hash Brownies",
                    description = "Crispy and golden",
                    imageUrl = "",
                    ingredients = listOf(
                        Ingredient("1", "Potatoes", 3, "pcs")
                    ),
                    steps = listOf("Step 1", "Step 2"),
                    servings = 2
                ) to 3
            )
        )
    } else {
        mapOf(
            "01-07-2025" to mapOf(
                Recipe(
                    id = 3,
                    title = "French Toast",
                    description = "Delicious toast",
                    imageUrl = "",
                    ingredients = listOf(
                        Ingredient("2", "Bread", 4, "slices")
                    ),
                    steps = listOf("Step 1", "Step 2"),
                    servings = 1
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
            .padding(10.dp)
    ) {
        data.forEach { (dateStr, recipeMap) ->
            item {
                DateSectionHeader(title = formatDateHeader(dateStr))
            }
            recipeMap.forEach { (recipe, serveCount) ->
                item {
                    PlanCard(
                        title = recipe.title,
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


@Composable
fun DateSectionHeader(title: String) {
    Box(modifier = Modifier
        .wrapContentWidth()
        .padding(vertical = 8.dp)
        .background(
            color = colorResource(id = R.color.primary),
            shape = RoundedCornerShape(10.dp)
        )
    ) {
        Text(
            text = title,
            fontFamily = nunito,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 5.dp)
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
    title: String,
    serveCount: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val textAlpha by animateFloatAsState(
        targetValue = if (isChecked) 0.5f else 1f,
        label = "TextAlpha"
    )
    val textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None

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
                painter = painterResource(id = R.drawable.hash_brown),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .alpha(textAlpha)
        ) {
            Text(
                text = title,
                fontFamily = nunito,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                textDecoration = textDecoration
            )
            Text(
                text = "$serveCount people",
                fontFamily = nunito,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
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

@Preview(showSystemUi = true)
@Composable
private fun PlannerPagePreview () {
    PlannerScreen()
}