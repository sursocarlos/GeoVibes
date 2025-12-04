package com.example.geovibes.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geovibes.model.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    // Instancia de la Base de Datos
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()

    var isLoading by mutableStateOf(false)
        private set

    // CAMBIO IMPORTANTE: Ahora la función acepta 'nombre: String'
    fun registerUser(email: String, password: String, nombre: String, onResult: (Boolean, String?) -> Unit) {

        // Validaciones locales (incluyendo nombre)
        if (nombre.isEmpty()) {
            onResult(false, "El nombre es obligatorio")
            return
        }
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
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // Creamos el usuario con el NOMBRE que nos llega
                            val newUser = User(
                                nombre = nombre, // <--- AQUÍ SE GUARDA
                                email = email,
                                rol = "usuario"
                            )
                            // Guardamos en Realtime Database
                            db.getReference("users").child(userId).setValue(newUser)
                                .addOnCompleteListener { dbTask ->
                                    isLoading = false
                                    // Éxito total (Auth + DB)
                                    onResult(true, null)
                                }
                        } else {
                            isLoading = false
                            onResult(true, null)
                        }
                    } else {
                        isLoading = false
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
                        val errorMessage = when (exception) {
                            is FirebaseAuthInvalidUserException,
                            is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
                            is FirebaseNetworkException -> "Sin conexión a internet. Comprueba tu red."
                            else -> "Error al iniciar sesión: ${exception?.localizedMessage}"
                        }
                        onResult(false, errorMessage)
                    }
                }
        }
    }
}