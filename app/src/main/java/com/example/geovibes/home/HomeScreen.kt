package com.example.geovibes.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
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
import com.example.geovibes.ui.theme.TextBlack
import com.example.geovibes.ui.theme.TravelBlue
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase // Necesario para leer el nombre
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Estado para guardar el nombre del usuario
    var userName by remember { mutableStateOf("Cargando...") }

    // EFECTO: Leer el nombre de la base de datos al cargar la pantalla
    LaunchedEffect(Unit) {
        val uid = currentUser?.uid
        if (uid != null) {
            FirebaseDatabase.getInstance().getReference("users").child(uid).child("nombre").get()
                .addOnSuccessListener { snapshot ->
                    // Si existe el nombre, lo ponemos. Si no, usamos el email o "Usuario"
                    userName = snapshot.value as? String ?: currentUser.email ?: "Usuario"
                }
                .addOnFailureListener {
                    userName = currentUser.email ?: "Usuario"
                }
        }
    }

    // Configuración del mapa (Mairena)
    val mairena = LatLng(37.345, -6.065)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mairena, 14f)
    }
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = false)) }
    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = false, compassEnabled = false, myLocationButtonEnabled = false))
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. EL MAPA
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        )

        // 2. BARRA SUPERIOR (PERFIL CON NOMBRE)
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .height(64.dp)
                .shadow(8.dp, RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier.size(40.dp).background(TravelBlue.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = TravelBlue, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text("Hola, explorador", fontSize = 12.sp, color = Color.Gray)
                        // AQUÍ MOSTRAMOS EL NOMBRE REAL
                        Text(
                            text = userName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                IconButton(onClick = {
                    auth.signOut()
                    navController.navigate("login") { popUpTo("map") { inclusive = true } }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar sesión", tint = Color(0xFFFF5252))
                }
            }
        }

        // 3. BOTÓN VER AVISOS
        ExtendedFloatingActionButton(
            onClick = { navController.navigate("elementList") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            containerColor = TravelBlue,
            contentColor = Color.White,
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
            text = { Text("Ver Avisos") }
        )
    }
}