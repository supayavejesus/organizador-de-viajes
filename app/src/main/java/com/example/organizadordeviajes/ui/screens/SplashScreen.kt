package com.example.organizadordeviajes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun SplashScreen(navController: NavController, placeViewModel: PlaceViewModel) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido al Organizador de Viajes",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    showError = false
                },
                label = { Text("Nombre de usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            if (showError) {
                Text(
                    text = "Por favor ingresa un nombre válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val user = username.text.trim().lowercase()
                    if (user.isNotBlank()) {
                        // 🔹 Guardar usuario globalmente
                        placeViewModel.setCurrentUserName(user)

                        // 🔹 Navegar con ese usuario
                        navController.navigate("${NavScreens.LIST_TRIPS.name}/$user")
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Ingresar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    OrganizadorDeViajesTheme {
        val navController = rememberNavController()
        val fakeVm = PlaceViewModel()
        SplashScreen(navController = navController, placeViewModel = fakeVm)
    }
}
