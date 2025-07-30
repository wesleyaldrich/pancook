package com.wesleyaldrich.pancook.ui.screens

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem as ExoPlayerMediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.model.MediaItem
import com.wesleyaldrich.pancook.model.MediaType
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.ui.theme.PancookTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.roundToInt


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
            when (it.groupValues[2]) {
                "min", "minutes" -> components.add("${value}m")
                "hour", "hours" -> components.add("${value}h")
                "day", "days" -> components.add("${value}d")
                "week", "weeks" -> components.add("${value}w")
                else -> components.add(duration)
            }
        }
    }

    return if (components.isEmpty()) duration else components.joinToString(" ")
}

/**
 * A reusable, auto-scrolling image/video carousel with infinite loop behavior and indicator dots.
 * This carousel now uses HorizontalPager for one-item-at-a-time swiping and pauses on video playback.
 *
 * @param mediaItems A list of MediaItem objects (image resource IDs or video URIs) to display.
 * @param modifier The modifier to be applied to the carousel.
 * @param autoScrollDelay The delay in milliseconds between automatic scrolls. Defaults to 3000ms.
 * @param contentDescription A function that provides content description for each item.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AutoScrollingImageCarousel(
    mediaItems: List<MediaItem>,
    modifier: Modifier = Modifier,
    autoScrollDelay: Long = 3000L,
    contentDescription: (Int, MediaType) -> String = { index, type -> "${type.name} ${index + 1}" }
) {
    if (mediaItems.isEmpty()) {
        Spacer(modifier = modifier)
        return
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { Int.MAX_VALUE }
    )
    val coroutineScope = rememberCoroutineScope()
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val context = LocalContext.current

    val exoPlayer: ExoPlayer = remember { ExoPlayer.Builder(context).build() }
    var isVideoPlaying by remember { mutableStateOf(false) }
    var isVideoFullscreen by remember { mutableStateOf(false) }
    var autoScrollKey by remember { mutableStateOf(0) }

    val activity = context as? Activity
    val window = activity?.window

    fun enterFullscreen() {
        if (activity == null || window == null) return
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE // Allows rotation to both landscape modes
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        isVideoFullscreen = true
    }

    fun exitFullscreen() {
        if (activity == null || window == null) return
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED // Return to default orientation handling
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.show(WindowInsetsCompat.Type.systemBars())
        isVideoFullscreen = false
    }

    DisposableEffect(Unit) {
        val playerListener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isVideoPlaying = isPlaying
            }
        }
        exoPlayer.addListener(playerListener)

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                exoPlayer.pause()
            } else if (event == Lifecycle.Event.ON_START) {
                // We don't want it to auto-play on resume, user should press play
                // exoPlayer.play()
            }
        }
        val lifecycle = (context as? LifecycleOwner)?.lifecycle
        lifecycle?.addObserver(lifecycleObserver)

        onDispose {
            lifecycle?.removeObserver(lifecycleObserver)
            // Ensure we exit fullscreen and reset orientation when the composable is disposed
            if (isVideoFullscreen) {
                exitFullscreen()
            }
            exoPlayer.release()
        }
    }

    LaunchedEffect(autoScrollKey, isDragged, isVideoPlaying) {
        if (pagerState.currentPage == 0 && mediaItems.isNotEmpty()) {
            val initialIndex = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % mediaItems.size)
            pagerState.scrollToPage(initialIndex)
        }
        if (!isDragged && !pagerState.isScrollInProgress && !isVideoPlaying) {
            while (true) {
                delay(autoScrollDelay)
                val nextPage = pagerState.currentPage + 1
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    LaunchedEffect(pagerState.isScrollInProgress, isDragged) {
        if (!pagerState.isScrollInProgress && !isDragged) {
            delay(500)
            autoScrollKey++
        }
    }

    val currentImageIndex = remember {
        derivedStateOf {
            if (mediaItems.isNotEmpty()) {
                pagerState.currentPage % mediaItems.size
            } else 0
        }
    }

    // Fullscreen dialog for video playback
    if (isVideoFullscreen) {
        Dialog(
            onDismissRequest = { exitFullscreen() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            BackHandler { exitFullscreen() }
            AndroidView(
                factory = { ctx ->
                    StyledPlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                        // Ensure fullscreen button toggles the orientation explicitly
                        setFullscreenButtonClickListener {
                            if (isVideoFullscreen) exitFullscreen() else enterFullscreen()
                        }
                    }
                },
                modifier = Modifier.fillMaxSize().background(Color.Black)
            )
        }
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val actualIndex = page % mediaItems.size
                val mediaItem = mediaItems[actualIndex]

                when (mediaItem.type) {
                    MediaType.IMAGE -> {
                        Image(
                            painter = painterResource(id = mediaItem.resourceId!!),
                            contentDescription = contentDescription(actualIndex, MediaType.IMAGE),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    MediaType.VIDEO -> {
                        // Only show the embedded player if not in fullscreen mode
                        if (!isVideoFullscreen) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                AndroidView(
                                    factory = { ctx ->
                                        StyledPlayerView(ctx).apply {
                                            player = exoPlayer
                                            useController = false // Start with no controller for click-to-play
                                            setFullscreenButtonClickListener { enterFullscreen() }
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize().background(Color.Black)
                                ) { playerView ->
                                    // Update useController based on isVideoPlaying state
                                    playerView.useController = isVideoPlaying

                                    if (actualIndex == currentImageIndex.value) {
                                        val exoMediaItem = ExoPlayerMediaItem.fromUri(mediaItem.uri!!)
                                        // Load media only if it's different from the current one to avoid re-buffering
                                        if (exoPlayer.currentMediaItem != exoMediaItem) {
                                            exoPlayer.setMediaItem(exoMediaItem)
                                            exoPlayer.prepare()
                                            exoPlayer.playWhenReady = false // Don't auto-play
                                        }
                                    } else {
                                        // Pause and seek to start if not the current video
                                        if (exoPlayer.isPlaying) {
                                            exoPlayer.pause()
                                            exoPlayer.seekTo(0)
                                        }
                                    }
                                }

                                if (!isVideoPlaying && actualIndex == currentImageIndex.value) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                exoPlayer.play()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayCircleOutline,
                                            contentDescription = "Play Video",
                                            tint = Color.White.copy(alpha = 0.8f),
                                            modifier = Modifier.size(80.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            // When in fullscreen, this Box acts as a placeholder or prevents rendering the embedded view
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black))
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                mediaItems.forEachIndexed { index, _ ->
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
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                coroutineScope.launch {
                                    val currentPagerPage = pagerState.currentPage
                                    val currentVisibleImageIndex = currentPagerPage % mediaItems.size
                                    val totalItems = mediaItems.size
                                    // Calculate shortest path to target page
                                    val forwardScroll = (index - currentVisibleImageIndex + totalItems) % totalItems
                                    val backwardScroll = (currentVisibleImageIndex - index + totalItems) % totalItems
                                    val targetScrollOffset = if (forwardScroll <= backwardScroll) forwardScroll else -backwardScroll
                                    pagerState.animateScrollToPage(currentPagerPage + targetScrollOffset)
                                    autoScrollKey++
                                }
                            }
                    )
                    if (index < mediaItems.size - 1) Spacer(modifier = Modifier.width(4.dp))
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

    val currentRecipe by remember(recipeId) {
        derivedStateOf { allRecipes.find { it.id == recipeId } }
    }

    if (currentRecipe == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    val recipe = currentRecipe!!

    val isRecipeBookmarked = bookmarkedRecipes.contains(recipe)
    val isRecipeUpvoted = upvotedRecipes.contains(recipe)
    val currentDisplayUpvoteCount = recipe.upvoteCount

    val displayMediaItems = remember(recipe.images, recipe.videos) {
        val items = mutableListOf<MediaItem>()
        recipe.videos.forEach { videoUri ->
            items.add(MediaItem(type = MediaType.VIDEO, uri = videoUri))
        }
        recipe.images.forEach { imageResourceId ->
            items.add(MediaItem(type = MediaType.IMAGE, resourceId = imageResourceId))
        }
        items
    }

    fun formatCount(count: Int): String {
        if (count < 1000) return count.toString()
        val exp = floor(log(count.toDouble(), 1000.0)).toInt()
        val units = arrayOf("", "k", "M", "B", "T")
        val formattedValue = String.format("%.1f", count / Math.pow(1000.0, exp.toDouble()))
        return formattedValue.replace(".0", "") + units[exp]
    }

    fun formatIngredientQuantity(qty: Double, unit: String): Pair<String, String> {
        val calculatedQty = qty * servingCount
        val formattedNumber = if (calculatedQty % 1.0 == 0.0) {
            "%.0f".format(calculatedQty)
        } else {
            "%.2f".format(calculatedQty)
        }

        return when (unit.lowercase()) {
            "g" -> {
                if (calculatedQty >= 750) {
                    "%.2f".format(calculatedQty / 1000.0).removeSuffix(".00") to "kg"
                } else {
                    formattedNumber to "g"
                }
            }
            "ml" -> {
                if (calculatedQty >= 750) {
                    "%.2f".format(calculatedQty / 1000.0).removeSuffix(".00") to "L"
                } else {
                    formattedNumber to "ml"
                }
            }
            "pcs", "items" -> {
                formattedNumber to unit
            }
            else -> {
                formattedNumber to unit
            }
        }
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
                        AutoScrollingImageCarousel(mediaItems = displayMediaItems)
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
                                    text = recipe.title,
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.nopal),
                                            contentDescription = "Creator Profile",
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surfaceVariant),
                                            contentScale = ContentScale.Crop,
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = recipe.recipeMaker,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp, color = colorResource(R.color.primary).copy(alpha = 1f))
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_clock),
                                            contentDescription = "Duration",
                                            modifier = Modifier.size(18.dp),
                                            tint = colorResource(R.color.primary).copy(alpha = 1f)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(text = formatDurationShort(recipe.duration), style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            if (upvotedRecipes.contains(recipe)) {
                                                upvotedRecipes.remove(recipe)
                                                recipe.upvoteCount--
                                            } else {
                                                upvotedRecipes.add(recipe)
                                                recipe.upvoteCount++
                                            }
                                        },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.size(22.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.ThumbUp,
                                                contentDescription = "Upvote Border",
                                                tint = colorResource(R.color.primary),
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Icon(
                                                imageVector = Icons.Filled.ThumbUp,
                                                contentDescription = "Upvote Fill",
                                                tint = if (isRecipeUpvoted) colorResource(R.color.primary) else MaterialTheme.colorScheme.surface,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(currentDisplayUpvoteCount),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            if (bookmarkedRecipes.contains(recipe)) {
                                                bookmarkedRecipes.remove(recipe)
                                            } else {
                                                bookmarkedRecipes.add(recipe)
                                            }
                                        },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.size(22.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Bookmark,
                                                contentDescription = "Bookmark Border",
                                                tint = colorResource(R.color.primary),
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Icon(
                                                imageVector = Icons.Filled.Bookmark,
                                                contentDescription = "Bookmark Fill",
                                                tint = if (isRecipeBookmarked) colorResource(R.color.primary) else MaterialTheme.colorScheme.surface,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(bookmarkedRecipes.size),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { showShareDialog = true },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Icon(Icons.Filled.Share, contentDescription = "Share", tint = colorResource(R.color.primary).copy(alpha = 1f))
                                    }
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = formatCount(567),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = recipe.description,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Ingredients",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                    modifier = Modifier.weight(1f)
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_serving),
                                        contentDescription = "Serving Count",
                                        modifier = Modifier.size(18.dp),
                                        tint = colorResource(R.color.primary).copy(alpha = 1f)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))

                                    Button(
                                        onClick = { if (servingCount > 1) servingCount-- },
                                        modifier = Modifier.size(18.dp),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Serving", tint = colorResource(R.color.accent_yellow).copy(alpha = 1f), modifier = Modifier.size(14.dp))
                                    }
                                    Text(
                                        text = "$servingCount",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    Button(
                                        onClick = { servingCount++ },
                                        modifier = Modifier.size(18.dp),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f))
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Serving", tint = colorResource(R.color.accent_yellow).copy(alpha = 1f), modifier = Modifier.size(14.dp))
                                    }
                                }
                            }

                            val ingredientValueBoxWidth = 60.dp
                            val indentSpacerWidth = 8.dp

                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                recipe.ingredients.forEach { ingredient ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(ingredientValueBoxWidth)
                                                .height(20.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            val (formattedQty, _) = formatIngredientQuantity(ingredient.qty, ingredient.unitMeasurement)
                                            Text(
                                                text = formattedQty,
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                                color = colorResource(R.color.accent_yellow).copy(alpha = 1f),
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.padding(end = 8.dp),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(indentSpacerWidth))

                                        Text(
                                            text = formatIngredientQuantity(ingredient.qty, ingredient.unitMeasurement).second,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.width(70.dp)
                                        )
                                        Spacer(modifier = Modifier.width(indentSpacerWidth))

                                        Text(
                                            text = ingredient.name,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { /* Handle add to groceries */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Add to Groceries", tint = MaterialTheme.colorScheme.onPrimary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Add to Groceries", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
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
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "Nutrition",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.Top
                            ) {
                                val commonNutritionItemWidth = 70.dp

                                recipe.nutritionFacts.forEach { fact ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(commonNutritionItemWidth)
                                    ) {
                                        Text(
                                            text = fact.label,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .height(30.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = fact.value,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
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
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            recipe.steps.forEach { step ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(colorResource(R.color.primary).copy(alpha = 1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "${step.stepNumber}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = step.description,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
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
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colorResource(R.color.primary).copy(alpha = 1f)),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (recipe.comments.isEmpty()) {
                                Text(
                                    text = "No comments yet. Be the first to share your thoughts!",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                recipe.comments.forEach { comment ->
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
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(colorResource(R.color.accent_yellow).copy(alpha = 1f)),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_serving),
                                                contentDescription = "User Avatar",
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(colorResource(R.color.accent_yellow).copy(alpha = 1f)),
                                                contentScale = ContentScale.Crop,
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = comment.userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(text = comment.commentText, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp))
                                            comment.isUpvote?.let { isUpvote ->
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = if (isUpvote) Icons.Default.ThumbUp else Icons.Default.ThumbDown,
                                                        contentDescription = if (isUpvote) "Upvoted" else "Downvoted",
                                                        tint = if (isUpvote) Color.Green else Color.Red,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = if (isUpvote) "Recommended" else "Not Recommended",
                                                        fontSize = 11.sp,
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
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add to Planner")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Add to Planner", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            navController.navigate(Screen.Instruction.createRoute(recipeId))
                        },
                        modifier = Modifier
                            .weight(0.5f)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary).copy(alpha = 1f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Start Instruction", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
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
                                        if (offsetY > size.height / 4f) {
                                            showShareDialog = false
                                        } else {
                                            coroutineScope.launch {
                                                offsetY = 0f
                                            }
                                        }
                                    }
                                ) { change, dragAmount ->
                                    change.consume()
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
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(4.dp)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
                                    .padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Share to...",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )

                            val shareLink = "app://pancook.recipe/${recipe.id}"

                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
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
                                label = { Text("Link", fontSize = 14.sp) },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        val clipboardManager = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                        val clipData = android.content.ClipData.newPlainText("Recipe Link", shareLink)
                                        clipboardManager.setPrimaryClip(clipData)
                                        showShareDialog = false
                                    }) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(20.dp))
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showShareDialog = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Close", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
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
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
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
        DetailRecipeScreen(recipeId = 4, navController = rememberNavController())
    }
}