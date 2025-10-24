package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.organizadordeviajes.viewmodels.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TripViewModel = viewModel()) {
    val viajes = viewModel.listTrip

    LaunchedEffect(Unit) {
        viewModel.getTrips()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title =
                    {
                        Text("Prueba de API - Viajes")
                    }
            )
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
                        .padding(8.dp)
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
fun MainScreenPreview() {
    MainScreen()
}
