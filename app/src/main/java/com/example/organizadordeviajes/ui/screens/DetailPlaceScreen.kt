package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.organizadordeviajes.data.model.Place
import com.example.organizadordeviajes.ui.navigation.NavScreens
import com.example.organizadordeviajes.ui.theme.OrganizadorDeViajesTheme
import com.example.organizadordeviajes.viewmodels.PlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPlaceScreen(
    navController: NavController,
    placeId: String,
    vm: PlaceViewModel
) {
    val lugar = vm.listPlaces.find { it.id == placeId.toInt() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del lugar") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        if (lugar == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Lugar no encontrado")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Imagen principal
                AsyncImage(
                    model = lugar.imagenUrl,
                    contentDescription = lugar.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                // Información textual
                Text(
                    text = lugar.nombre ?: "Sin nombre",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = lugar.ciudad ?: "Ciudad no especificada",
                    style = MaterialTheme.typography.titleMedium
                )

                if (!lugar.descripcion.isNullOrBlank()) {
                    Text(
                        text = "Descripción: ${lugar.descripcion}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (!lugar.indicaciones.isNullOrBlank()) {
                    Text(
                        text = "Indicaciones: ${lugar.indicaciones}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (!lugar.tiempoEstimado.isNullOrBlank()) {
                    Text(
                        text = "Tiempo estimado: ${lugar.tiempoEstimado}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (!lugar.precio.isNullOrBlank()) {
                    Text(
                        text = "Precio: ${lugar.precio}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }

                    Button(
                        onClick = {
                            navController.navigate("${NavScreens.EDIT_PLACE.name}/${lugar.id}")
                        }
                    ) {
                        Text("Editar lugar")
                    }

                    Button(
                        onClick = {
                            vm.deletePlace(lugar.id ?: return@Button) { success ->
                                if (success) {
                                    println("Lugar eliminado correctamente")
                                    navController.navigate("${NavScreens.PLACES.name}/${lugar.idViaje}")
                                } else {
                                    println("Error al eliminar lugar")
                                }
                            }
                        }
                    ) {
                        Text("Eliminar lugar")
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPlaceScreenPreview() {
    OrganizadorDeViajesTheme {
        val navController = rememberNavController()
        val fakeViewModel = androidx.lifecycle.viewmodel.compose.viewModel<PlaceViewModel>()

        // Ejemplo temporal para vista previa
        fakeViewModel.listPlaces.add(
            com.example.organizadordeviajes.data.model.Place(
                id = 1,
                nombre = "Disney World",
                ciudad = "Orlando",
                descripcion = "El parque más famoso del mundo.",
                imagenUrl = "https://i.imgur.com/nY5VqCm.jpeg",
                indicaciones = "Tomar bus desde el hotel.",
                tiempoEstimado = "8 horas",
                precio = "120$ por persona",
                idViaje = 2
            )
        )

        DetailPlaceScreen(
            navController = navController,
            placeId = "1",
            vm = fakeViewModel
        )
    }
}
