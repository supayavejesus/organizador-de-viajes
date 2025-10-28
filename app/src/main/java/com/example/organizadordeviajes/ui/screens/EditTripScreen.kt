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
import com.example.organizadordeviajes.viewmodels.TripViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripScreen(
    navController: NavController,
    tripId: String,
    currentUsername: String,
    vm: TripViewModel
) {
    var trip by remember { mutableStateOf<Trip?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(tripId) {
        val id = tripId.toIntOrNull() ?: return@LaunchedEffect

        try {
            val response = RetrofitInstance.api.getTrips()
            if (response.isSuccessful) {
                trip = response.body()?.find { it.id == id }
            }
        } catch (e: Exception) {
            println("Error cargando detalle del viaje: ${e.message}")
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
                val currentTrip = trip!!
                val isOwner = currentTrip.usuario?.trim()?.lowercase() ==
                        currentUsername.trim().lowercase()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = currentTrip.nombre ?: "Sin nombre",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "País: ${trip!!.pais}")
                    Text(text = "Usuario: ${trip!!.usuario}")

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(onClick = { navController.popBackStack() }) {
                            Text("Volver")
                        }

                        if (isOwner) {
                            Button(
                                onClick = {
                                    navController.navigate(
                                        "${NavScreens.EDIT_TRIP.name}/${trip!!.id}"
                                    )
                                }
                            ) {
                                Text("Editar viaje")
                            }

                            Button(
                                onClick = {
                                    vm.deleteTrip(trip!!.id ?: return@Button) { success ->
                                        if (success) {
                                            println("Viaje eliminado correctamente")
                                            navController.navigate(
                                                "${NavScreens.LIST_TRIPS.name}/${currentUsername}"
                                            )
                                        } else {
                                            println("Error al eliminar viaje")
                                        }
                                    }
                                }
                            ) {
                                Text("Eliminar viaje")
                            }
                        }
                    }

                    if (!isOwner) {
                        Text(
                            text = "Solo el propietario del viaje puede editar o eliminarlo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}


