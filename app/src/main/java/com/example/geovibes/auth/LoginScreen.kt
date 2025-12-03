package com.example.geovibes.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.geovibes.R
import com.example.geovibes.components.GeoVibesTextField // Importa tu nuevo componente
import com.example.geovibes.ui.theme.TextGray
import com.example.geovibes.ui.theme.TravelBlue
import com.example.geovibes.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val isLoading = authViewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Todo centrado verticalmente
    ) {
        // 1. CABECERA: Logo e Intro
        // Asegúrate de que R.drawable.ic_brujula existe (lo vi en tus archivos)
        Image(
            painter = painterResource(id = R.drawable.ic_brujula),
            contentDescription = "Logo GeoVibes",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bienvenido a GeoVibes",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TravelBlue
        )
        Text(
            text = "Inicia sesión para continuar explorando",
            fontSize = 16.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. FORMULARIO (Usando nuestro componente personalizado)
        GeoVibesTextField(
            value = email,
            onValueChange = { email = it; emailError = false },
            label = "Email",
            icon = Icons.Default.Email,
            isError = emailError,
            errorMessage = if (email.isEmpty()) "Campo obligatorio" else "Formato inválido",
            enabled = !isLoading,
            // CORRECCIÓN 1: Teclado específico para Email (aparece la @)
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        GeoVibesTextField(
            value = password,
            onValueChange = { password = it; passwordError = false },
            label = "Contraseña",
            icon = Icons.Default.Lock, // Icono de candado
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = TextGray)
                }
            },
            isError = passwordError,
            errorMessage = "Campo obligatorio",
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. BOTÓN DE ACCIÓN
        if (isLoading) {
            CircularProgressIndicator(color = TravelBlue)
        } else {
            Button(
                onClick = {
                    emailError = email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    passwordError = password.isEmpty()

                    if (!emailError && !passwordError) {
                        authViewModel.loginUser(email, password) { success, message ->
                            if (success) navController.navigate("map") { popUpTo("login") { inclusive = true } }
                            else Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                    contentColor = Color.White // <--- 1. FUERZA EL COLOR AQUÍ
                )
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // <--- 2. Y FUÉRZALO AQUÍ TAMBIÉN (DOBLE SEGURIDAD)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. FOOTER
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿Nuevo aquí?", color = TextGray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Crea una cuenta",
                color = TravelBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = !isLoading) {
                    // CORRECCIÓN 2: Navegación limpia
                    navController.navigate("register") {
                        // Al ir al registro, quitamos el Login de la pila para no acumular
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}