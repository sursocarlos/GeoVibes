package com.example.geovibes.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.geovibes.model.Elemento
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate

class ElementsViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Estado: La lista de elementos que mostramos en pantalla
    var elementos by mutableStateOf<List<Elemento>>(emptyList())
        private set

    // Estado: ¿Es el usuario actual un administrador?
    var isAdmin by mutableStateOf(false)
        private set

    init {
        checkUserRole()     // 1. Averiguar quién es el usuario
        listenToElements()  // 2. Empezar a escuchar la lista
    }

    // --- 1. VERIFICAR EL ROL DEL USUARIO ---
    private fun checkUserRole() {
        val userId = auth.currentUser?.uid ?: return
        // Buscamos en el nodo "users" -> "ID del usuario" -> "rol"
        val userRef = db.getReference("users").child(userId).child("rol")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rol = snapshot.getValue(String::class.java)
                // Si el valor es "admin", activamos los superpoderes
                isAdmin = (rol == "admin")
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // --- 2. ESCUCHAR CAMBIOS EN LA LISTA DE ELEMENTOS ---
    private fun listenToElements() {
        // Escuchamos todo el nodo "elementos"
        db.getReference("elementos").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Elemento>()
                for (child in snapshot.children) {
                    val elem = child.getValue(Elemento::class.java)
                    // Importante: Asignamos el ID de Firebase al objeto local
                    elem?.let { lista.add(it.copy(id = child.key ?: "")) }
                }
                elementos = lista // Actualizamos la lista de la pantalla
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // --- 3. FUNCIONES DE ADMINISTRADOR (Crear, Editar, Borrar) ---

    // Guardar (sirve para Crear Nuevo o Editar Existente)
    fun saveElement(elemento: Elemento, onResult: (Boolean) -> Unit) {
        if (!isAdmin) return // Seguridad: Si no es admin, no hacemos nada

        val ref = db.getReference("elementos")

        if (elemento.id.isEmpty()) {
            // CREAR: Si no tiene ID, es nuevo. Usamos push() para generar ID
            val newRef = ref.push()
            // Añadimos la fecha de hoy automáticamente (Requisito PDF)
            val fechaHoy = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            val newElement = elemento.copy(fechaCreacion = fechaHoy)

            newRef.setValue(newElement).addOnCompleteListener { onResult(it.isSuccessful) }
        } else {
            // EDITAR: Si tiene ID, actualizamos ese hijo concreto
            ref.child(elemento.id).setValue(elemento).addOnCompleteListener { onResult(it.isSuccessful) }
        }
    }

    // Borrar
    fun deleteElement(id: String, onResult: (Boolean) -> Unit) {
        if (isAdmin) {
            db.getReference("elementos").child(id).removeValue()
                .addOnCompleteListener { onResult(it.isSuccessful) }
        }
    }
}

