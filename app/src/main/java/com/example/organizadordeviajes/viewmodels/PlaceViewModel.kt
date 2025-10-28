package com.example.organizadordeviajes.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizadordeviajes.data.RetrofitInstance
import com.example.organizadordeviajes.data.model.Place
import kotlinx.coroutines.launch

class PlaceViewModel : ViewModel() {

    val listPlaces = mutableStateListOf<Place>()

    /* =========================
       🔹 OBTENER LUGARES
    ========================= */
    fun getAllPlaces() {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.getPlacesByTrip(0)
                if (res.isSuccessful) {
                    res.body()?.let {
                        listPlaces.clear()
                        listPlaces.addAll(it)
                    }
                } else {
                    println("Error getAllPlaces: ${res.code()}")
                }
            } catch (e: Exception) {
                println("Excepción getAllPlaces: ${e.message}")
            }
        }
    }

    fun getPlacesByTrip(tripId: Int) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.getPlacesByTrip(tripId)
                if (res.isSuccessful) {
                    res.body()?.let {
                        listPlaces.clear()
                        listPlaces.addAll(it)
                    }
                } else {
                    println("Error getPlacesByTrip: ${res.code()}")
                }
            } catch (e: Exception) {
                println("Excepción getPlacesByTrip: ${e.message}")
            }
        }
    }

    /* =========================
       🔹 CREAR LUGAR (mínimo o completo)
    ========================= */
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
                        onResult(true)
                        return@launch
                    }
                }
                println("Error createPlaceBasic: ${res.code()}")
                onResult(false)
            } catch (e: Exception) {
                println("Excepción createPlaceBasic: ${e.message}")
                onResult(false)
            }
        }
    }

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
                        onResult(true)
                        return@launch
                    }
                }
                println("Error createPlace: ${res.code()}")
                onResult(false)
            } catch (e: Exception) {
                println("Excepción createPlace: ${e.message}")
                onResult(false)
            }
        }
    }

    /* =========================
       🔹 ACTUALIZAR LUGAR
    ========================= */
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
                        onResult(true)
                        return@launch
                    }
                }
                println("Error updatePlace: ${res.code()}")
                onResult(false)
            } catch (e: Exception) {
                println("Excepción updatePlace: ${e.message}")
                onResult(false)
            }
        }
    }

    /* =========================
       🔹 ELIMINAR LUGAR
    ========================= */
    fun deletePlace(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.deletePlace(id)
                if (res.isSuccessful) {
                    listPlaces.removeIf { it.id == id }
                    onResult(true)
                } else {
                    println("Error deletePlace: ${res.code()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                println("Excepción deletePlace: ${e.message}")
                onResult(false)
            }
        }
    }

    /* =========================
       🧩 OBTENER UN LUGAR DEL CACHE
       (para editar sin re-llamar API)
    ========================= */
    fun getPlaceFromCache(id: Int): Place? {
        return listPlaces.firstOrNull { it.id == id }
    }
}
