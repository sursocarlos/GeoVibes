package com.example.geovibes.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (currentUser != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFBBDEFB), // color de fondo azul claro
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Usuario: ${currentUser.email}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1) // azul oscuro
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        Text("HOME SCREEN — Aquí pondré el mapa")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            auth.signOut()
            navController.navigate("login") { popUpTo("map") { inclusive = true } }
        }) {
            Text("Cerrar sesión")
        }
    }
}
