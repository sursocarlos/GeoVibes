package com.example.geovibes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.geovibes.components.GeoVibesTextField
import com.example.geovibes.model.Elemento
import com.example.geovibes.ui.theme.TravelBlue
import com.example.geovibes.viewmodel.ElementsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementFormScreen(
    navController: NavController,
    elementId: String? = null, // Si es null = Crear nuevo. Si tiene texto = Editar.
    viewModel: ElementsViewModel = viewModel()
) {
    // Si estamos editando, buscamos el elemento en la lista del ViewModel
    val elementoExistente = remember {
        if (elementId != null) viewModel.elementos.find { it.id == elementId } else null
    }

    // Estados del formulario (si editamos, rellenamos con lo que había)
    var nombre by remember { mutableStateOf(elementoExistente?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(elementoExistente?.descripcion ?: "") }

    // Control de errores visuales
    var nombreError by remember { mutableStateOf(false) }
    var descError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (elementId == null) "Nuevo Aviso" else "Editar Aviso",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(titleContentColor = TravelBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // CAMPO NOMBRE
            GeoVibesTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = false
                },
                label = "Título del aviso",
                icon = Icons.Default.Title,
                isError = nombreError,
                errorMessage = "Escribe un título"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CAMPO DESCRIPCIÓN
            GeoVibesTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    descError = false
                },
                label = "Descripción detallada",
                icon = Icons.Default.Description,
                isError = descError,
                errorMessage = "Escribe una descripción"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // BOTÓN GUARDAR
            Button(
                onClick = {
                    // Validaciones simples
                    nombreError = nombre.isEmpty()
                    descError = descripcion.isEmpty()

                    if (!nombreError && !descError) {
                        // Creamos el objeto (si es nuevo, el ID lo pone vacío)
                        val nuevoElemento = Elemento(
                            id = elementId ?: "",
                            nombre = nombre,
                            descripcion = descripcion,
                            // Si editamos, mantenemos la fecha original. Si es nuevo, el ViewModel pone la de hoy.
                            fechaCreacion = elementoExistente?.fechaCreacion ?: ""
                        )

                        // Llamamos al cerebro para guardar
                        viewModel.saveElement(nuevoElemento) { success ->
                            if (success) {
                                navController.popBackStack() // Volvemos a la lista
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(25.dp)),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TravelBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Guardar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}