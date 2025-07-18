package com.wesleyaldrich.pancook.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import kotlin.math.floor
import kotlin.math.log
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import com.wesleyaldrich.pancook.ui.navigation.Screen
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * A reusable, auto-scrolling image carousel with infinite loop behavior and indicator dots.
 *
 * @param images A list of drawable resource IDs for the images to display.
 * @param modifier The modifier to be applied to the carousel.
 * @param autoScrollDelay The delay in milliseconds between automatic scrolls. Defaults to 3000ms.
 * @param imageContentDescription A function that provides content description for each image based on its index.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoScrollingImageCarousel(
    images: List<Int>,
    modifier: Modifier = Modifier,
    autoScrollDelay: Long = 3000L,
    imageContentDescription: (Int) -> String = { index -> "Image ${index + 1}" }
) {
    if (images.isEmpty()) {
        Spacer(modifier = modifier) // Render nothing or a placeholder if no images
        return
    }

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState)
    val isDragged by lazyListState.interactionSource.collectIsDraggedAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Key to restart auto-scroll effect
    var autoScrollKey by remember { mutableStateOf(0) }

    // Auto-scroll logic with user interaction pause
    LaunchedEffect(autoScrollKey, isDragged, isPressed, images.size) {
        if (images.isNotEmpty() && !isDragged && !isPressed) {
            // Calculate an initial index that's "far enough" to give the illusion of infinite scroll
            // and ensure it starts at a valid image index.
            val initialOffset = Int.MAX_VALUE / 2
            val initialItem = initialOffset - (initialOffset % images.size)
            lazyListState.scrollToItem(initialItem)

            while (true) {
                delay(autoScrollDelay)
                // Only auto-scroll if user is not dragging or pressing and not already scrolling
                if (!isDragged && !isPressed && !lazyListState.isScrollInProgress) {
                    val nextIndex = lazyListState.firstVisibleItemIndex + 1
                    lazyListState.animateScrollToItem(nextIndex)
                } else {
                    break // Exit the current while loop, the LaunchedEffect will restart if conditions allow
                }
            }
        }
    }

    // Listener for scroll state changes to reset autoScrollKey when user stops scrolling
    LaunchedEffect(lazyListState.isScrollInProgress, isDragged, isPressed) {
        if (!lazyListState.isScrollInProgress && !isDragged && !isPressed) {
            // Small delay to ensure user has fully stopped interacting
            delay(500)
            autoScrollKey++ // Increment key to re-trigger the auto-scroll LaunchedEffect
        }
    }

    // Keep track of the currently visible item for the indicator dots, accounting for infinite scroll
    val currentImageIndex = remember {
        derivedStateOf {
            if (images.isNotEmpty() && lazyListState.firstVisibleItemIndex >= 0) {
                lazyListState.firstVisibleItemIndex % images.size
            } else 0
        }
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                flingBehavior = flingBehavior,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(Int.MAX_VALUE, key = { it }) { index ->
                    val actualIndex = index % images.size
                    Image(
                        painter = painterResource(id = images[actualIndex]),
                        contentDescription = imageContentDescription(actualIndex),
                        modifier = Modifier
                            .fillParentMaxWidth() // CRITICAL: Ensure each item takes full width
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Box( // This Box ensures the dots are centered horizontally
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center // This centers the dots within their Row
            ) {
                images.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                colorResource(R.color.primary).copy(
                                    alpha = if (index == currentImageIndex.value) 0.8f else 0.4f
                                )
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                coroutineScope.launch {
                                    val currentFirstVisibleItem = lazyListState.firstVisibleItemIndex
                                    val currentVisibleImageIndex = currentFirstVisibleItem % images.size
                                    val targetImageIndex = index

                                    // Calculate the optimal scroll direction (forward or backward)
                                    // to reach the targetImageIndex from the current visible image index.
                                    val totalItems = images.size
                                    val forwardScroll = (targetImageIndex - currentVisibleImageIndex + totalItems) % totalItems
                                    val backwardScroll = (currentVisibleImageIndex - targetImageIndex + totalItems) % totalItems

                                    val targetScrollIndex: Int =
                                        if (forwardScroll <= backwardScroll) {
                                            currentFirstVisibleItem + forwardScroll
                                        } else {
                                            currentFirstVisibleItem - backwardScroll
                                        }

                                    lazyListState.animateScrollToItem(targetScrollIndex)
                                    autoScrollKey++ // Reset auto-scroll after manual interaction
                                }
                            }
                    )
                    if (index < images.size - 1) Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRecipeScreen(recipeId: Int, navController: NavController) {
    var servingCount by remember { mutableStateOf(1) }
    var showShareDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val currentRecipe = remember(recipeId) {
        allRecipes.find { it.id == recipeId }
    }

    if (currentRecipe == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    var currentUpvoteCount by remember(currentRecipe.id) { mutableStateOf(currentRecipe.upvoteCount) }
    var isBookmarked by remember(currentRecipe.id) {
        mutableStateOf(bookmarkedRecipes.contains(currentRecipe))
    }

    val displayImages = remember(currentRecipe.image) {
        listOf(currentRecipe.image, R.drawable.salad, R.drawable.fudgy_brownies)
    }

    fun formatCount(count: Int): String {
        if (count < 1000) return count.toString()
        val exp = floor(log(count.toDouble(), 1000.0)).toInt()
        val units = arrayOf("", "k", "M", "B", "T")
        val formattedValue = String.format("%.1f", count / Math.pow(1000.0, exp.toDouble()))
        return formattedValue.replace(".0", "") + units[exp]
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
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Replaced the old LazyRow and Box for dots with the new Composable
                        AutoScrollingImageCarousel(images = displayImages)
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentRecipe.title,
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.nopal),
                                            contentDescription = "Creator Profile",
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surfaceVariant),
                                            contentScale = ContentScale.Crop,
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = currentRecipe.recipeMaker,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 16.sp, color = colorResource(R.color.primary).copy(alpha = 1f))
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_clock),
                                            contentDescription = "Duration",
                                            modifier = Modifier.size(20.dp),
                                            tint = colorResource(R.color.primary).copy(alpha = 1f)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(text = currentRecipe.duration, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp))
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            currentUpvoteCount = if (currentUpvoteCount == currentRecipe.upvoteCount) currentUpvoteCount + 1 else currentUpvoteCount - 1
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.ThumbUp, contentDescription = "Upvote", tint = colorResource(R.color.primary).copy(alpha = 1f))
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(currentUpvoteCount),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            if (isBookmarked) {
                                                bookmarkedRecipes.remove(currentRecipe)
                                            } else {
                                                bookmarkedRecipes.add(currentRecipe)
                                            }
                                            isBookmarked = !isBookmarked
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Bookmark,
                                                contentDescription = "Bookmark Border",
                                                tint = colorResource(R.color.primary),
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Icon(
                                                imageVector = Icons.Filled.Bookmark,
                                                contentDescription = "Bookmark Fill",
                                                tint = if (isBookmarked) colorResource(R.color.primary) else colorResource(R.color.accent_yellow),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(bookmarkedRecipes.size),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { showShareDialog = true },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.Share, contentDescription = "Share", tint = colorResource(R.color.primary).copy(alpha = 1f))
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(567), // Static share count for now
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = currentRecipe.description,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Ingredients",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                    modifier = Modifier.weight(1f)
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_serving),
                                        contentDescription = "Serving Count",
                                        modifier = Modifier.size(20.dp),
                                        tint = colorResource(R.color.primary).copy(alpha = 1f)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))

                                    Button(
                                        onClick = { if (servingCount > 1) servingCount-- },
                                        modifier = Modifier.size(20.dp),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Serving", tint = colorResource(R.color.accent_yellow).copy(alpha = 1f), modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "$servingCount",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    Button(
                                        onClick = { servingCount++ },
                                        modifier = Modifier.size(20.dp),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Serving", tint = colorResource(R.color.accent_yellow).copy(alpha = 1f), modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            val ingredientValueBoxWidth = 60.dp
                            val indentSpacerWidth = 8.dp

                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                currentRecipe.ingredients.forEach { ingredient ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(width = ingredientValueBoxWidth, height = 20.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Text(
                                                text = "%.2f".format(ingredient.qty * servingCount),
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                                                color = colorResource(R.color.accent_yellow).copy(alpha = 1f),
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.padding(end = 8.dp),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(indentSpacerWidth))

                                        Text(
                                            text = ingredient.unitMeasurement,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.width(70.dp)
                                        )
                                        Spacer(modifier = Modifier.width(indentSpacerWidth))

                                        Text(
                                            text = ingredient.name,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Nutrition",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.Top
                            ) {
                                val fixedNutritionItemWidth = 80.dp

                                currentRecipe.nutritionFacts.forEach { fact ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(fixedNutritionItemWidth)
                                    ) {
                                        Text(
                                            text = fact.label,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .height(32.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = fact.value,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                                                color = colorResource(R.color.accent_yellow).copy(alpha = 1f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Instructions",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            currentRecipe.steps.forEach { step ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(CircleShape)
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "${step.stepNumber}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = step.description,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Comments",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (currentRecipe.comments.isEmpty()) {
                                Text(
                                    text = "No comments yet. Be the first to share your thoughts!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                currentRecipe.comments.forEach { comment ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (comment.imageUrl != null && comment.imageUrl.startsWith("content://")) {
                                            Image(
                                                painter = rememberAsyncImagePainter(comment.imageUrl),
                                                contentDescription = "User Avatar",
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(CircleShape)
                                                    .background(colorResource(R.color.accent_yellow).copy(alpha = 1f)),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_serving),
                                                contentDescription = "User Avatar",
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(CircleShape)
                                                    .background(colorResource(R.color.accent_yellow).copy(alpha = 1f)),
                                                contentScale = ContentScale.Crop,
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = comment.userName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text(text = comment.commentText, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
                                            comment.isUpvote?.let { isUpvote ->
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = if (isUpvote) Icons.Default.ThumbUp else Icons.Default.ThumbDown,
                                                        contentDescription = if (isUpvote) "Upvoted" else "Downvoted",
                                                        tint = if (isUpvote) Color.Green else Color.Red,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = if (isUpvote) "Recommended" else "Not Recommended",
                                                        fontSize = 12.sp,
                                                        color = if (isUpvote) Color.Green else Color.Red
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                    MaterialTheme.colorScheme.surface
                                ),
                                startY = 0f,
                                endY = 25f
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { /* Handle add to planner */ },
                        modifier = Modifier
                            .weight(0.5f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add to Planner")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Add to Planner", fontSize = 15.5.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            navController.navigate(Screen.Instruction.createRoute(recipeId))
                        },
                        modifier = Modifier
                            .weight(0.5f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Start Instruction", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showShareDialog,
            enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
            exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
        ) {
            Dialog(
                onDismissRequest = { showShareDialog = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnClickOutside = true
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            showShareDialog = false
                        },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    var offsetY by remember { mutableStateOf(0f) }
                    val coroutineScope = rememberCoroutineScope()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.5f)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .offset { IntOffset(x = 0, y = offsetY.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        if (offsetY > size.height / 4f) { // Dismiss if dragged down by more than 25% of its height
                                            showShareDialog = false
                                        } else {
                                            // Animate back to original position if not dismissed
                                            coroutineScope.launch {
                                                offsetY = 0f
                                            }
                                        }
                                    }
                                ) { change, dragAmount ->
                                    change.consume()
                                    // Only allow dragging downwards (positive y-axis delta)
                                    offsetY = (offsetY + dragAmount.y).coerceAtLeast(0f)
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Drag handle
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(4.dp)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
                                    .padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Share to...",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )

                            val shareLink = "https://pancook.app/recipe/${currentRecipe.id}"

                            LazyRow(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                item {
                                    ShareOption(
                                        icon = Icons.Default.ContentCopy,
                                        text = "Copy link"
                                    ) {
                                        val clipboardManager = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                        val clipData = android.content.ClipData.newPlainText("Recipe Link", shareLink)
                                        clipboardManager.setPrimaryClip(clipData)
                                        showShareDialog = false
                                    }
                                }
                                item {
                                    ShareOption(
                                        icon = Icons.Default.ChatBubble,
                                        text = "WhatsApp"
                                    ) {
                                        val sendIntent: Intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, shareLink)
                                            type = "text/plain"
                                            setPackage("com.whatsapp")
                                        }
                                        try {
                                            context.startActivity(sendIntent)
                                        } catch (e: Exception) {
                                            // Handle if WhatsApp is not installed
                                            showShareDialog = false
                                        }
                                        showShareDialog = false
                                    }
                                }
                                item {
                                    ShareOption(
                                        icon = Icons.Default.People,
                                        text = "Facebook"
                                    ) {
                                        val sendIntent: Intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, shareLink)
                                            type = "text/plain"
                                            setPackage("com.facebook.katana")
                                        }
                                        try {
                                            context.startActivity(sendIntent)
                                        } catch (e: Exception) {
                                            // Handle if Facebook is not installed
                                            showShareDialog = false
                                        }
                                        showShareDialog = false
                                    }
                                }
                                item {
                                    ShareOption(
                                        icon = Icons.Default.MoreHoriz,
                                        text = "More"
                                    ) {
                                        val sendIntent: Intent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, shareLink)
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, null))
                                        showShareDialog = false
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = shareLink,
                                onValueChange = { /* Read-only */ },
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Link") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        val clipboardManager = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                        val clipData = android.content.ClipData.newPlainText("Recipe Link", shareLink)
                                        clipboardManager.setPrimaryClip(clipData)
                                        showShareDialog = false
                                    }) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showShareDialog = false },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Close", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShareOption(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 800
)
@Composable
fun DetailRecipeScreenPreview() {
    PancookTheme {
        DetailRecipeScreen(recipeId = 1, navController = rememberNavController())
    }
}