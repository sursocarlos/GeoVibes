package com.example.geovibes.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta "Ocean Travel"
val TravelBlue = Color(0xFF0066CC)      // Azul principal (Botones, Títulos)
val OceanCyan = Color(0xFF00B4D8)       // Azul secundario (Detalles)
val LightBlueBg = Color(0xFFEDF6FF)     // Fondo muy clarito (para inputs)

// Neutros
val TextBlack = Color(0xFF1A1C1E)       // Texto principal (casi negro, más suave)
val TextGray = Color(0xFF757575)        // Texto secundario
val InputGray = Color(0xFFF3F4F6)       // Fondo de los campos de texto
val White = Color(0xFFFFFFFF)

// Colores para el tema Oscuro (por si acaso)
val DarkBluePrimary = Color(0xFF82B1FF)
val DarkBlueSecondary = Color(0xFF80DEEA)
val DarkBackground = Color(0xFF121212)

// Mantenemos referencias antiguas por compatibilidad si es necesario,
// pero apuntando a los nuevos
val Purple80 = DarkBluePrimary
val PurpleGrey80 = DarkBlueSecondary
val Pink80 = OceanCyan

val Purple40 = TravelBlue
val PurpleGrey40 = TextGray
val Pink40 = OceanCyan