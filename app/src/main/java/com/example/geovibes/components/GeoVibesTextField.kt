package com.example.geovibes.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.geovibes.ui.theme.InputGray
import com.example.geovibes.ui.theme.TravelBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoVibesTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null, // Icono opcional al principio
    trailingIcon: @Composable (() -> Unit)? = null, // Icono al final (ojito)
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    // Input moderno: Sin bordes duros, fondo suave y esquinas redondas
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = if (icon != null) {
            { Icon(imageVector = icon, contentDescription = null, tint = TravelBlue) }
        } else null,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        shape = RoundedCornerShape(16.dp), // ¡Esquinas muy redondeadas!
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TravelBlue,
            unfocusedBorderColor = Color.Transparent, // Sin borde cuando no escribes
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = InputGray, // Fondo gris suave
            unfocusedContainerColor = InputGray,
            errorContainerColor = InputGray
        ),
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    )
}