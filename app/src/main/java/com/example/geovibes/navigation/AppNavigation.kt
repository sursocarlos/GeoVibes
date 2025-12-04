package com.example.geovibes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.geovibes.auth.LoginScreen
import com.example.geovibes.auth.RegisterScreen
import com.example.geovibes.home.HomeScreen
import com.example.geovibes.ui.screens.ElementFormScreen
import com.example.geovibes.ui.screens.ElementListScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    // Si el usuario ya está logueado, vamos directo al mapa. Si no, al login.
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "map" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        // --- RUTAS CLÁSICAS ---
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("map") { HomeScreen(navController) }

        // --- NUEVAS RUTAS (GESTIÓN DE AVISOS) ---

        // 1. Pantalla de Lista
        composable("elementList") {
            ElementListScreen(navController)
        }

        // 2. Pantalla de Formulario (Crear/Editar)
        // Acepta un parámetro opcional "?id={id}"
        composable(
            route = "elementForm?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            // Recuperamos el ID (si existe) y se lo pasamos a la pantalla
            val id = backStackEntry.arguments?.getString("id")
            ElementFormScreen(navController, elementId = id)
        }
    }
}

