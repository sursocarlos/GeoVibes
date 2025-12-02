package com.example.geovibes.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var isLoading by mutableStateOf(false)
        private set

    fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        // Validaciones locales
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onResult(false, "El formato del correo no es correcto")
            return
        }
        if (password.length < 6) {
            onResult(false, "La contraseña debe tener al menos 6 caracteres")
            return
        }

        isLoading = true
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthUserCollisionException -> "Este correo ya está registrado."
                            is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil."
                            is FirebaseNetworkException -> "Sin conexión a internet."
                            else -> "Error al registrarse: ${task.exception?.localizedMessage}"
                        }
                        onResult(false, errorMessage)
                    }
                }
        }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        // Validaciones locales
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onResult(false, "El formato del correo no es correcto")
            return
        }
        if (password.isEmpty()) {
            onResult(false, "La contraseña no puede estar vacía")
            return
        }

        isLoading = true
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        val exception = task.exception

                        // AQUÍ ESTÁ EL CAMBIO DE SEGURIDAD:
                        val errorMessage = when (exception) {
                            // Unificamos las dos excepciones en una sola respuesta
                            is FirebaseAuthInvalidUserException,
                            is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."

                            is FirebaseNetworkException -> "Sin conexión a internet. Comprueba tu red."
                            is FirebaseTooManyRequestsException -> "Demasiados intentos fallidos. Por favor, espera un momento."
                            else -> "Error al iniciar sesión: ${exception?.localizedMessage}"
                        }
                        onResult(false, errorMessage)
                    }
                }
        }
    }
}