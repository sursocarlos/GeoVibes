package com.example.geovibes.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person // Icono para el nombre
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.geovibes.R
import com.example.geovibes.components.GeoVibesTextField
import com.example.geovibes.ui.theme.TextGray
import com.example.geovibes.ui.theme.TravelBlue
import com.example.geovibes.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    // Variables de texto
    var nombre by remember { mutableStateOf("") } // NUEVO CAMPO
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Variables de visibilidad
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Variables de error visual
    var nombreError by remember { mutableStateOf(false) } // ERROR NUEVO
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    val isLoading = authViewModel.isLoading
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // 1. CABECERA
        Image(
            painter = painterResource(id = R.drawable.ic_brujula),
            contentDescription = "Logo GeoVibes",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Crea tu cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TravelBlue
        )
        Text(
            text = "Únete a la comunidad de exploradores",
            fontSize = 16.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. FORMULARIO
        GeoVibesTextField(
            value = nombre,
            onValueChange = { nombre = it; nombreError = false },
            label = "Nombre de usuario",
            icon = Icons.Default.Person,
            isError = nombreError,
            errorMessage = "El nombre es obligatorio",
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- EMAIL ---
        GeoVibesTextField(
            value = email,
            onValueChange = { email = it; emailError = false },
            label = "Email",
            icon = Icons.Default.Email,
            isError = emailError,
            errorMessage = if (email.isEmpty()) "Campo obligatorio" else "Formato inválido",
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CONTRASEÑA ---
        GeoVibesTextField(
            value = password,
            onValueChange = { password = it; passwordError = false },
            label = "Contraseña",
            icon = Icons.Default.Lock,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = TextGray)
                }
            },
            isError = passwordError,
            errorMessage = if (password.isEmpty()) "Campo obligatorio" else "Mínimo 6 caracteres",
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CONFIRMAR CONTRASEÑA ---
        GeoVibesTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; confirmPasswordError = false },
            label = "Confirmar contraseña",
            icon = Icons.Default.Lock,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = TextGray)
                }
            },
            isError = confirmPasswordError,
            errorMessage = if (confirmPassword.isEmpty()) "Campo obligatorio" else "Las contraseñas no coinciden",
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. BOTÓN DE ACCIÓN
        if (isLoading) {
            CircularProgressIndicator(color = TravelBlue)
        } else {
            Button(
                onClick = {
                    // Validaciones
                    nombreError = nombre.isEmpty()
                    emailError = email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    passwordError = password.isEmpty() || password.length < 6
                    confirmPasswordError = confirmPassword.isEmpty() || (password.isNotEmpty() && password != confirmPassword)

                    if (!nombreError && !emailError && !passwordError && !confirmPasswordError) {
                        // Llamamos al nuevo registro pasando el NOMBRE
                        authViewModel.registerUser(email, password, nombre) { success, message ->
                            if (success) {
                                navController.navigate("map") { popUpTo("register") { inclusive = true } }
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(25.dp)),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TravelBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿Ya tienes cuenta?", color = TextGray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Inicia sesión",
                color = TravelBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = !isLoading) {
                    navController.navigate("login")
                }
            )
        }
    }
}