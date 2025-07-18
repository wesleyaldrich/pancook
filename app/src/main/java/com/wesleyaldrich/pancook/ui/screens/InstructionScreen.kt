package com.wesleyaldrich.pancook.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // Import LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import kotlinx.coroutines.delay
import kotlin.math.max
import com.wesleyaldrich.pancook.ui.navigation.Screen // Import the Screen object
import androidx.compose.ui.graphics.Brush // Import Brush for gradient background
import com.wesleyaldrich.pancook.model.Instruction // Import Instruction
import com.wesleyaldrich.pancook.ui.screens.allRecipes // Updated import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionScreen(recipeId: Int, navController: NavController) {
    // Retrieve the specific recipe from the shared data source using the ID
    val currentRecipe = remember(recipeId) { // Use remember(recipeId) to re-lookup if ID changes
        allRecipes.find { it.id == recipeId }
    }

    // Handle case where recipe is not found (e.g., navigate back or show error)
    if (currentRecipe == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack() // Go back if recipe not found
        }
        return // Don't render anything if recipe is null
    }

    // Use properties from currentRecipe
    val instructions = currentRecipe.steps
    val currentRecipeMainImage = currentRecipe.image // Using the main image from the actual recipe

    var currentStepIndex by rememberSaveable { mutableStateOf(0) }
    var timerValueSeconds by rememberSaveable { mutableStateOf(0) }
    var isTimerRunning by rememberSaveable { mutableStateOf(false) }
    var isTimerPaused by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current // Get the current context

    // Reset timer state when currentStepIndex changes
    LaunchedEffect(currentStepIndex, instructions) { // Depend on instructions too if list changes dynamically
        val currentInstruction = instructions.getOrNull(currentStepIndex)
        if (currentInstruction != null && currentInstruction.timerSeconds != null && currentInstruction.timerSeconds > 0) {
            timerValueSeconds = currentInstruction.timerSeconds
            isTimerRunning = false
            isTimerPaused = false
        } else {
            timerValueSeconds = 0
            isTimerRunning = false
            isTimerPaused = false
        }
    }

    // Timer logic
    LaunchedEffect(isTimerRunning, timerValueSeconds) { // Depend on timerValueSeconds too to stop immediately
        while (isTimerRunning && timerValueSeconds > 0) {
            delay(1000L)
            timerValueSeconds--
        }
        if (timerValueSeconds == 0 && isTimerRunning) {
            isTimerRunning = false // Stop timer when it reaches 0
            isTimerPaused = false // Reset pause state
            // Open YouTube link when timer ends
            val youtubeUrl = "https://youtu.be/QB7ACr7pUuE?si=qk9D1JEbb4crjacB"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            context.startActivity(intent)
        }
    }

    // Function to format time for display (MM:SS)
    fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // This correctly pops back to DetailRecipeScreen
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back to Detail Page",
                            tint = MaterialTheme.colorScheme.onSurface
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
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp), // Add padding for sticky buttons
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // 60. Instruction image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = currentRecipeMainImage), // Use the actual recipe's main image
                            contentDescription = "Instruction Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 61. Instruction step title
                    Text(
                        text = "Step ${currentStepIndex + 1}",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                        modifier = Modifier.fillMaxWidth(),
                        color = colorResource(R.color.primary).copy(alpha = 1f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 62. Instruction detail
                    Text(
                        text = instructions.getOrNull(currentStepIndex)?.description ?: "No instruction found for this step.", // Access description from the passed instructions
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Conditional Timer section
                    val currentInstruction = instructions.getOrNull(currentStepIndex)
                    if (currentInstruction != null && currentInstruction.timerSeconds != null && currentInstruction.timerSeconds > 0) { // Check if the current instruction has a timer
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Timer value and adjustment buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    // Decrease buttons
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Button(
                                            onClick = { timerValueSeconds = max(0, timerValueSeconds - 60) },
                                            modifier = Modifier.height(32.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Filled.Remove, contentDescription = "Decrease Timer by 1 minute", modifier = Modifier.size(14.dp), tint = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                            Text(text = " 1m", fontSize = 12.sp, color = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Button(
                                            onClick = { timerValueSeconds = max(0, timerValueSeconds - 30) },
                                            modifier = Modifier.height(32.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Filled.Remove, contentDescription = "Decrease Timer by 30 seconds", modifier = Modifier.size(14.dp), tint = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                            Text(text = " 30s", fontSize = 12.sp, color = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Button(
                                            onClick = { timerValueSeconds = max(0, timerValueSeconds - 15) },
                                            modifier = Modifier.height(32.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Filled.Remove, contentDescription = "Decrease Timer by 15 seconds", modifier = Modifier.size(14.dp), tint = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                            Text(text = " 15s", fontSize = 12.sp, color = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                        }
                                    }

                                    // 63. Timer value - Now boxed in accent_blue
                                    Box(
                                        modifier = Modifier
                                            .wrapContentSize(align = Alignment.Center) // Adjusts box size to content
                                            .clip(RoundedCornerShape(8.dp)) // Rounded corners for the box
                                            .background(colorResource(R.color.accent_blue)) // Accent blue background
                                            .padding(horizontal = 16.dp, vertical = 8.dp), // Padding inside the box
                                        contentAlignment = Alignment.Center // Center content within the box
                                    ) {
                                        Text(
                                            text = formatTime(timerValueSeconds),
                                            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold, fontSize = 50.sp),
                                            color = colorResource(R.color.primary).copy(alpha = 1f) // Text color white for contrast
                                        )
                                    }


                                    // Increase buttons
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Button(
                                            onClick = { timerValueSeconds += 60 },
                                            modifier = Modifier.height(32.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Filled.Add, contentDescription = "Increase Timer by 1 minute", modifier = Modifier.size(14.dp), tint = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                            Text(text = " 1m", fontSize = 12.sp, color = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Button(
                                            onClick = { timerValueSeconds += 30 },
                                            modifier = Modifier.height(32.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Filled.Add, contentDescription = "Increase Timer by 30 seconds", modifier = Modifier.size(14.dp), tint = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                            Text(text = " 30s", fontSize = 12.sp, color = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Button(
                                            onClick = { timerValueSeconds += 15 },
                                            modifier = Modifier.height(32.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Filled.Add, contentDescription = "Increase Timer by 15 seconds", modifier = Modifier.size(14.dp), tint = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                            Text(text = " 15s", fontSize = 12.sp, color = colorResource(R.color.accent_yellow).copy(alpha = 1f))
                                        }
                                    }

                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // 65. Timer start/stop/reset button
                                Button(
                                    onClick = {
                                        if (isTimerRunning) {
                                            isTimerRunning = false
                                            isTimerPaused = true
                                        } else if (isTimerPaused) {
                                            if (timerValueSeconds > 0) {
                                                isTimerRunning = true
                                                isTimerPaused = false
                                            } else {
                                                timerValueSeconds = currentInstruction.timerSeconds ?: 0 // Reset to initial value or 0
                                                isTimerPaused = false // Make sure it's not paused after reset
                                            }
                                        } else {
                                            if (timerValueSeconds > 0) {
                                                isTimerRunning = true
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                                ) {
                                    if (isTimerRunning) {
                                        Icon(Icons.Filled.Pause, contentDescription = "Pause Timer", modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Pause", fontSize = 18.sp)
                                    } else if (isTimerPaused && timerValueSeconds > 0) {
                                        Icon(Icons.Filled.PlayArrow, contentDescription = "Continue Timer", modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Continue", fontSize = 18.sp)
                                    } else {
                                        Icon(Icons.Filled.PlayArrow, contentDescription = "Start Timer", modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = if (timerValueSeconds == 0 && isTimerPaused) "Reset" else "Start", fontSize = 18.sp)
                                    }
                                }
                                if (!isTimerRunning && (isTimerPaused || timerValueSeconds > 0)) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            timerValueSeconds = currentInstruction.timerSeconds ?: 0 // Reset to initial value or 0
                                            isTimerRunning = false
                                            isTimerPaused = false
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                                    ) {
                                        Icon(Icons.Filled.Refresh, contentDescription = "Reset Timer", modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Reset", fontSize = 18.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 66. Navigation buttons (sticky at the bottom)
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
                    onClick = {
                        // Only update internal state, do not navigate
                        if (currentStepIndex > 0) {
                            currentStepIndex--
                        }
                    },
                    enabled = currentStepIndex > 0,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp), // Set height to match DetailRecipeScreen
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Step", tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Previous", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (currentStepIndex < instructions.size - 1) {
                            currentStepIndex++
                        } else {
                            // Navigate to RecipeCompletionScreen when on the last step and "Next" is clicked
                            navController.navigate(Screen.RecipeCompletion.createRoute(recipeId))
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp), // Set height to match DetailRecipeScreen
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                ) {
                    Text(text = if (currentStepIndex < instructions.size - 1) "Next" else "Finish", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Step", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 411, heightDp = 800)
@Composable
fun InstructionScreenPreview() {
    PancookTheme {
        InstructionScreen(recipeId = 1, navController = rememberNavController())
    }
}
