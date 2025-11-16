package com.example.geovibes.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.geovibes.viewmodel.AuthViewModel
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val authViewModel = AuthViewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Iniciar sesión", fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.loginUser(email, password) { success, message ->
                    if (success) navController.navigate("map") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                    else Toast.makeText(context, message ?: "Error desconocido", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }) { Text("Iniciar sesión") }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta?")
        }
    }
}
