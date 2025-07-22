package com.wesleyaldrich.pancook.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wesleyaldrich.pancook.R

// Data Step
data class Step(
    var photoPainter: Painter? = null,
    var description: String = "",
    var timerDuration: Int = 0
)

@Composable
fun AddRecipeOverlayScreen(
    onDismiss: () -> Unit,
    onRecipeCreated: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    // Start the animation when the composable enters the composition
    LaunchedEffect(Unit) {
        visible = true
    }

    // Handle back button press
    BackHandler(enabled = visible) {
        visible = false
        // Delay dismissal until animation finishes
        // In a real app, you might want to use LaunchedEffect with a delay or a callback
        // from the animation to ensure it completes before dismissing.
        // For simplicity here, we'll dismiss immediately.
        onDismiss()
    }

    // Use AnimatedVisibility for slide-in/slide-out animation
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }), // Slides in from bottom
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }) // Slides out to bottom
    ) {
        // The Surface below will cover the entire screen including the navbar
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddRecipeScreen(
                onBackClick = {
                    visible = false
                    onDismiss()
                },
                onRecipeCreated = {
                    visible = false
                    onRecipeCreated()
                    onDismiss()
                }
            )
        }
    }
}

// ... (rest of your AddRecipeScreen, SectionHeader, IngredientInputRow, StepItem, Spinner, formatDuration)
// The AddRecipeScreen itself remains the same as your original code, as the animation is handled by the overlay.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    onBackClick: () -> Unit = {},
    onRecipeCreated: () -> Unit = {}
) {
    var recipeName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var selectedDurationUnit by remember { mutableStateOf("Menit") }
    var portion by remember { mutableStateOf("") }
    var selectedPortionUnit by remember { mutableStateOf("Orang") }
    val ingredients = remember { mutableStateListOf("") }
    val ingredientUnits = remember { mutableStateListOf("") }
    val steps = remember { mutableStateListOf(Step()) }

    LaunchedEffect(ingredients.size) {
        while (ingredientUnits.size < ingredients.size) {
            ingredientUnits.add("")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Buat Resep Baru") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = onBackClick, modifier = Modifier.weight(1f)) {
                    Text("Batal")
                }
                Spacer(Modifier.width(16.dp))
                Button(onClick = onRecipeCreated, modifier = Modifier.weight(1f)) {
                    Text("Buat Resep")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SectionHeader("Informasi Umum Resep")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.salad),
                        contentDescription = "Tambahkan Gambar",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                }
                Spacer(Modifier.height(16.dp))

                Text("Nama Resep", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = recipeName,
                    onValueChange = { recipeName = it },
                    label = { Text("Masukkan nama resep") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))
                Text("Deskripsi", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Jelaskan resep Anda") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(Modifier.height(16.dp))
                Text("Durasi Memasak", fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { if (it.all { c -> c.isDigit() }) duration = it },
                        label = { Text("Contoh: 30") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Spinner(listOf("Menit", "Jam"), selectedDurationUnit) {
                        selectedDurationUnit = it
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Porsi", fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = portion,
                        onValueChange = { if (it.all { c -> c.isDigit() }) portion = it },
                        label = { Text("Contoh: 2") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Spinner(listOf("Orang", "Porsi"), selectedPortionUnit) {
                        selectedPortionUnit = it
                    }
                }
            }

            item { SectionHeader("Bahan-bahan") }

            itemsIndexed(ingredients) { index, ingredient ->
                IngredientInputRow(
                    ingredient = ingredient,
                    onIngredientChange = { ingredients[index] = it },
                    unit = ingredientUnits[index],
                    onUnitChange = { ingredientUnits[index] = it },
                    onDelete = {
                        if (ingredients.size > 1) {
                            ingredients.removeAt(index)
                            ingredientUnits.removeAt(index)
                        }
                    }
                )
            }

            item {
                Button(
                    onClick = {
                        ingredients.add("")
                        ingredientUnits.add("")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Tambah Bahan")
                }
            }

            item { SectionHeader("Langkah-langkah Memasak") }

            itemsIndexed(steps) { index, step ->
                StepItem(
                    stepNumber = index + 1,
                    step = step,
                    onStepDescriptionChange = { steps[index] = step.copy(description = it) },
                    onStepPhotoClick = { },
                    onCustomTimerClick = { }
                )
                if (steps.size > 1) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { steps.removeAt(index) }) {
                            Text("Hapus Langkah")
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { steps.add(Step()) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Tambah Langkah")
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(24.dp))
        Divider()
        Spacer(Modifier.height(16.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun IngredientInputRow(
    ingredient: String,
    onIngredientChange: (String) -> Unit,
    unit: String,
    onUnitChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    val unitOptions = listOf("kg", "gr", "sendok teh", "sendok makan", "ml", "liter", "buah", "butir", "secukupnya", "")
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.weight(0.4f)) {
            OutlinedTextField(
                value = unit.ifEmpty { "Satuan" },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .clickable { expanded = true }
                    .fillMaxWidth(),
                label = { Text("Satuan") },
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                unitOptions.forEach {
                    DropdownMenuItem(onClick = {
                        onUnitChange(it)
                        expanded = false
                    }, text = { Text(it) })
                }
            }
        }
        Spacer(Modifier.width(8.dp))
        OutlinedTextField(
            value = ingredient,
            onValueChange = onIngredientChange,
            label = { Text("Bahan") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Hapus")
        }
    }
}

@Composable
fun StepItem(
    stepNumber: Int,
    step: Step,
    onStepDescriptionChange: (String) -> Unit,
    onStepPhotoClick: () -> Unit,
    onCustomTimerClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text("Langkah $stepNumber", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable { onStepPhotoClick() },
                contentAlignment = Alignment.Center
            ) {
                if (step.photoPainter != null) {
                    Image(painter = step.photoPainter!!, contentDescription = "Foto", modifier = Modifier.fillMaxSize())
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.salad),
                        contentDescription = "Tambah Foto",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = step.description,
                    onValueChange = onStepDescriptionChange,
                    label = { Text("Deskripsi langkah...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = formatDuration(step.timerDuration), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = onCustomTimerClick, colors = ButtonDefaults.outlinedButtonColors()) {
                        Text("Atur Timer")
                    }
                }
            }
        }
    }
}

@Composable
fun Spinner(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth(),
            label = { Text("Pilih") },
            trailingIcon = {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    text = { Text(item) }
                )
            }
        }
    }
}

fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}