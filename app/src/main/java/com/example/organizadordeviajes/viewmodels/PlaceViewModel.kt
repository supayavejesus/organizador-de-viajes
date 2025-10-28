package com.example.organizadordeviajes.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizadordeviajes.data.RetrofitInstance
import com.example.organizadordeviajes.data.model.Place
import kotlinx.coroutines.launch

class PlaceViewModel : ViewModel() {

    // 🧍 Nombre del usuario actual (para validaciones y navegación)
    var currentUserName: String = ""
        private set

    fun setCurrentUserName(name: String) {
        currentUserName = name.trim().lowercase()
        println("✅ Usuario actual en ViewModel: $currentUserName")
    }

    // 📋 Lista de lugares cargados
    val listPlaces = mutableStateListOf<Place>()

    fun getPlacesByTrip(tripId: Int) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.getPlacesByTrip(tripId)
                if (res.isSuccessful) {
                    val lugares = res.body().orEmpty()
                    listPlaces.clear()
                    listPlaces.addAll(lugares)
                    println("Lugares cargados: ${lugares.size}")
                } else {
                    println("Error getPlacesByTrip: ${res.code()} ${res.message()}")
                }
            } catch (e: Exception) {
                println("Excepción getPlacesByTrip: ${e.message}")
            }
        }
    }

    // 🔹 Obtener un lugar por ID
    fun getPlaceById(id: Int, onResult: (Place?) -> Unit) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.getPlaceById(id)
                if (res.isSuccessful) onResult(res.body())
                else onResult(null)
            } catch (e: Exception) {
                println("Error getPlaceById: ${e.message}")
                onResult(null)
            }
        }
    }

    // 🔹 Crear lugar básico
    fun createPlaceBasic(
        nombre: String,
        ciudad: String,
        tripId: Int,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val payload = Place(
                    id = null,
                    nombre = nombre,
                    ciudad = ciudad,
                    descripcion = null,
                    imagenUrl = null,
                    indicaciones = null,
                    tiempoEstimado = null,
                    precio = null,
                    idViaje = tripId
                )
                val res = RetrofitInstance.api.createPlace(payload)
                if (res.isSuccessful) {
                    res.body()?.let {
                        listPlaces.add(0, it)
                        println("Lugar básico creado: ${it.nombre}")
                        onResult(true)
                        return@launch
                    }
                }
                println("Error createPlaceBasic: ${res.code()} ${res.message()}")
                onResult(false)
            } catch (e: Exception) {
                println("Excepción createPlaceBasic: ${e.message}")
                onResult(false)
            }
        }
    }

    // 🔹 Crear lugar completo
    fun createPlace(
        nombre: String,
        ciudad: String,
        descripcion: String? = null,
        imagenUrl: String? = null,
        indicaciones: String? = null,
        tiempo: String? = null,
        precio: String? = null,
        tripId: Int,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val payload = Place(
                    id = null,
                    nombre = nombre,
                    ciudad = ciudad,
                    descripcion = descripcion,
                    imagenUrl = imagenUrl,
                    indicaciones = indicaciones,
                    tiempoEstimado = tiempo,
                    precio = precio,
                    idViaje = tripId
                )
                val res = RetrofitInstance.api.createPlace(payload)
                if (res.isSuccessful) {
                    res.body()?.let {
                        listPlaces.add(0, it)
                        println("Lugar creado: ${it.nombre}")
                        onResult(true)
                        return@launch
                    }
                }
                println("Error createPlace: ${res.code()} ${res.message()}")
                onResult(false)
            } catch (e: Exception) {
                println("Excepción createPlace: ${e.message}")
                onResult(false)
            }
        }
    }

    // 🔹 Actualizar lugar
    fun updatePlace(
        id: Int,
        tripId: Int,
        nombre: String,
        ciudad: String,
        descripcion: String? = null,
        imagenUrl: String? = null,
        indicaciones: String? = null,
        tiempo: String? = null,
        precio: String? = null,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val payload = Place(
                    id = id,
                    nombre = nombre,
                    ciudad = ciudad,
                    descripcion = descripcion,
                    imagenUrl = imagenUrl,
                    indicaciones = indicaciones,
                    tiempoEstimado = tiempo,
                    precio = precio,
                    idViaje = tripId
                )
                val res = RetrofitInstance.api.updatePlace(id, payload)
                if (res.isSuccessful) {
                    res.body()?.let { updated ->
                        val idx = listPlaces.indexOfFirst { it.id == id }
                        if (idx >= 0) listPlaces[idx] = updated
                        println("Lugar actualizado: ${updated.nombre}")
                        onResult(true)
                        return@launch
                    }
                }
                println("Error updatePlace: ${res.code()} ${res.message()}")
                onResult(false)
            } catch (e: Exception) {
                println("Excepción updatePlace: ${e.message}")
                onResult(false)
            }
        }
    }

    // 🔹 Eliminar lugar
    fun deletePlace(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.deletePlace(id)
                if (res.isSuccessful) {
                    listPlaces.removeIf { it.id == id }
                    println("🗑 Lugar eliminado (ID: $id)")
                    onResult(true)
                } else {
                    println("Error deletePlace: ${res.code()} ${res.message()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                println("Excepción deletePlace: ${e.message}")
                onResult(false)
            }
        }
    }

    // 🔹 Obtener lugar desde la cache local
    fun getPlaceFromCache(id: Int): Place? =
        listPlaces.firstOrNull { it.id == id }
}
