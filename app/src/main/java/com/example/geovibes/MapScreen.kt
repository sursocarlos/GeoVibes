package com.example.geovibes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MapScreen(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("MAP SCREEN — Aquí pondremos el mapa")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            auth.signOut()
            navController.navigate("login") {
                popUpTo("map") { inclusive = true }
            }
        }) {
            Text("Cerrar sesión")
        }
    }
}
