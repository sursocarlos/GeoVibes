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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val authViewModel = AuthViewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 1. Añadimos esta variable para controlar si se ve o no
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Iniciar sesión", fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))
        OutlinedTextField(value = email, modifier = Modifier.fillMaxWidth(), onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            // Aquí cambiamos la transformación según la variable
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            // Aquí añadimos el icono del ojito
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Descripción para accesibilidad
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
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
        },
            modifier = Modifier.fillMaxWidth(),
            ) { Text("Iniciar sesión") }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta?")
        }
    }
}
