
        import androidx.compose.foundation.layout.Arrangement
        import androidx.compose.foundation.layout.Box
        import androidx.compose.foundation.layout.Column
        import androidx.compose.foundation.layout.Row
        import androidx.compose.foundation.layout.fillMaxSize
        import androidx.compose.foundation.layout.fillMaxWidth
        import androidx.compose.foundation.layout.padding
        import androidx.compose.foundation.lazy.LazyColumn
        import androidx.compose.foundation.lazy.items
        import androidx.compose.material3.Button
        import androidx.compose.material3.Card
        import androidx.compose.material3.ExperimentalMaterial3Api
        import androidx.compose.material3.Scaffold
        import androidx.compose.material3.Text
        import androidx.compose.material3.TopAppBar
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.LaunchedEffect
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.tooling.preview.Preview
        import androidx.compose.ui.unit.dp
        import androidx.lifecycle.viewmodel.compose.viewModel
        import androidx.navigation.NavController
        import com.example.organizadordeviajes.ui.navigation.NavScreens
        import com.example.organizadordeviajes.viewmodels.TripViewModel

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun MyTravelsScreen(
            navController: NavController,
            username: String,
            vm: TripViewModel = viewModel()
        ) {
            val trips = vm.listTrip

            LaunchedEffect(username) {
                if (username.isNotBlank()) {
                    // Normalizamos el nombre a minúsculas
                    val normalizedUsername = username.trim().lowercase()
                    vm.getTripsByUsername(normalizedUsername)
                } else {
                    vm.getTrips() // fallback: carga todos si no se pasó username
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Mis Viajes de $username") }
                    )
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                navController.navigate("${NavScreens.FORM_TRIP.name}/$username")
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Crear viaje")
                        }

                        Button(
                            onClick = {
                                navController.navigate("${NavScreens.LIST_TRIPS.name}/$username")
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Volver")
                        }
                    }
                }
            ) { padding ->
                if (trips.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No tienes viajes registrados.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        items(trips) { trip ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                onClick = {
                                    navController.navigate("${NavScreens.PLACES.name}/${trip.id}")
                                }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "Nombre: ${trip.nombre ?: "Sin nombre"}")
                                    Text(text = "País: ${trip.pais ?: "Sin país"}")
                                }
                            }
                        }
                    }
                }
            }
        }



        @Preview(showBackground = true)
        @Composable
        fun MyTravelsScreenPreview() {
            val context = androidx.compose.ui.platform.LocalContext.current
            val navController = androidx.compose.runtime.remember { androidx.navigation.NavController(context) }

            MyTravelsScreen(
                navController = navController,
                username = "UsuarioEjemplo"
            )
        }