package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.organizadordeviajes.data.RetrofitInstance
import com.example.organizadordeviajes.data.model.Trip
import com.example.organizadordeviajes.ui.navigation.NavScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTripScreen(
    navController: NavController,
    tripId: String,
    username: String
) {
    var trip by remember { mutableStateOf<Trip?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(tripId) {
        val id = tripId.toIntOrNull() ?: return@LaunchedEffect
        try {
            val response = RetrofitInstance.api.getTrips()
            if (response.isSuccessful) {
                trip = response.body()?.find { it.id == id }
            }
        } catch (e: Exception) {
            println("Error al cargar detalle: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del viaje") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            trip == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Viaje no encontrado")
                }
            }

            else -> {
                val t = trip!!
                val isOwner = (t.usuario?.trim()?.lowercase() ?: "") ==
                        username.trim().lowercase()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = t.nombre ?: "Sin nombre",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("País: ${t.pais ?: "Desconocido"}")
                    Text("Usuario: ${t.usuario ?: "Desconocido"}")

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(onClick = { navController.popBackStack() }) {
                            Text("Volver")
                        }

                        if (isOwner) {
                            val tid = t.id
                            if (tid != null) {
                                Button(onClick = {
                                    navController.navigate("${NavScreens.EDIT_TRIP.name}/$tid")
                                }) {
                                    Text("Editar viaje")
                                }

                                Button(onClick = {
                                    scope.launch {
                                        val res = RetrofitInstance.api.deleteTrip(tid)
                                        if (res.isSuccessful) {
                                            println("Viaje eliminado correctamente")
                                            navController.navigate("${NavScreens.LIST_TRIPS.name}/$username")
                                        }
                                    }
                                }) {
                                    Text("Eliminar viaje")
                                }
                            }
                        }
                    }

                    if (!isOwner) {
                        Text(
                            text = "Solo el propietario puede editar o eliminar este viaje.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
