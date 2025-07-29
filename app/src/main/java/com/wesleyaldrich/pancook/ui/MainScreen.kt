// ui/MainScreen.kt
package com.wesleyaldrich.pancook.ui

import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.navigation.NavigationGraph
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.ui.theme.nunito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val isSpecialScreen = currentRoute?.startsWith(Screen.DetailRecipe.route.substringBefore('/')) == true ||
            currentRoute?.startsWith(Screen.Instruction.route.substringBefore('/')) == true ||
            currentRoute?.startsWith(Screen.RecipeCompletion.route.substringBefore('/')) == true ||
            currentRoute == Screen.GroceryList.route ||
            currentRoute == Screen.SavedRecipe.route ||
            currentRoute == Screen.Add.route

    Scaffold(
        topBar = {
            if (!isSpecialScreen) {
                CustomTopBar(
                    username = "Pancokers",
                    onFirstButtonClick = { navController.navigate(Screen.GroceryList.route) },
                    onSecondButtonClick = { navController.navigate(Screen.SavedRecipe.route) }
                )
            }
        },
        bottomBar = {
            if (!isSpecialScreen) {
                // We call the new Custom PanCook Bar
                CustomPanCookBar(navController = navController, currentRoute = currentRoute)
            }
        }
        // 1. The separate FloatingActionButton has been removed from here, as you requested.
    ) { paddingValues ->
        // This modifier has been updated to prevent content from being cut off.
        NavigationGraph(
            navController,
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            onLogout = onLogout
        )
    }
}

/**
 * The new bottom bar. It uses a simple Row to prevent layout issues
 * while allowing for the custom "Add" button design.
 */
@Composable
fun CustomPanCookBar(navController: NavHostController, currentRoute: String?) {
    val items = listOf(Screen.Home, Screen.MyRecipe, Screen.Add, Screen.Planner, Screen.Profile)
    val screensWithoutLabels = listOf(Screen.Add.route) // Add screens here that shouldn't have a label

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            if (screen == Screen.Add) {
                // 2. The "Add" button is now inside the bar, with the elevated look.
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.Add.route) },
                        modifier = Modifier.offset(y = (-40).dp),
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Recipe")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (!screensWithoutLabels.contains(screen.route)) {
                        Text(
                            text = screen.title,
                            fontSize = 12.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTopBar(
    username: String,
    profileImageResId: Int = R.drawable.nopal, // replace with your image
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp)
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left Side: Profile picture + Username
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(profileImageResId),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = nunito
            )
        }

        // Right Side: Two pill buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButtonPill(
                icon = Icons.AutoMirrored.Filled.List,
                onClick = onFirstButtonClick
            )
            IconButtonPill(
                icon = Icons.Default.SaveAlt,
                onClick = onSecondButtonClick
            )
        }
    }
}

@Composable
fun IconButtonPill(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(5, 26, 57))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White
        )
    }
}