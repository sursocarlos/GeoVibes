package com.example.geovibes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.geovibes.navigation.AppNavigation
import com.example.geovibes.ui.theme.GeoVibesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoVibesTheme {
                GeoVibesApp()
            }
        }
    }
}

@Composable
fun GeoVibesApp() {
    val navController = rememberNavController()
    AppNavigation(navController)
}
