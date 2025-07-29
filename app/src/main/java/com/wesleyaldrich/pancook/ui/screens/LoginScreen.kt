package com.wesleyaldrich.pancook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.wesleyaldrich.pancook.ui.navigation.Screen

@Composable
fun LoginScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Page")
        Button(onClick = {
            // After login is successful, navigate to the main screen
            navController.navigate(Screen.Main.route) {
                // Pop up to the login screen to remove it from the back stack
                popUpTo(Screen.Login.route) {
                    inclusive = true // This also removes the login screen itself
                }
            }
        }) {
            Text("Log In")
        }
    }
}