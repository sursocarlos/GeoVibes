package com.example.geovibes.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.geovibes.ui.theme.TravelBlue
import com.example.geovibes.ui.theme.TextBlack
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // 1. Ubicación: Mairena del Aljarafe, Sevilla
    // Coordenadas aproximadas del centro
    val mairena = LatLng(37.345, -6.065)

    // Estado de la cámara
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mairena, 14f) // Zoom nivel ciudad
    }

    // Propiedades del mapa (Limpiamos la UI de Google para poner la nuestra)
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = false)) }
    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings(
            zoomControlsEnabled = false, // Quitamos botones de zoom feos
            compassEnabled = false,
            myLocationButtonEnabled = false
        ))
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // --- EL MAPA DE FONDO ---
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        )

        // --- BARRA FLOTANTE SUPERIOR (PERFIL + LOGOUT) ---
        // Usamos una Surface para darle forma de "Pastilla" y sombra elegante
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp) // Margen superior para la barra de estado
                .fillMaxWidth()
                .height(64.dp)
                .shadow(8.dp, RoundedCornerShape(32.dp)), // Sombra suave y esquinas redondas
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // ZONA USUARIO (Icono + Email)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f) // Ocupa el espacio disponible
                ) {
                    // Avatar Circular
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(TravelBlue.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = TravelBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Email del Usuario
                    Column {
                        Text(
                            text = "Hola, explorador",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = currentUser?.email ?: "Usuario",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis // Puntos suspensivos si es muy largo
                        )
                    }
                }

                // BOTÓN CERRAR SESIÓN
                IconButton(
                    onClick = {
                        auth.signOut()
                        navController.navigate("login") { popUpTo("map") { inclusive = true } }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = Color(0xFFFF5252) // Un rojo suave para indicar "salir"
                    )
                }
            }
        }
    }
}