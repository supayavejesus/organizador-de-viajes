package com.example.organizadorviajes.data.remote

import com.example.organizadordeviajes.data.model.Place
import com.example.organizadordeviajes.data.model.Trip
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @GET("trips")
    suspend fun getTrips(): Response<List<Trip>>

    @GET("trips/{username}")
    suspend fun getTripsByUsername(@Path("username") username: String): Response<List<Trip>>

    @POST("trips")
    suspend fun createTrip(@Body trip: Trip): Response<Trip>

    @PUT("trips/{id}")
    suspend fun updateTrip(@Path("id") id: Int, @Body trip: Trip): Response<Trip>

    @DELETE("trips/{id}")
    suspend fun deleteTrip(@Path("id") id: Int): Response<Unit>


    @GET("trips/{trip_id}/places")
    suspend fun getPlacesByTrip(@Path("trip_id") tripId: Int): Response<List<Place>>



    @GET("places")
    suspend fun getAllPlaces(): Response<List<Place>>

    @GET("places/{id}")
    suspend fun getPlaceById(@Path("id") id: Int): Response<Place>


    @POST("places")
    suspend fun createPlace(@Body place: Place): Response<Place>

    @PUT("places/{id}")
    suspend fun updatePlace(@Path("id") id: Int, @Body place: Place): Response<Place>

    @DELETE("places/{id}")
    suspend fun deletePlace(@Path("id") id: Int): Response<Unit>
}
