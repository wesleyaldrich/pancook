import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.menuAnchor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.theme.PancookTheme

// Ingredient data class
data class Ingredient(
    val id: Long = 0L,
    var quantity: String = "",
    var unit: String = "g",
    var name: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    onBackPressed: () -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }
    var servings by remember { mutableStateOf(1) }

    val ingredients = remember { mutableStateListOf(Ingredient(id = 0L)) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    BackHandler { onBackPressed() }

    val darkBlue = Color(0xFF051A39)
    val lightGrayBackground = Color(0xFFF0F0F0)
    val veryLightGreen = Color(0xFFF8FFED)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGrayBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Image Picker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { pickImageLauncher.launch("image/*") }
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.salad),
                            contentDescription = "Placeholder",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Add Recipe Image", color = Color.DarkGray)
                    }
                }
            }

            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        InputSection("Recipe Title", title, { title = it }, darkBlue)

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        InputSection("Description", description, { description = it }, darkBlue, multiLine = true)

        Spacer(modifier = Modifier.height(16.dp))

        // Timer
        TimerSection(hours, minutes, seconds, { hours = it }, { minutes = it }, { seconds = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Servings
        ServingSection(servings, { if (servings > 1) servings-- }, { servings++ }, darkBlue)

        Spacer(modifier = Modifier.height(16.dp))

        // Ingredients
        IngredientsSection(ingredients, darkBlue, veryLightGreen)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                println("Recipe Title: $title")
                println("Description: $description")
                println("Time: ${hours}h ${minutes}m ${seconds}s")
                println("Servings: $servings")
                ingredients.forEach { println("${it.quantity} ${it.unit} ${it.name}") }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save Recipe")
        }
    }
}

@Composable
fun InputSection(
    label: String,
    text: String,
    onValueChange: (String) -> Unit,
    darkBlue: Color,
    multiLine: Boolean = false
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = label, color = darkBlue, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = !multiLine,
            maxLines = if (multiLine) 5 else 1,
            colors = TextFieldDefaults.textFieldColors()
        )
    }
}

@Composable
fun TimerSection(
    hours: Int,
    minutes: Int,
    seconds: Int,
    onHoursChange: (Int) -> Unit,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimeInput("Hours", hours, onHoursChange)
        TimeInput("Minutes", minutes, onMinutesChange)
        TimeInput("Seconds", seconds, onSecondsChange)
    }
}

@Composable
fun TimeInput(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        TextField(
            value = value.toString(),
            onValueChange = { onValueChange(it.toIntOrNull() ?: 0) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(80.dp)
        )
    }
}

@Composable
fun ServingSection(
    servings: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    darkBlue: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Servings", color = darkBlue, fontSize = 16.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onDecrease, enabled = servings > 1) { Text("-") }
            Spacer(modifier = Modifier.width(8.dp))
            Text(servings.toString(), fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onIncrease) { Text("+") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsSection(
    ingredients: MutableList<Ingredient>,
    darkBlue: Color,
    backgroundColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        ingredients.forEachIndexed { index, ingredient ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity
                TextField(
                    value = ingredient.quantity,
                    onValueChange = { ingredients[index] = ingredient.copy(quantity = it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Qty") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Dropdown Unit
                var expanded by remember { mutableStateOf(false) }
                val units = listOf("g", "kg", "ml", "cup", "tbsp")
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = ingredient.unit,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .width(80.dp),
                        label = { Text("Unit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        units.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    ingredients[index] = ingredient.copy(unit = unit)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Name
                TextField(
                    value = ingredient.name,
                    onValueChange = { ingredients[index] = ingredient.copy(name = it) },
                    modifier = Modifier.weight(2f),
                    placeholder = { Text("Ingredient") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Delete
                IconButton(onClick = { if (ingredients.size > 1) ingredients.removeAt(index) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { ingredients.add(Ingredient(id = System.currentTimeMillis())) }) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Ingredient")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecipePreview() {
    PancookTheme {
        AddRecipeScreen {}
    }
}
