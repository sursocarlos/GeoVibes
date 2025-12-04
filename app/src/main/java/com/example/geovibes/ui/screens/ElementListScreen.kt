package com.example.geovibes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geovibes.ui.theme.TravelBlue
import com.example.geovibes.ui.theme.TextGray
import com.example.geovibes.viewmodel.ElementsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementListScreen(
    navController: NavController,
    // Usamos el mismo ViewModel para compartir datos
    viewModel: ElementsViewModel = viewModel()
) {
    val elementos = viewModel.elementos
    val isAdmin = viewModel.isAdmin // <-- AQUÍ ESTÁ LA CLAVE: Leemos si es admin

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tablón de Avisos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TravelBlue
                )
            )
        },
        floatingActionButton = {
            // SEGURIDAD VISUAL: Solo mostramos el botón (+) si es Admin
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { navController.navigate("elementForm") }, // Navega al formulario vacío
                    containerColor = TravelBlue,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir")
                }
            }
        }
    ) { padding ->
        // Si la lista está vacía, mostramos un mensaje bonito
        if (elementos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay avisos todavía", color = TextGray)
            }
        } else {
            // LISTA DE ELEMENTOS
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)) // Fondo gris clarito
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(elementos) { elemento ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Cabecera de la tarjeta
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = elemento.nombre,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                // Fecha pequeña a la derecha
                                Text(
                                    text = elemento.fechaCreacion,
                                    fontSize = 12.sp,
                                    color = TextGray
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Descripción
                            Text(
                                text = elemento.descripcion,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )

                            // SEGURIDAD VISUAL: Botones de Acción (Solo Admin)
                            if (isAdmin) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    // Botón EDITAR
                                    TextButton(onClick = {
                                        // Navega al formulario pasando el ID para editar
                                        navController.navigate("elementForm?id=${elemento.id}")
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Editar")
                                    }

                                    // Botón BORRAR
                                    val context = androidx.compose.ui.platform.LocalContext.current

                                    TextButton(
                                        onClick = {
                                            viewModel.deleteElement(elemento.id) { success ->
                                                if (success) {
                                                    android.widget.Toast.makeText(context, "Elemento eliminado", android.widget.Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Borrar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}