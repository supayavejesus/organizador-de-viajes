package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.organizadordeviajes.ui.navigation.NavScreens
import com.example.organizadordeviajes.ui.theme.OrganizadorDeViajesTheme
import com.example.organizadordeviajes.viewmodels.PlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPlaceScreen(
    navController: NavController,
    tripId: String,
    vm: PlaceViewModel
) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var ciudad by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }
    var imagenUrl by remember { mutableStateOf(TextFieldValue("")) }
    var indicaciones by remember { mutableStateOf(TextFieldValue("")) }
    var tiempo by remember { mutableStateOf(TextFieldValue("")) }
    var precio by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agregar lugar") })
        }
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
                label = { Text("URL de imagen (opcional)") },
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
                label = { Text("Tiempo estimado (ej. 2h, 1 día)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (ej. 100$ por persona)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Guardar
            Button(
                onClick = {
                    if (nombre.text.isNotBlank() && ciudad.text.isNotBlank()) {

                        // Detectar si solo tiene los campos mínimos
                        val soloObligatorios =
                            descripcion.text.isBlank() &&
                                    imagenUrl.text.isBlank() &&
                                    indicaciones.text.isBlank() &&
                                    tiempo.text.isBlank() &&
                                    precio.text.isBlank()

                        if (soloObligatorios) {
                            // Crear con campos básicos
                            vm.createPlaceBasic(
                                nombre = nombre.text,
                                ciudad = ciudad.text,
                                tripId = tripId.toInt()
                            ) { success ->
                                if (success) {
                                    println("Lugar básico creado correctamente")
                                    navController.navigate("${NavScreens.PLACES.name}/$tripId")
                                } else {
                                    println("Error al crear lugar básico")
                                }
                            }
                        } else {
                            // Crear con todos los campos
                            vm.createPlace(
                                nombre = nombre.text,
                                ciudad = ciudad.text,
                                descripcion = descripcion.text.ifBlank { null },
                                imagenUrl = imagenUrl.text.ifBlank { null },
                                indicaciones = indicaciones.text.ifBlank { null },
                                tiempo = tiempo.text.ifBlank { null },
                                precio = precio.text.ifBlank { null },
                                tripId = tripId.toInt()
                            ) { success ->
                                if (success) {
                                    println("Lugar completo creado correctamente")
                                    navController.navigate("${NavScreens.PLACES.name}/$tripId")
                                } else {
                                    println("Error al crear lugar completo")
                                }
                            }
                        }
                    } else {
                        println("Campos obligatorios vacíos")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar lugar")
            }

            // Botón Cancelar
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormPlaceScreenPreview() {
    OrganizadorDeViajesTheme {
        val navController = rememberNavController()
        val fakeViewModel = androidx.lifecycle.viewmodel.compose.viewModel<PlaceViewModel>()
        FormPlaceScreen(
            navController = navController,
            tripId = "1",
            vm = fakeViewModel
        )
    }
}
