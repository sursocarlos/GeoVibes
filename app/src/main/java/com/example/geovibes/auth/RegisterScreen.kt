package com.example.geovibes.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.geovibes.viewmodel.AuthViewModel


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun RegisterScreen(navController: NavHostController) {
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

        Text("Registrarse", fontSize = 26.sp)

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
                authViewModel.registerUser(email, password) { success, message ->
                    if (success) navController.navigate("map") {
                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                    else Toast.makeText(context, message ?: "Error desconocido", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }) { Text("Registrarse") }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("login") }) {
            Text("¿Ya tienes cuenta?")
        }
    }
}
