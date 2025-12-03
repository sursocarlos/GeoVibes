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
import com.example.geovibes.ui.theme.TextBlack
import com.example.geovibes.ui.theme.TextGray
import com.example.geovibes.ui.theme.TravelBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoVibesTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
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
        shape = RoundedCornerShape(16.dp),

        colors = OutlinedTextFieldDefaults.colors(
            // 1. Bordes y Fondos
            focusedBorderColor = TravelBlue,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = InputGray,
            unfocusedContainerColor = InputGray,
            errorContainerColor = InputGray,

            // 2. TEXTOS Y ETIQUETAS (Normal)
            focusedLabelColor = TravelBlue,
            focusedTextColor = TextBlack,
            unfocusedLabelColor = TextGray,
            unfocusedTextColor = TextBlack,

            // 3. CORRECCIÓN DEL ERROR (Aquí estaba el fallo del Samsung)
            // Forzamos que el texto siga siendo NEGRO aunque haya error
            errorTextColor = TextBlack,
            errorLabelColor = MaterialTheme.colorScheme.error, // La etiqueta sí la queremos roja
            errorCursorColor = TravelBlue, // El cursor en azul o rojo, como prefieras (azul queda bien)

            cursorColor = TravelBlue
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