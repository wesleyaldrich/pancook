package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape // Import for RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Import for sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import kotlin.math.floor
import kotlin.math.log
import androidx.compose.ui.graphics.Brush // Import for Brush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRecipeScreen(recipeId: Int, navController: NavController) {
    var servingCount by remember { mutableStateOf(1) } // Default value set to 1

    // Placeholder values for social counts
    val upvoteCount = 12500
    val bookmarkCount = 3200
    val shareCount = 567

    // Define formatCount inside the composable, or outside if it doesn't need composable scope
    fun formatCount(count: Int): String {
        if (count < 1000) return count.toString()
        val exp = floor(log(count.toDouble(), 1000.0)).toInt()
        val units = arrayOf("", "k", "M", "B", "T")
        val formattedValue = String.format("%.1f", count / Math.pow(1000.0, exp.toDouble()))
        return formattedValue.replace(".0", "") + units[exp]
    }

    // Placeholder list of base ingredients (for 1 serving)
    val baseIngredients = remember {
        listOf(
            "1.0 unit Fresh Lettuce",
            "0.5 unit Cherry Tomatoes",
            "0.25 unit Cucumber",
            "0.1 unit Red Onion",
            "50.0g Grilled Chicken",
            "1.0 tbsp Olive Oil",
            "1.0 dash Salt",
            "1.0 dash Black Pepper",
            "2.0 pcs Hard-boiled Eggs",
            "10.0g Feta Cheese"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = "Back",
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
                // .padding(bottom = 80.dp) // Removed this padding
            ) {
                item {
                    // 34. Carousel Recipe image placeholder
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp) // Apply horizontal padding here to match card width
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp)) // Apply rounded corners here
                        ) {
                            LazyRow(
                                modifier = Modifier.fillMaxSize(), // LazyRow fills the clipped Box
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Although LazyRow is used, currently only one image is displayed.
                                // If more images are added to the list, they will also fill the width.
                                items(listOf(R.drawable.salad)) { imgResId ->
                                    Image(
                                        painter = painterResource(id = imgResId), // Use imgResId from the items lambda
                                        contentDescription = "Recipe Image",
                                        modifier = Modifier
                                            .fillParentMaxWidth() // This ensures each item in LazyRow takes full width of the LazyRow's parent
                                            .fillMaxHeight(), // Ensures the image fills the height of the Box
                                        contentScale = ContentScale.Crop // Prevents stretching by cropping the image
                                    )
                                }
                            }
                        }
                        // Carousel dots moved below the carousel image box
                        Row(
                            modifier = Modifier
                                .fillMaxWidth() // Take full width to center the dots
                                .padding(top = 8.dp), // Padding between image and dots
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(3) { // Assuming 3 total pages for the carousel based on image
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = if (it == 0) 0.8f else 0.4f)) // Highlight first dot
                                )
                                if (it < 2) Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                    }


                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                        // Main header row containing title/creator/duration on left and social buttons on right
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top, // Keeping Alignment.Top
                            horizontalArrangement = Arrangement.SpaceBetween // Pushes the left part and social buttons apart
                        ) {
                            // Left section: Column for Title, and then Row for Creator + Duration
                            Column(modifier = Modifier.weight(1f)) { // Give it weight to push social buttons right
                                Text(
                                    text = "Delicious Salad", // 35. Recipe title - Placeholder
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.fillMaxWidth() // Title should take full width of its column
                                )
                                Spacer(modifier = Modifier.height(4.dp)) // Space between title and the next row

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Creator Name (40) group
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_profile_placeholder),
                                            contentDescription = "Creator Profile",
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color.LightGray),
                                            contentScale = ContentScale.Crop,
                                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "by Firstname", // 40. Creator name - Placeholder, now just "Firstname"
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium) // Made smaller
                                        )
                                    }

                                    // Duration (36, 37) group
                                    Spacer(modifier = Modifier.width(16.dp)) // Minimal space between name and duration
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_clock), // 36. Duration icon
                                            contentDescription = "Duration",
                                            modifier = Modifier.size(20.dp),
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(text = "15 min", style = MaterialTheme.typography.bodySmall) // Made smaller
                                    }
                                }
                            }

                            // 38. Social icons (Upvote, Saved, Share) & 39. Social Icon labels
                            Row(
                                modifier = Modifier.padding(top = 8.dp), // Added top padding to move down
                                verticalAlignment = Alignment.Top // Still align within its own row to top
                            ) {
                                // Upvote
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { /* Handle Upvote */ },
                                        modifier = Modifier.size(24.dp) // Maintain icon size
                                    ) {
                                        Icon(Icons.Filled.ThumbUp, contentDescription = "Upvote", tint = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Spacer(modifier = Modifier.height(0.dp)) // Reduced space between icon and text
                                    Text(
                                        text = formatCount(upvoteCount),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.8) // Made text even smaller
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp)) // Smaller space between social icon groups
                                // Bookmark
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { /* Handle Saved */ },
                                        modifier = Modifier.size(24.dp) // Maintain icon size
                                    ) {
                                        Icon(Icons.Filled.Bookmark, contentDescription = "Saved", tint = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Spacer(modifier = Modifier.height(0.dp)) // Reduced space between icon and text
                                    Text(
                                        text = formatCount(bookmarkCount),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.8) // Made text even smaller
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp)) // Smaller space between social icon groups
                                // Share
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { /* Handle Share */ },
                                        modifier = Modifier.size(24.dp) // Maintain icon size
                                    ) {
                                        Icon(Icons.Filled.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Spacer(modifier = Modifier.height(0.dp)) // Reduced space between icon and text
                                    Text(
                                        text = formatCount(shareCount),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.8) // Made text even smaller
                                    )
                                }
                            }
                        }

                        // Remaining content below the main header row
                        Spacer(modifier = Modifier.height(16.dp)) // Space after the main header block, before description

                        Text(
                            text = "A fresh and healthy salad perfect for a light meal or side dish. This recipe is easy to make and packed with nutrients. Enjoy!", // 41. Recipe description text area - Placeholder
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }

                // Ingredients Section within a Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp), // Padding for the card itself
                        shape = RoundedCornerShape(12.dp), // Rounded corners for the card
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Card background color
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) { // Inner padding for card content
                            Row( // Combined row for Ingredients header and serving controls
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween // Distribute content
                            ) {
                                Text(
                                    text = "Ingredients", // 42. Ingredients section header
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.weight(1f) // Allow text to take space
                                )

                                // Serving controls group (43, 44, 45)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_serving), // 43. Serving count icon
                                        contentDescription = "Serving Count",
                                        modifier = Modifier.size(20.dp), // Smaller icon
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp)) // Smaller space after icon

                                    Button(
                                        onClick = { if (servingCount > 1) servingCount-- },
                                        modifier = Modifier.size(20.dp), // Smaller button size
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Serving", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp)) // Smaller icon inside button
                                    }
                                    Text(
                                        text = "$servingCount", // 45. Serving count value
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp), // Smaller text size
                                        modifier = Modifier.padding(horizontal = 4.dp) // Smaller horizontal padding
                                    )
                                    Button(
                                        onClick = { servingCount++ },
                                        modifier = Modifier.size(20.dp), // Smaller button size
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Serving", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp)) // Smaller icon inside button
                                    }
                                }
                            }

                            // Placeholder for Ingredients List (46. Ingredients value & 47. Ingredients detail)
                            baseIngredients.forEach { baseIngredient ->
                                val parts = baseIngredient.split(" ", limit = 2) // Split to get the numeric part and the rest
                                val originalValue = parts[0].toFloatOrNull() ?: 0f
                                val multipliedValue = originalValue * servingCount
                                val formattedMultipliedValue = if (multipliedValue == floor(multipliedValue)) {
                                    multipliedValue.toInt().toString()
                                } else {
                                    String.format("%.1f", multipliedValue)
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Small box for ingredient value (46)
                                    Box(
                                        modifier = Modifier
                                            .size(width = 40.dp, height = 24.dp) // Adjust size as needed
                                            .clip(RoundedCornerShape(8.dp)) // Increased corner rounding here!
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)), // Light background for the box
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = formattedMultipliedValue,
                                            style = MaterialTheme.typography.bodySmall, // Smaller text for the value
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp)) // Space between box and text
                                    Text(
                                        text = parts[1], // Only show the detail part (47)
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f) // Allow text to take remaining space
                                    )
                                }
                            }
                        }
                    }
                }


                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Nutrition", // 49. Nutrition section header
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Calories:", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "XXX kcal", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Protein:", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "XXg", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Fat:", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "XXg", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Carbs:", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "XXg", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Instructions", // 52. Instruction section header
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                // Placeholder for Instructions
                items(3) { index ->
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "${index + 1}", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Step ${index + 1}: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Comments", // 56. Comment section header
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_profile_placeholder),
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "John Doe", fontWeight = FontWeight.Bold)
                                Text(text = "This is a placeholder comment. Great recipe!", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_profile_placeholder),
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "Jane Smith", fontWeight = FontWeight.Bold)
                                Text(text = "Another placeholder comment. I would try this!", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        // Add some padding at the end of the LazyColumn to make sure content doesn't get hidden by the bottom bar
                        Spacer(modifier = Modifier.height(80.dp)) // Height of your bottom bar + some extra padding
                    }
                }
            }

            // Two sticky buttons at the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), // Slightly transparent surface
                                MaterialTheme.colorScheme.surface // Full color at the bottom
                            ),
                            startY = 0f,
                            endY = 300f // Adjust this value to control the gradient spread
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "Add to Planner" button
                Button(
                    onClick = { /* Handle add to planner */ },
                    modifier = Modifier
                        .weight(0.4f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add to Planner") // You might want a different icon for planner
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Add to Planner")
                }

                Spacer(modifier = Modifier.width(16.dp))

                // "Start Instruction" button
                Button(
                    onClick = { /* Handle navigation to instructions */ },
                    modifier = Modifier
                        .weight(0.6f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Start Instruction", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 411, // Pixel 2 width
    heightDp = 731 // Pixel 2 height
)
@Composable
fun DetailRecipeScreenPreview() {
    PancookTheme {
        DetailRecipeScreen(recipeId = 1, navController = rememberNavController())
    }
}