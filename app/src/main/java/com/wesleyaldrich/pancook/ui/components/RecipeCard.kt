package com.wesleyaldrich.pancook.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.wesleyaldrich.pancook.model.Recipe
import com.wesleyaldrich.pancook.ui.navigation.Screen.Home.title
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins
import androidx.compose.ui.graphics.Color // Import Color for explicit tinting

/**
 * A reusable card component for displaying content with an image, title, and a toggle switch.
 *
 * @param modifier Modifier to be applied to the card.
 * @param imagePainter The painter for the image to display.
 * @param title The main title text for the card.
 * @param description A short description text for the card.
 * @param duration The duration text for the recipe (e.g., "30 min").
 * @param likeCount The number of likes for the item.
 * @param imageHeight The height of the image.
 * @param onClick Lambda to handle card clicks.
 * @param isBookmarked Boolean to indicate if the recipe is bookmarked.
 * @param onBookmarkClick Lambda to handle bookmark icon clicks.
 * @param isUpvoted Boolean to indicate if the recipe is upvoted by the current user. // Added this param for consistency
 * @param onUpvoteClick Lambda to handle upvote icon clicks. // Added this param for consistency
 */

@Composable
fun RecipeReusableCard(
    modifier: Modifier = Modifier,
    imagePainter: Painter,
    title: String,
    description: String,
    duration: String,
    likeCount: Int,
    imageHeight: Dp = 180.dp,
    onClick: () -> Unit = {},
    isBookmarked: Boolean = false,
    onBookmarkClick: () -> Unit = {},
    isUpvoted: Boolean = false, // Added this parameter
    onUpvoteClick: () -> Unit = {} // Added this parameter
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Card Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.TopCenter),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDurationShort(duration), // Apply formatting
                        fontFamily = nunito,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp, // Toned down from 16.sp
                        color = colorResource(R.color.accent_yellow),
                        modifier = Modifier
                            .background(
                                colorResource(R.color.primary).copy(alpha = 0.75f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Row {
                        // Bookmark button with layered icons for border effect
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(colorResource(R.color.primary).copy(alpha = 0.75f))
                                .clickable { onBookmarkClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            // Border Icon (always white, slightly larger)
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = "Bookmark Border",
                                tint = Color.White, // Fixed white for the border
                                modifier = Modifier.size(20.dp) // Slightly larger than the fill icon
                            )
                            // Fill Icon (dynamic color, slightly smaller)
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = "Bookmark Fill",
                                // Corrected tinting to match MyRecipeCard.kt
                                tint = if (isBookmarked) colorResource(R.color.accent_yellow) else colorResource(R.color.primary).copy(alpha = 0.75f),
                                modifier = Modifier.size(16.dp) // Smaller than the border icon
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 8.dp)
                        .background(
                            colorResource(R.color.primary).copy(alpha = 0.75f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Upvote button with layered icons for border effect
                    Box(
                        modifier = Modifier
                            .size(28.dp) // Toned down from 30.dp
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(R.color.primary).copy(alpha = 0.75f))
                            .clickable { onUpvoteClick() }, // Make clickable
                        contentAlignment = Alignment.Center
                    ) {
                        // Border Icon (always white, slightly larger)
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Upvote Border",
                            tint = Color.White, // Fixed white for the border
                            modifier = Modifier.size(18.dp) // Toned down from 20.dp
                        )
                        // Fill Icon (dynamic color, slightly smaller)
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Upvote Fill",
                            tint = if (isUpvoted) colorResource(R.color.accent_yellow) else Color.White, // *** THIS IS THE CORRECTED LINE ***
                            modifier = Modifier.size(14.dp) // Toned down from 16.dp
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (likeCount >= 1000) {
                            "${likeCount / 1000}k"
                        } else {
                            "$likeCount"
                        },
                        fontFamily = nunito,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = colorResource(R.color.accent_yellow),
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp, // Toned down from 16.sp
                    maxLines = 1, // Limit text to 1 line
                    overflow = TextOverflow.Ellipsis, // Add ellipsis if text overflows
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
                Text(
                    text = description,
                    fontFamily = nunito,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp, // Toned down from 12.sp
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

/**
 * Helper function to format duration strings into a shorter format.
 * Examples: "1 hour 20 minutes" -> "1h 20m", "30 min" -> "30m", "2 days" -> "2d"
 */
private fun formatDurationShort(duration: String): String {
    val durationLower = duration.lowercase()
    val components = mutableListOf<String>()

    // Regex to find and extract numbers followed by common time units (hour, min, day, week)
    val hoursMatch = Regex("(\\d+)\\s+(hour|hours)").find(durationLower)
    hoursMatch?.let {
        components.add("${it.groupValues[1]}h")
    }

    val minutesMatch = Regex("(\\d+)\\s+(min|minutes)").find(durationLower)
    minutesMatch?.let {
        components.add("${it.groupValues[1]}m")
    }

    val daysMatch = Regex("(\\d+)\\s+(day|days)").find(durationLower)
    daysMatch?.let {
        components.add("${it.groupValues[1]}d")
    }

    val weeksMatch = Regex("(\\d+)\\s+(week|weeks)").find(durationLower)
    weeksMatch?.let {
        components.add("${it.groupValues[1]}w")
    }

    // Handle cases where duration might just be "30 min" without "hour"
    if (components.isEmpty()) {
        val singleUnitMatch = Regex("(\\d+)\\s*(min|minutes|hour|hours|day|days|week|weeks)").find(durationLower)
        singleUnitMatch?.let {
            val value = it.groupValues[1]
            when (it.groupValues[2]) { // Make 'when' exhaustive
                "min", "minutes" -> components.add("${value}m")
                "hour", "hours" -> components.add("${value}h")
                "day", "days" -> components.add("${value}d")
                "week", "weeks" -> components.add("${value}w")
                else -> components.add(duration) // Fallback for unhandled units
            }
        }
    }

    return if (components.isEmpty()) duration else components.joinToString(" ")
}


@Preview(showBackground = true)
@Composable
fun RecipeReusableCardPreview() {
    PancookTheme {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            RecipeReusableCard(
                imagePainter = painterResource(id = R.drawable.salad),
                title = "Delicious Salad",
                description = "by Nunuk",
                duration = "15 minutes", // Changed for preview
                likeCount = 1234, // This will now show as "1k Likes"
                isBookmarked = true, // Preview with bookmarked state
                isUpvoted = true // Preview with upvoted state
            )
            Spacer(modifier = Modifier.height(16.dp))
            RecipeReusableCard(
                imagePainter = painterResource(id = R.drawable.salad),
                title = "Another Delicious Dish",
                description = "by Chef John",
                duration = "1 hour 30 minutes", // Changed for preview
                likeCount = 987, // This will remain "987 Likes"
                isBookmarked = false, // Preview with unbookmarked state
                isUpvoted = false // Preview with unupvoted state
            )
            Spacer(modifier = Modifier.height(16.dp))
            RecipeReusableCard(
                imagePainter = painterResource(id = R.drawable.salad),
                title = "Quick Snack",
                description = "by Mary",
                duration = "5 min", // Changed for preview
                likeCount = 10000, // This will show as "10k Likes"
                isBookmarked = true, // Preview with bookmarked state
                isUpvoted = true // Preview with upvoted state
            )
            Spacer(modifier = Modifier.height(16.dp))
            RecipeReusableCard(
                imagePainter = painterResource(id = R.drawable.salad),
                title = "Long Cook Meal",
                description = "by Chef Gordon",
                duration = "2 hours", // Changed for preview
                likeCount = 5000,
                isBookmarked = true,
                isUpvoted = false // Preview with unupvoted state
            )
        }
    }
}