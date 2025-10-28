package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.organizadordeviajes.ui.navigation.NavScreens
import com.example.organizadordeviajes.viewmodels.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTripsScreen(
    navController: NavController = rememberNavController(),
    viewModel: TripViewModel = viewModel(),
    username: String = ""
) {
    val viajes = viewModel.listTrip

    LaunchedEffect(Unit) {
        viewModel.getTrips()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Bienvenido ${if (username.isNotEmpty()) username else ""}")
                }
            )
        },
        bottomBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        navController.navigate("${NavScreens.MY_TRAVELS.name}/$username")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Mis Viajes")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        navController.navigate("${NavScreens.FORM_TRIP.name}/$username")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Crear Viaje")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(viajes) { viaje ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        // TODO: navegar a la pantalla de lugares de este viaje
                        // navController.navigate("places/${viaje.id}")
                    }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Nombre: ${viaje.nombre ?: "Sin nombre"}")
                        Text(text = "Usuario: ${viaje.usuario ?: "Desconocido"}")
                        Text(text = "País: ${viaje.pais ?: "Sin país"}")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListTripsScreenPreview() {
    ListTripsScreen(username = "pepito")
}
