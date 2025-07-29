package com.wesleyaldrich.pancook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.wesleyaldrich.pancook.ui.MainScreen
import com.wesleyaldrich.pancook.ui.navigation.RootNavigation
import com.wesleyaldrich.pancook.ui.theme.PancookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PancookTheme {
                // The Surface should fill the whole screen
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Call the new RootNavigation instead of MainScreen
                    RootNavigation()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun Preview() {
    PancookTheme {
//        MainScreen()
    }
}