package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.organizadordeviajes.ui.navigation.NavScreens
import com.example.organizadordeviajes.viewmodels.PlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlaceScreen(
    navController: NavController,
    placeId: String,
    vm: PlaceViewModel
) {
    val idInt = placeId.toIntOrNull()
    val place = remember(idInt, vm.listPlaces.size) { idInt?.let { vm.getPlaceFromCache(it) } }

    // Si no existe en cache, avisa y vuelve
    if (idInt == null || place == null) {
        LaunchedEffect(Unit) {
            println("No se encontró el lugar en memoria")
            navController.popBackStack()
        }
        return
    }

    var nombre by remember { mutableStateOf(TextFieldValue(place.nombre.orEmpty())) }
    var ciudad by remember { mutableStateOf(TextFieldValue(place.ciudad.orEmpty())) }
    var descripcion by remember { mutableStateOf(TextFieldValue(place.descripcion.orEmpty())) }
    var imagenUrl by remember { mutableStateOf(TextFieldValue(place.imagenUrl.orEmpty())) }
    var indicaciones by remember { mutableStateOf(TextFieldValue(place.indicaciones.orEmpty())) }
    var tiempo by remember { mutableStateOf(TextFieldValue(place.tiempoEstimado.orEmpty())) }
    var precio by remember { mutableStateOf(TextFieldValue(place.precio.orEmpty())) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar lugar") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL de imagen") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = indicaciones,
                onValueChange = { indicaciones = it },
                label = { Text("Indicaciones") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tiempo,
                onValueChange = { tiempo = it },
                label = { Text("Tiempo estimado") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (nombre.text.isBlank() || ciudad.text.isBlank()) {
                        println("Nombre y ciudad son obligatorios")
                        return@Button
                    }
                    vm.updatePlace(
                        id = idInt,
                        tripId = place.idViaje ?: return@Button,
                        nombre = nombre.text,
                        ciudad = ciudad.text,
                        descripcion = descripcion.text.ifBlank { null },
                        imagenUrl = imagenUrl.text.ifBlank { null },
                        indicaciones = indicaciones.text.ifBlank { null },
                        tiempo = tiempo.text.ifBlank { null },
                        precio = precio.text.ifBlank { null }
                    ) { ok ->
                        if (ok) {
                            println("Lugar actualizado")
                            navController.navigate("${NavScreens.PLACES.name}/${place.idViaje}")
                        } else {
                            println("Error al actualizar lugar")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar cambios") }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cancelar") }
        }
    }
}
