package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.organizadordeviajes.ui.navigation.NavScreens
import com.example.organizadordeviajes.viewmodels.PlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPlaceScreen(
    navController: NavController,
    tripId: String,
    vm: PlaceViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var ciudad by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }
    var imagenUrl by remember { mutableStateOf(TextFieldValue("")) }
    var indicaciones by remember { mutableStateOf(TextFieldValue("")) }
    var tiempo by remember { mutableStateOf(TextFieldValue("")) }
    var precio by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar lugar") }) }
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
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL Imagen (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = indicaciones,
                onValueChange = { indicaciones = it },
                label = { Text("Indicaciones (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tiempo,
                onValueChange = { tiempo = it },
                label = { Text("Tiempo estimado)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val idTrip = tripId.toIntOrNull()
                    if (idTrip == null) {
                        println("tripId inválido")
                        return@Button
                    }
                    if (nombre.text.isNotBlank() && ciudad.text.isNotBlank()) {
                        val soloObligatorios = listOf(
                            descripcion.text, imagenUrl.text,
                            indicaciones.text, tiempo.text, precio.text
                        ).all { it.isBlank() }

                        if (soloObligatorios) {
                            vm.createPlaceBasic(nombre.text, ciudad.text, idTrip) { ok ->
                                if (ok) navController.navigate("${NavScreens.PLACES.name}/$tripId")
                            }
                        } else {
                            vm.createPlace(
                                nombre = nombre.text,
                                ciudad = ciudad.text,
                                descripcion = descripcion.text.ifBlank { null },
                                imagenUrl = imagenUrl.text.ifBlank { null },
                                indicaciones = indicaciones.text.ifBlank { null },
                                tiempo = tiempo.text.ifBlank { null },
                                precio = precio.text.ifBlank { null },
                                tripId = idTrip
                            ) { ok ->
                                if (ok) navController.navigate("${NavScreens.PLACES.name}/$tripId")
                            }
                        }
                    } else println("Campos obligatorios vacíos")
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar lugar") }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cancelar") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormPlaceScreenPreview() {

}