package com.example.organizadordeviajes.data.model

import com.google.gson.annotations.SerializedName

data class Trip (
    val id: Int?,

    @SerializedName("name")
    val nombre: String?,

    @SerializedName("username")
    val usuario: String?,

    @SerializedName("country")
    val pais: String?
)