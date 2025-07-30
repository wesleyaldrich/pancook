// ui/MainScreen.kt
package com.wesleyaldrich.pancook.ui

import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.Dp // ✅ FIXED: Add this import
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wesleyaldrich.pancook.R
import com.wesleyaldrich.pancook.ui.navigation.IconResource
import com.wesleyaldrich.pancook.ui.navigation.NavigationGraph
import com.wesleyaldrich.pancook.ui.navigation.Screen
import com.wesleyaldrich.pancook.ui.theme.nunito
import com.wesleyaldrich.pancook.ui.theme.poppins

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
                CustomPanCookBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { paddingValues ->
        NavigationGraph(
            navController,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            onLogout = onLogout
        )
    }
}

@Composable
fun CustomPanCookBar(navController: NavHostController, currentRoute: String?) {
    val items = listOf(Screen.Home, Screen.MyRecipe, Screen.Add, Screen.Planner, Screen.Profile)
    val screensWithoutLabels = listOf(Screen.Add.route)

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
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Recipe", tint = MaterialTheme.colorScheme.onPrimary)
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
                    AppIcon(
                        iconResource = screen.icon,
                        contentDescription = screen.title,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (!screensWithoutLabels.contains(screen.route)) {
                        Text(
                            text = screen.title,
                            fontSize = 12.sp,
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppIcon(
    iconResource: IconResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    size: Dp = 32.dp
) {
    val combinedModifier = modifier.size(size)

    when (iconResource) {
        is IconResource.Drawable -> Icon(
            painter = painterResource(id = iconResource.id),
            contentDescription = contentDescription,
            modifier = combinedModifier,
            tint = tint
        )
        is IconResource.Vector -> Icon(
            imageVector = iconResource.imageVector,
            contentDescription = contentDescription,
            modifier = combinedModifier,
            tint = tint
        )
    }
}

@Composable
fun CustomTopBar(
    username: String,
    profileImageResId: Int = R.drawable.nopal,
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

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButtonPill(
                icon = IconResource.Drawable(R.drawable.nav_grocery_list),
                onClick = onFirstButtonClick
            )
            IconButtonPill(
                icon = IconResource.Drawable(R.drawable.nav_saved_recipe),
                onClick = onSecondButtonClick
            )
        }
    }
}

@Composable
fun IconButtonPill(
    icon: IconResource,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(5, 26, 57))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        AppIcon(
            iconResource = icon,
            contentDescription = null,
            tint = Color.White,
            size = 32.dp
        )
    }
}