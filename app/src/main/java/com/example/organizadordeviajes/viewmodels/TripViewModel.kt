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
}
