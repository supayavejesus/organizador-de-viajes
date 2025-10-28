package com.example.organizadordeviajes.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizadordeviajes.data.RetrofitInstance
import com.example.organizadordeviajes.data.model.Trip
import kotlinx.coroutines.launch

class TripViewModel : ViewModel() {

    val listTrip = mutableStateListOf<Trip>()

    fun getTrips() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTrips()
                if (response.isSuccessful) {
                    response.body()?.let {
                        listTrip.clear()
                        listTrip.addAll(it)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Error al conectar con la API: ${e.message}")
            }
        }
    }

    fun getTripsByUsername(username: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.getTripsByUsername(username)
                if (res.isSuccessful) {
                    listTrip.clear()
                    listTrip.addAll(res.body() ?: emptyList())
                    println("Viajes cargados para $username")
                } else {
                    println("Error al obtener viajes: ${res.code()} ${res.message()}")
                }
            } catch (e: Exception) {
                println("Excepción getTripsByUsername: ${e.message}")
            }
        }
    }


    fun createTrips(
        nombre: String,
        pais: String,
        username: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val newTrip = Trip(
                    id = null,
                    nombre = nombre,
                    pais = pais,
                    usuario = username,
                )
                val response = RetrofitInstance.api.createTrip(newTrip)
                if (response.isSuccessful) {
                    response.body()?.let {
                        listTrip.add(it)
                        onResult(true)
                    }
                } else {
                    println("Error al crear viaje: ${response.code()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                println("Error al crear viaje: ${e.message}")
                onResult(false)
            }
        }
    }

    fun updateTrip(
        id: Int,
        nombre: String,
        pais: String,
        username: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updatedTrip = Trip(id, nombre, username, pais)
                val response = RetrofitInstance.api.updateTrip(id, updatedTrip)
                if (response.isSuccessful) {
                    response.body()?.let { tripUpdated ->
                        val index = listTrip.indexOfFirst { it.id == id }
                        if (index >= 0) {
                            listTrip[index] = tripUpdated
                        }
                    }
                    onResult(true)
                } else {
                    println("Error al actualizar viaje: ${response.code()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                println("Error al actualizar viaje: ${e.message}")
                onResult(false)
            }
        }
    }

    fun deleteTrip(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteTrip(id)
                if (response.isSuccessful) {
                    listTrip.removeIf { it.id == id }
                    onResult(true)
                } else {
                    println("Error al eliminar viaje: ${response.code()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                println("Error al eliminar viaje: ${e.message}")
                onResult(false)
            }
        }
    }
    fun getTripFromCache(id: Int): Trip? =
    listTrip.firstOrNull { it.id == id }
}
