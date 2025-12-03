package com.example.geovibes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.geovibes.navigation.AppNavigation
import com.example.geovibes.ui.theme.GeoVibesTheme
import androidx.compose.runtime.* // Importante para el estado
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Guardamos la referencia del Splash Screen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // 2. Variable para controlar si la app está lista
        var isAppReady = false

        // 3. Le decimos a la Splash: "No te vayas mientras isAppReady sea falso"
        splashScreen.setKeepOnScreenCondition {
            !isAppReady // Se mantiene visible mientras NO esté lista
        }

        setContent {
            GeoVibesTheme {
                // Simulamos una mini carga o esperamos a que Compose pinte
                // Esto es un truco visual para evitar el parpadeo blanco
                LaunchedEffect(Unit) {
                    delay(500) // Esperamos medio segundo (puedes ajustar esto)
                    isAppReady = true // ¡Ya puedes quitar la splash!
                }

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