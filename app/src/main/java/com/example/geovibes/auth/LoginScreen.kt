package com.example.geovibes.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.geovibes.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    // Variables de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Variable de visibilidad (ojito)
    var passwordVisible by remember { mutableStateOf(false) }

    // Variables de error visual (Rojo)
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val isLoading = authViewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Iniciar sesión", fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))

        // --- CAMPO EMAIL ---
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false // Limpiamos error al escribir
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = emailError,
            supportingText = {
                if (emailError) {
                    // Lógica inteligente: Vacío -> "Campo obligatorio", Texto mal -> "Formato inválido"
                    Text(if (email.isEmpty()) "Campo obligatorio" else "Formato de correo inválido")
                }
            }
        )

        // --- CAMPO CONTRASEÑA ---
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false // Limpiamos error al escribir
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = passwordError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            supportingText = {
                if (passwordError) {
                    Text("Campo obligatorio")
                }
            }
        )

        // Nota: Al usar supportingText, el Spacer intermedio ya no es necesario o puede ser pequeño
        Spacer(modifier = Modifier.height(8.dp))

        // --- BOTÓN / CARGA ---
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    // 1. Validaciones Visuales Locales
                    // Email: Error si vacío O formato incorrecto
                    emailError = email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()

                    // Password: Error si vacío
                    passwordError = password.isEmpty()

                    // 2. Si no hay errores visuales, llamamos al ViewModel
                    if (!emailError && !passwordError) {
                        authViewModel.loginUser(email, password) { success, message ->
                            if (success) navController.navigate("map") {
                                popUpTo("login") { inclusive = true }
                            }
                            else Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar sesión")
            }
        }

        TextButton(
            onClick = { navController.navigate("register") },
            enabled = !isLoading
        ) {
            Text("¿No tienes cuenta?")
        }
    }
}