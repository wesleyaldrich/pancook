// ui/MainScreen.kt
package com.wesleyaldrich.pancook.ui

import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.navigation.NavigationGraph
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.ui.theme.nunito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val screens = listOf(
        Screen.Home,
        Screen.MyRecipe,
        Screen.Add, // This will be handled by the FAB, but kept in `screens` for route definition
        Screen.Planner,
        Screen.Profile
    )

    // Determine if the current route is the DetailRecipe screen or Instruction screen
    val isSpecialScreen = currentRoute?.startsWith(Screen.DetailRecipe.route.substringBefore('/')) == true ||
            currentRoute?.startsWith(Screen.Instruction.route.substringBefore('/')) == true ||
            currentRoute?.startsWith(Screen.RecipeCompletion.route.substringBefore('/')) == true ||
            currentRoute == Screen.GroceryList.route ||
            currentRoute == Screen.SavedRecipe.route // ADD THIS LINE

    Scaffold(
        topBar = {
            if (!isSpecialScreen) { // Conditionally show TopAppBar
                CustomTopBar(
                    username = "Pancokers",
                    onFirstButtonClick = {
                        navController.navigate(Screen.GroceryList.route){
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onSecondButtonClick = {
                        navController.navigate(Screen.SavedRecipe.route){
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (!isSpecialScreen) { // Conditionally show NavigationBar
                CustomBottomBar(
                    navController = navController,
                    currentRoute = currentRoute ?: ""
                )
            }
        },
        floatingActionButton = {
            if (!isSpecialScreen) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.Add.route)
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .offset(y = 48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Center Action")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        NavigationGraph(navController, modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun CustomBottomBar(navController: NavHostController, currentRoute: String) {
    val items = listOf(Screen.Home, Screen.MyRecipe, Screen.Planner, Screen.Profile)

    NavigationBar(
        tonalElevation = 8.dp,
    ) {
        items.forEachIndexed { index, screen ->
            if (index == 2) { // This index should correspond to where you want the FAB to be. If 'Add' is the 3rd item (index 2), then this is correct.
                // Empty space for center FAB
                Spacer(modifier = Modifier.weight(1f))
            }

            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        screen.icon, // Using screen.icon for dynamic icons
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) }
            )
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