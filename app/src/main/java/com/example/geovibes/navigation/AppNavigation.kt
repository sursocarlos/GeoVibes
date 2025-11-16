package com.example.geovibes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.geovibes.auth.LoginScreen
import com.example.geovibes.auth.RegisterScreen
import com.example.geovibes.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "map" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") { LoginScreen(navController) }

        composable("register") { RegisterScreen(navController) }

        composable("map") { HomeScreen(navController) }
    }
}

