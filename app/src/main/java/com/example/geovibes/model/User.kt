package com.example.geovibes.model

data class User(
    val nombre: String = "",
    val telefono: String = "",
    val email: String = "",
    val rol: String = "usuario" // Por defecto todos son usuarios normales
)
