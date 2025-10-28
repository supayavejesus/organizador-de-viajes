package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.organizadordeviajes.data.model.Place
import com.example.organizadordeviajes.ui.navigation.NavScreens
import com.example.organizadordeviajes.ui.theme.OrganizadorDeViajesTheme
import com.example.organizadordeviajes.viewmodels.PlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceScreen(
    navController: NavController,
    tripId: String,
    vm: PlaceViewModel = viewModel()
) {
    val lugares = vm.listPlaces

    LaunchedEffect(Unit) {
        vm.getPlacesByTrip(tripId.toInt())
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lugares del viaje") })
        },
        bottomBar = {
            Button(
                onClick = {
                    navController.navigate("${NavScreens.FORM_PLACE.name}/$tripId")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Agregar Lugar")
            }
        }
    ) { padding ->
        if (lugares.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay lugares registrados en este viaje.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(lugares) { place ->
                    PlaceCard(
                        place = place,
                        onClick = {
                            navController.navigate("${NavScreens.DETAIL_PLACE.name}/${place.id}")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceCard(
    place: Place,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = place.imagenUrl,
                contentDescription = place.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = place.nombre ?: "Sin nombre", style = MaterialTheme.typography.titleMedium)
            Text(text = place.ciudad ?: "Sin ciudad", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceScreenPreview() {
    OrganizadorDeViajesTheme {
        val navController = rememberNavController()
        PlaceScreen(navController = navController, tripId = "1")
    }
}
