package com.wesleyaldrich.pancook.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
// import androidx.compose.foundation.clickable // Remove this import if no longer needed anywhere in this file
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
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins
import androidx.compose.ui.graphics.Color
import kotlin.math.floor
import kotlin.math.log
// Make sure to add this import if you still use clickable for the bookmark/delete buttons
import androidx.compose.foundation.clickable // Ensure this is imported if other clickables remain


/**
 * A reusable card component for displaying content with an image, title, and actions.
 *
 * @param modifier Modifier to be applied to the card.
 * @param imagePainter The painter for the image to display.
 * @param title The main title text for the card.
 * @param description A short description text for the card.
 * @param duration The duration text for the recipe (e.g., "30 min").
 * @param upvoteCount The number of upvotes for the item.
 * @param isBookmarked Boolean to indicate if the recipe is bookmarked.
 * @param onBookmarkClick Lambda to handle bookmark icon clicks.
 * @param onDeleteClick Lambda to handle delete icon clicks.
 * @param hideDeleteButton Boolean to control the visibility of the delete button.
 * @param isUpvoted Boolean to indicate if the recipe is upvoted by the current user.
 * @param onUpvoteClick Lambda to handle upvote icon clicks. (This lambda will now be unused in this component)
 */
@Composable
fun ReusableCard(
    modifier: Modifier = Modifier,
    imagePainter: Painter,
    title: String,
    description: String,
    duration: String,
    upvoteCount: Int,
    isBookmarked: Boolean = false,
    onBookmarkClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    hideDeleteButton: Boolean = false, // New parameter
    isUpvoted: Boolean = false, // New parameter for upvote state
    onUpvoteClick: () -> Unit = {} // New parameter for upvote click (will be ignored for upvote icon)
) {
    // Helper function for formatting count (copied from DetailRecipeScreen for consistency)
    fun formatCount(count: Int): String {
        if (count < 1000) return count.toString()
        val exp = floor(log(count.toDouble(), 1000.0)).toInt()
        val units = arrayOf("", "k", "M", "B", "T")
        val formattedValue = String.format("%.1f", count / Math.pow(1000.0, exp.toDouble()))
        return formattedValue.replace(".0", "") + units[exp]
    }

    Card(
        modifier = modifier
            .width(180.dp),
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
                    .height(140.dp) // Adjusted image height for better balance
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Card Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight() // Fill the Box height
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
                        fontSize = 14.sp,
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
                                .clickable { onBookmarkClick() }, // This remains clickable
                            contentAlignment = Alignment.Center
                        ) {
                            // Border Icon (always white, slightly larger)
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = "Bookmark Border",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            // Fill Icon (dynamic color, slightly smaller)
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = "Bookmark Fill",
                                tint = if (isBookmarked) colorResource(R.color.accent_yellow) else colorResource(R.color.primary).copy(alpha = 0.75f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Delete button (conditionally visible)
                        if (!hideDeleteButton) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(colorResource(R.color.primary).copy(alpha = 0.75f))
                                    .clickable { onDeleteClick() }, // This remains clickable
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = colorResource(R.color.accent_yellow),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
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
                            .size(24.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(R.color.primary).copy(alpha = 0.75f))
                        // --- REMOVED: .clickable { onUpvoteClick() } ---
                        , // Keep the comma if you remove the clickable modifier
                        contentAlignment = Alignment.Center
                    ) {
                        // Border Icon (always white, slightly larger)
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Upvotes Border",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        // Fill Icon (dynamic color, slightly smaller)
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Upvotes Fill",
                            tint = if (isUpvoted) colorResource(R.color.accent_yellow) else colorResource(R.color.primary).copy(alpha = 0.75f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    // Using the new formatCount helper for consistency
                    Text(
                        text = formatCount(upvoteCount),
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
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = description,
                    fontFamily = nunito,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
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
fun ReusableCardPreview() {
    PancookTheme {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            ReusableCard(
                imagePainter = painterResource(id = R.drawable.salad),
                title = "Delicious Salad",
                description = "by Nunuk",
                duration = "15 minutes",
                upvoteCount = 1234,
                isBookmarked = true,
                onBookmarkClick = {},
                onDeleteClick = {},
                isUpvoted = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            ReusableCard(
                imagePainter = painterResource(id = R.drawable.spicy_noodle),
                title = "Another Delicious Dish",
                description = "by Chef John",
                duration = "1 hour 30 minutes",
                upvoteCount = 987,
                isBookmarked = false,
                onBookmarkClick = {},
                onDeleteClick = {},
                isUpvoted = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            ReusableCard(
                imagePainter = painterResource(id = R.drawable.chicken_stir_fry),
                title = "Quick Snack",
                description = "by Mary",
                duration = "5 min",
                upvoteCount = 10000,
                isBookmarked = true,
                onBookmarkClick = {},
                onDeleteClick = {},
                isUpvoted = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            ReusableCard(
                imagePainter = painterResource(id = R.drawable.vegetable_curry),
                title = "Long Cook Meal",
                description = "by Chef Gordon",
                duration = "2 hours",
                upvoteCount = 5000,
                isBookmarked = true,
                onBookmarkClick = {},
                onDeleteClick = {},
                isUpvoted = false,
                hideDeleteButton = true
            )
        }
    }
}