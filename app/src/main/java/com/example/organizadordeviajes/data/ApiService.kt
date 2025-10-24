package com.example.organizadorviajes.data.remote

import com.example.organizadordeviajes.data.model.Place
import com.example.organizadordeviajes.data.model.Trip

import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @GET("trips")
    suspend fun getTrips(): Response<List<Trip>>
    // Obtiene todos los viajes

    @GET("trips/by-username")
    suspend fun getTripsByUsername(@Query("username") username: String): Response<List<Trip>>
    // Obtiene viajes filtrados por usuario

    @POST("trips")
    suspend fun createTrip(@Body trip: Trip): Response<Trip>
    // Crea un nuevo viaje

    @PUT("trips/{id}")
    suspend fun updateTrip(@Path("id") id: Int, @Body trip: Trip): Response<Trip>
    // Actualiza un viaje existente

    @DELETE("trips/{id}")
    suspend fun deleteTrip(@Path("id") id: Int): Response<Unit>
    // Elimina un viaje por su ID



    @GET("places/by-trip/{trip_id}")
    suspend fun getPlacesByTrip(@Path("trip_id") tripId: Int): Response<List<Place>>
    // Obtiene todos los lugares de un viaje

    @POST("places")
    suspend fun createPlace(@Body place: Place): Response<Place>
    // Crea un nuevo lugar

    @PUT("places/{id}")
    suspend fun updatePlace(@Path("id") id: Int, @Body place: Place): Response<Place>
    // Actualiza un lugar existente

    @DELETE("places/{id}")
    suspend fun deletePlace(@Path("id") id: Int): Response<Unit>
    // Elimina un lugar
}
