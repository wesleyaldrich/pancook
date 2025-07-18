package com.wesleyaldrich.pancook.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.lazy.LazyColumn
import com.wesleyaldrich.pancook.model.Comment // Import Comment
import com.wesleyaldrich.pancook.ui.screens.allRecipes // Updated import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCompletionScreen(recipeId: Int, navController: NavController) {
    // Retrieve the specific recipe from the shared data source
    val sampleRecipe = remember {
        allRecipes.find { it.id == recipeId }
    }

    // Handle case where recipe is not found
    if (sampleRecipe == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    var uploadedImageUri by remember { mutableStateOf<Uri?>(null) }
    var commentTitle by remember { mutableStateOf("") }
    var commentDetail by remember { mutableStateOf("") }
    var upvoted by remember { mutableStateOf(false) }
    var downvoted by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uploadedImageUri = uri
    }

    val isSendCommentEnabled = uploadedImageUri != null && commentTitle.isNotBlank() && commentDetail.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back to Detail Page",
                            tint = colorResource(R.color.primary).copy(alpha = 1f)
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
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { pickImageLauncher.launch("image/*") }
                            .border(1.dp, colorResource(R.color.primary).copy(alpha = 1f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uploadedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(uploadedImageUri),
                                contentDescription = "Uploaded Dish Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Outlined.Image,
                                    contentDescription = "Upload Image",
                                    modifier = Modifier.size(48.dp),
                                    tint = colorResource(R.color.primary).copy(alpha = 1f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Upload your completed dish image",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = colorResource(R.color.primary).copy(alpha = 1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                upvoted = !upvoted
                                downvoted = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (upvoted) colorResource(R.color.primary).copy(alpha = 1f) else MaterialTheme.colorScheme.surfaceVariant, // Changed colors for clarity
                                contentColor = if (upvoted) colorResource(R.color.accent_yellow) else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Icon(
                                Icons.Filled.ThumbUp,
                                contentDescription = "Upvote",
                                tint = if (upvoted) colorResource(R.color.accent_yellow).copy(alpha = 1f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Upvote",
                                color = if (upvoted) colorResource(R.color.accent_yellow).copy(alpha = 1f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                downvoted = !downvoted
                                upvoted = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (downvoted) colorResource(R.color.primary).copy(alpha = 1f) else MaterialTheme.colorScheme.surfaceVariant, // Changed colors for clarity
                                contentColor = if (downvoted) colorResource(R.color.accent_yellow) else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Icon(
                                Icons.Filled.ThumbDown,
                                contentDescription = "Downvote",
                                tint = if (downvoted) colorResource(R.color.accent_yellow).copy(alpha = 1f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Downvote",
                                color = if (downvoted) colorResource(R.color.accent_yellow).copy(alpha = 1f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Comment Title (${commentTitle.length}/25)",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(R.color.primary).copy(alpha = 1f),
                        modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start)
                    )
                    OutlinedTextField(
                        value = commentTitle,
                        onValueChange = {
                            if (it.length <= 25) {
                                commentTitle = it
                            }
                        },
                        placeholder = { Text("Type here") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.secondary),
                            unfocusedBorderColor = colorResource(R.color.primary).copy(alpha = 1f),
                            focusedLabelColor = colorResource(R.color.secondary),
                            unfocusedLabelColor = colorResource(R.color.primary).copy(alpha = 1f)
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Comment Detail (${commentDetail.length}/150)",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(R.color.primary).copy(alpha = 1f),
                        modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start)
                    )
                    OutlinedTextField(
                        value = commentDetail,
                        onValueChange = {
                            if (it.length <= 150) {
                                commentDetail = it
                            }
                        },
                        placeholder = { Text("Type here") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.secondary),
                            unfocusedBorderColor = colorResource(R.color.primary).copy(alpha = 1f),
                            focusedLabelColor = colorResource(R.color.secondary),
                            unfocusedLabelColor =colorResource(R.color.primary).copy(alpha = 1f)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

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
                    onClick = { navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }},
                    modifier = Modifier
                        .weight(0.5f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Back to Home", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        // Form a Comment object from the input and print it
                        val newComment = Comment(
                            userName = "Current User", // Replace with actual user name
                            commentText = commentDetail,
                            imageUrl = uploadedImageUri?.toString(), // Convert Uri to String
                            isUpvote = when {
                                upvoted -> true
                                downvoted -> false
                                else -> null
                            }
                        )
                        println("New Comment to be sent: $newComment")
                        // In a real app, you would send this 'newComment' object
                        // to a backend service or update a mutable list accessible
                        // by DetailRecipeScreen.

                        // After sending, navigate home
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    enabled = isSendCommentEnabled,
                    modifier = Modifier
                        .weight(0.5f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.primary).copy(alpha = 1f),
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Send Comment", color = colorResource(R.color.accent_yellow).copy(alpha = 1f), fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 800)
@Composable
fun RecipeCompletionScreenPreview() {
    PancookTheme {
        RecipeCompletionScreen(recipeId = 1, navController = rememberNavController())
    }
}
