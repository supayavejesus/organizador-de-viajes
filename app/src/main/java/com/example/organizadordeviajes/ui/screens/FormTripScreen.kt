package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.organizadordeviajes.ui.navigation.NavScreens
import com.example.organizadordeviajes.viewmodels.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTripScreen(
    navController: NavController,
    username: String,
    vm: TripViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var pais by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear nuevo viaje") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del viaje") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = pais,
                onValueChange = { pais = it },
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.text.isNotBlank() && pais.text.isNotBlank()) {
                        vm.createTrips(
                            nombre = nombre.text,
                            pais = pais.text,
                            username = username
                        ) { success ->
                            if (success) {
                                println("Viaje creado correctamente")
                                navController.navigate("${NavScreens.LIST_TRIPS.name}/$username")
                            } else {
                                println("Error al crear viaje")
                            }
                        }
                    } else {
                        println("Campos incompletos")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar viaje")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón: Cancelar
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
