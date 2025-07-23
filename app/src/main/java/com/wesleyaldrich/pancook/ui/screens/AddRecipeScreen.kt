package com.wesleyaldrich.pancook.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontWeight
import com.wesleyaldrich.pancook.ui.theme.poppins

// Data class for Ingredient (remains unchanged)
data class Ingredient(
    val id: Long = 0L,
    var quantity: String = "",
    var unit: String = "g",
    var name: String = ""
)

// Step Data Class (remains unchanged)
data class Step(
    val id: Long = 0L,
    var description: String = "",
    var imageUri: Uri? = null,
    var timerMinutes: String = "0",
    var timerSeconds: String = "0"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    onBackPressed: () -> Unit
) {
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hoursInput by remember { mutableStateOf("0") }
    var minutesInput by remember { mutableStateOf("0") }
    var secondsInput by remember { mutableStateOf("0") }
    var servingsInput by remember { mutableStateOf("1") }

    val ingredients = remember { mutableStateListOf(Ingredient(id = 0L)) }
    val steps = remember { mutableStateListOf(Step(id = 0L)) }


    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        imageUris = uris
    }

    BackHandler { onBackPressed() }

    val darkBlue = Color(0xFF051A39)
    val lightGrayBackground = Color(0xFFF0F0F0)
    val veryLightGreen = Color(0xFFF8FFED)

    val pagerState = rememberPagerState(pageCount = { imageUris.size.coerceAtLeast(1) })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGrayBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // IMAGE SECTION (Carousel)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .clickable { pickImagesLauncher.launch("image/*") }
        ) {
            if (imageUris.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUris[page]),
                        contentDescription = "Recipe Image ${page + 1}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Dots Indicator
                Row(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { index ->
                        val selected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(if (selected) 10.dp else 8.dp)
                                .background(
                                    if (selected) Color.White else Color.LightGray,
                                    CircleShape
                                )
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.salad),
                            contentDescription = "Placeholder",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Add Recipe Image",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(20.dp)
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(16.dp))
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recipe Title Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(darkBlue)
                .padding(16.dp)
        ) {
            Text(
                text = "Recipe Title",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Enter recipe title", fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins,
                ) },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = darkBlue,
                    unfocusedTextColor = darkBlue,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Description Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(darkBlue)
                .padding(16.dp)
        ) {
            Text(
                text = "Description",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)

            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Enter recipe description", fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins,
                ) },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 200.dp),
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = darkBlue,
                    unfocusedTextColor = darkBlue,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Set Timer Section (Cooking Duration)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(darkBlue)
                .padding(16.dp)
        ) {
            Text(
                text = "Cooking Duration", // Changed label for clarity
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeInputUnit(
                    label = "Hours",
                    value = hoursInput,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                            val intValue = newValue.toIntOrNull() ?: 0
                            if (intValue in 0..99) {
                                hoursInput = newValue
                            } else if (newValue.isEmpty()) {
                                hoursInput = newValue
                            }
                        }
                    },
                    darkBlue = darkBlue,
                    placeholder = "00"
                )
                TimeInputUnit(
                    label = "Minutes",
                    value = minutesInput,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                            val intValue = newValue.toIntOrNull() ?: 0
                            if (intValue in 0..59) {
                                minutesInput = newValue
                            } else if (newValue.isEmpty()) {
                                minutesInput = newValue
                            }
                        }
                    },
                    darkBlue = darkBlue,
                    placeholder = "00"
                )
                TimeInputUnit(
                    label = "Seconds",
                    value = secondsInput,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                            val intValue = newValue.toIntOrNull() ?: 0
                            if (intValue in 0..59) {
                                secondsInput = newValue
                            } else if (newValue.isEmpty()) {
                                secondsInput = newValue
                            }
                        }
                    },
                    darkBlue = darkBlue,
                    placeholder = "00"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Serving Size Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(darkBlue)
                .padding(16.dp)
        ) {
            Text(
                text = "Serving Size",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                OutlinedTextField(
                    value = servingsInput,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                            val intValue = newValue.toIntOrNull() ?: 0
                            if (intValue >= 0 || newValue.isEmpty()) {
                                servingsInput = newValue
                            }
                        }
                    },
                    modifier = Modifier
                        .width(90.dp)
                        .height(56.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppins,
                        color = darkBlue
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(
                            text = "1",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppins,
                            color = Color.Gray.copy(alpha = 0.6f)
                        )
                    },
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = darkBlue,
                        unfocusedTextColor = darkBlue,
                        focusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "People",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppins,
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ingredients Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(darkBlue)
                .padding(16.dp)
        ) {
            Text(
                text = "Ingredients",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(veryLightGreen)
                    .padding(16.dp)
            ) {
                ingredients.forEachIndexed { index, ingredient ->
                    IngredientInputRow(
                        ingredient = ingredient,
                        onQuantityChange = { ingredients[index] = ingredient.copy(quantity = it) },
                        onUnitChange = { ingredients[index] = ingredient.copy(unit = it) },
                        onNameChange = { ingredients[index] = ingredient.copy(name = it) },
                        onRemove = {
                            if (ingredients.size > 1) {
                                ingredients.removeAt(index)
                            }
                        },
                        showRemoveButton = ingredients.size > 1,
                        darkBlue = darkBlue,
                    )
                    if (index < ingredients.size - 1) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                OutlinedButton(
                    onClick = {
                        ingredients.add(Ingredient(id = System.currentTimeMillis()))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = 24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = darkBlue,
                        contentColor = veryLightGreen
                    ),
                    border = BorderStroke(1.dp, veryLightGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Ingredient", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Ingredient", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Steps Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(darkBlue)
                .padding(16.dp)
        ) {
            Text(
                text = "Steps",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(veryLightGreen)
                    .padding(16.dp)
            ) {
                steps.forEachIndexed { index, step ->
                    StepInputRow(
                        stepNumber = index + 1,
                        step = step,
                        onDescriptionChange = { steps[index] = step.copy(description = it) },
                        onImageUriChange = { uri -> steps[index] = step.copy(imageUri = uri) },
                        onTimerMinutesChange = { newValue -> steps[index] = step.copy(timerMinutes = newValue) },
                        onTimerSecondsChange = { newValue -> steps[index] = step.copy(timerSeconds = newValue) },
                        onRemove = {
                            if (steps.size > 1) {
                                steps.removeAt(index)
                            }
                        },
                        showRemoveButton = steps.size > 1,
                        darkBlue = darkBlue
                    )
                    if (index < steps.size - 1) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                OutlinedButton(
                    onClick = {
                        steps.add(Step(id = System.currentTimeMillis()))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = 24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = darkBlue,
                        contentColor = veryLightGreen
                    ),
                    border = BorderStroke(1.dp, veryLightGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Step", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Step", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Two buttons at the bottom: Cancel and Create
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp), // Padding from the very bottom of the screen
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel Button
            OutlinedButton(
                onClick = onBackPressed, // Typically navigates back
                modifier = Modifier
                    .weight(1f) // Takes half the width
                    .height(56.dp)
                    .padding(end = 8.dp), // Space between buttons
                shape = RoundedCornerShape(16.dp), // Consistent rounded corners
                border = BorderStroke(1.dp, Color.Red), // Red border for Cancel
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Red, // Solid Red background
                    contentColor = Color.White // White text
                )
            ) {
                Text("Cancel", style = MaterialTheme.typography.labelLarge)
            }

            // Create Button (Primary Action)
            Button(
                onClick = {
                    val hours = hoursInput.toIntOrNull() ?: 0
                    val minutes = minutesInput.toIntOrNull() ?: 0
                    val seconds = secondsInput.toIntOrNull() ?: 0
                    val servingsInt = servingsInput.toIntOrNull() ?: 1

                    println("Recipe Title: $title")
                    println("Description: $description")
                    println("Cooking Time: ${hours}h ${minutes}m ${seconds}s")
                    println("Servings: ${servingsInt} people")
                    println("Ingredients:")
                    ingredients.forEach {
                        println("  - ${it.quantity} ${it.unit} ${it.name}")
                    }
                    println("Steps:")
                    steps.forEachIndexed { index, step ->
                        val stepMinutesInt = step.timerMinutes.toIntOrNull() ?: 0
                        val stepSecondsInt = step.timerSeconds.toIntOrNull() ?: 0
                        println("  Step ${index + 1}: ${step.description}, Image: ${step.imageUri}, Timer: ${stepMinutesInt}min ${stepSecondsInt}sec")
                    }
                    // Add actual navigation or data submission logic here
                },
                modifier = Modifier
                    .weight(1f) // Takes half the width
                    .height(56.dp)
                    .padding(start = 8.dp), // Space between buttons
                shape = RoundedCornerShape(16.dp), // Consistent rounded corners
                colors = ButtonDefaults.buttonColors(containerColor = darkBlue) // Dark blue background
            ) {
                Text("Create", style = MaterialTheme.typography.labelLarge, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientInputRow(
    ingredient: Ingredient,
    onQuantityChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onRemove: () -> Unit,
    showRemoveButton: Boolean,
    darkBlue: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val units = listOf("g", "kg", "ml", "L", "pcs", "cup", "tbsp", "tsp", "pinch", "dash", "to taste")

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            OutlinedTextField(
                value = ingredient.quantity,
                onValueChange = { onQuantityChange(it) },
                placeholder = { Text("Qty", fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins,
                ) },
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .width(80.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = darkBlue,
                    focusedTextColor = darkBlue,
                    unfocusedTextColor = darkBlue,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = ingredient.unit,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Unit", style = LocalTextStyle.current.copy(fontSize = 14.sp)) },
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.DarkGray,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = Color.Blue,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        units.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption, color = darkBlue) },
                                onClick = {
                                    onUnitChange(selectionOption)
                                    expanded = false
                                },
                                modifier = Modifier.height(48.dp)
                            )
                        }
                    }
                }
            }

            if (showRemoveButton) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red.copy(alpha = 0.8f))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Ingredient", tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = ingredient.name,
            onValueChange = { onNameChange(it) },
            placeholder = { Text("Ingredient name (e.g., 'Flour')", fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
            ) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = darkBlue,
                focusedTextColor = darkBlue,
                unfocusedTextColor = darkBlue,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

// UPDATED: StepInputRow Composable for Minutes and Seconds (keyboard input)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepInputRow(
    stepNumber: Int,
    step: Step,
    onDescriptionChange: (String) -> Unit,
    onImageUriChange: (Uri?) -> Unit,
    onTimerMinutesChange: (String) -> Unit,
    onTimerSecondsChange: (String) -> Unit,
    onRemove: () -> Unit,
    showRemoveButton: Boolean,
    darkBlue: Color
) {
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageUriChange(uri)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Top Row: Step Number, Timer (Minutes & Seconds), and Delete Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Distribute content
        ) {
            // Step Number (left-aligned, in its own pill)
            Text(
                text = "$stepNumber",
                fontSize = 20.sp, // Prominent font size
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                color = darkBlue,
                modifier = Modifier
                    .width(48.dp) // Fixed width for step number pill
                    .height(48.dp) // Fixed height
                    .clip(RoundedCornerShape(24.dp)) // Perfect pill shape
                    .background(Color.White)
                    .wrapContentSize(Alignment.Center) // Center content
            )

            // Timer Inputs (Minutes and Seconds, each in its own pill)
            Row(
                modifier = Modifier
                    .weight(1f) // Takes available space to push delete button to right
                    .height(48.dp), // Height for the row containing timer pills
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End // Align timer contents to the end (right)
            ) {
                // Minutes Input Pill
                OutlinedTextField(
                    value = step.timerMinutes, // Bind to step.timerMinutes
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                            val intValue = newValue.toIntOrNull() ?: 0
                            if (intValue in 0..59 || newValue.isEmpty()) { // Validate for minutes (0-59)
                                onTimerMinutesChange(newValue)
                            }
                        }
                    },
                    modifier = Modifier
                        .width(64.dp) // Increased width for better visibility of "0" and input
                        .height(40.dp), // Height within pill
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        fontFamily = poppins,
                        color = darkBlue
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(
                            text = "0", // Placeholder visible
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = poppins,
                            color = Color.Gray.copy(alpha = 0.6f)
                        )
                    },
                    shape = RoundedCornerShape(20.dp), // Pill shape for text field
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent, // No border
                        cursorColor = darkBlue,
                        focusedTextColor = darkBlue,
                        unfocusedTextColor = darkBlue,
                        focusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White // Solid white background
                    )
                )
                Text(
                    text = "min", // "min" label
                    fontSize = 14.sp,
                    fontFamily = poppins,
                    color = darkBlue,
                    modifier = Modifier.padding(start = 2.dp, end = 4.dp)
                )

                // Seconds Input Pill
                OutlinedTextField(
                    value = step.timerSeconds, // Bind to step.timerSeconds
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                            val intValue = newValue.toIntOrNull() ?: 0
                            if (intValue in 0..59 || newValue.isEmpty()) { // Validate for seconds (0-59)
                                onTimerSecondsChange(newValue)
                            }
                        }
                    },
                    modifier = Modifier
                        .width(64.dp) // Increased width for better visibility of "0" and input
                        .height(40.dp), // Height within pill
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        fontFamily = poppins,
                        color = darkBlue
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(
                            text = "0", // Placeholder visible
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = poppins,
                            color = Color.Gray.copy(alpha = 0.6f)
                        )
                    },
                    shape = RoundedCornerShape(20.dp), // Pill shape for text field
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent, // No border
                        cursorColor = darkBlue,
                        focusedTextColor = darkBlue,
                        unfocusedTextColor = darkBlue,
                        focusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White // Solid white background
                    )
                )
                Text(
                    text = "sec", // "sec" label
                    fontSize = 14.sp,
                    fontFamily = poppins,
                    color = darkBlue,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }


            // Conditional Remove Button (right-aligned, part of the main Row)
            if (showRemoveButton) {
                Spacer(modifier = Modifier.width(8.dp)) // Space from timer elements
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red.copy(alpha = 0.8f))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Step", tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp)) // Space between step header row and image/description row

        // Step Image and Description
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Step Image Placeholder/Preview
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .clickable { pickImageLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (step.imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(step.imageUri),
                        contentDescription = "Step Image $stepNumber",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.salad),
                            contentDescription = "Add image",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Add Photo",
                            fontSize = 10.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Step Description Input
            OutlinedTextField(
                value = step.description,
                onValueChange = { onDescriptionChange(it) },
                placeholder = { Text("Describe step (e.g., 'Chop vegetables')", fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins,
                ) },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 80.dp, max = 150.dp),
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = darkBlue,
                    focusedTextColor = darkBlue,
                    unfocusedTextColor = darkBlue,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputUnit(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    darkBlue: Color,
    placeholder: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = poppins,
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .width(64.dp)
                .height(64.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins,
                    color = Color.Gray.copy(alpha = 0.6f)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = darkBlue,
                focusedTextColor = darkBlue,
                unfocusedTextColor = darkBlue,
                focusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
                unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}


@Preview(showBackground = true,widthDp = 411,
    heightDp = 2400)
@Composable
fun AddRecipeScreenPreview() {
    PancookTheme {
        AddRecipeScreen(onBackPressed = {})
    }
}