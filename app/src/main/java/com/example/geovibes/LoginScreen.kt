package com.example.geovibes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val auth = FirebaseAuth.getInstance()

    fun isValidEmail(input: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return emailRegex.matches(input)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Iniciar sesión", fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            // VALIDACIONES
            when {
                email.isBlank() || password.isBlank() -> {
                    errorMsg = "Rellena todos los campos."
                    return@Button
                }

                !isValidEmail(email) -> {
                    errorMsg = "Correo no válido."
                    return@Button
                }

                password.length < 6 -> {
                    errorMsg = "La contraseña debe tener mínimo 6 caracteres."
                    return@Button
                }
            }

            // LOGIN
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    navController.navigate("map") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                .addOnFailureListener {
                    errorMsg = "Error: ${it.message}"
                }

        }) {
            Text("Entrar")
        }

        Spacer(Modifier.height(10.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("No tienes cuenta?")
        }

        errorMsg?.let {
            Spacer(Modifier.height(10.dp))
            Text(it, color = Color.Red)
        }
    }
}



