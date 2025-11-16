package com.example.geovibes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        val errorMessage = task.exception?.let { exception ->
                            when {
                                exception.message?.contains("badly formatted", true) == true ->
                                    "El correo electrónico no tiene un formato válido."
                                exception.message?.contains("password", true) == true ->
                                    "La contraseña debe tener al menos 6 caracteres"
                                exception.message?.contains("already in use", true) == true ->
                                    "Este correo ya está registrado."
                                else -> "Ha ocurrido un error interno."
                            }
                        } ?: "Ha ocurrido un error interno."

                        onResult(false, errorMessage)
                    }
                }
        }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        val errorMessage = task.exception?.let { exception ->
                            when {
                                exception.message?.contains("badly formatted", true) == true ->
                                    "El correo electrónico no tiene un formato válido."
                                exception.message?.contains("password", true) == true ->
                                    "Contraseña incorrecta."
                                exception.message?.contains("no user record", true) == true ->
                                    "No existe una cuenta con ese correo."
                                else -> "No existe una cuenta con ese correo."
                            }
                        } ?: "Ha ocurrido un error interno."

                        onResult(false, errorMessage)
                    }
                }
        }
    }

}
