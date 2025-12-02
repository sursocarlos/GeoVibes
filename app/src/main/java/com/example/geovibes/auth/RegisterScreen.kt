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
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    // Variables de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Variables de visibilidad (ojitos)
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Variables de error visual (Rojo)
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    val isLoading = authViewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Registrarse", fontSize = 26.sp)

        Spacer(Modifier.height(20.dp))

        // --- CAMPO EMAIL ---
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = emailError,
            supportingText = {
                if (emailError) {
                    // Lógica inteligente: Si está vacío -> "Campo obligatorio", si no -> "Formato inválido"
                    Text(if (email.isEmpty()) "Campo obligatorio" else "Formato de correo inválido")
                }
            }
        )

        // --- CAMPO CONTRASEÑA ---
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
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
                    // Lógica inteligente: Si está vacío -> "Campo obligatorio", si no -> "Mínimo 6 caracteres"
                    Text(if (password.isEmpty()) "Campo obligatorio" else "Mínimo 6 caracteres")
                }
            }
        )

        // --- CAMPO CONFIRMAR CONTRASEÑA ---
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = false
            },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = confirmPasswordError,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            supportingText = {
                if (confirmPasswordError) {
                    Text(if (confirmPassword.isEmpty()) "Campo obligatorio" else "Las contraseñas no coinciden")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- BOTÓN / CARGA ---
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    // 1. Email
                    emailError = email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher (email).matches()

                    // 2. Password (Error si vacía o corta)
                    passwordError = password.isEmpty() || password.length < 6

                    // 3. Confirmar Password (LÓGICA MEJORADA)
                    // Solo marcamos error si está vacía...
                    // O SI (la primera NO está vacía Y son diferentes)
                    confirmPasswordError = confirmPassword.isEmpty() || (password.isNotEmpty() && password != confirmPassword)

                    // Solo enviamos si no hay errores
                    if (!emailError && !passwordError && !confirmPasswordError) {
                        authViewModel.registerUser(email, password) { success, message ->
                            if (success) {
                                navController.navigate("map") {
                                    popUpTo("register") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }

        TextButton(
            onClick = { navController.navigate("login") },
            enabled = !isLoading
        ) {
            Text("¿Ya tienes cuenta?")
        }
    }
}