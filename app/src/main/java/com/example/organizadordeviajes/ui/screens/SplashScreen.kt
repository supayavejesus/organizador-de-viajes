    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.height
    import androidx.compose.material3.Button
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import androidx.navigation.NavController
    import androidx.navigation.compose.rememberNavController
    import com.example.organizadordeviajes.ui.navigation.NavScreens
    import com.example.organizadordeviajes.ui.theme.OrganizadorDeViajesTheme
    import androidx.compose.ui.tooling.preview.Preview

    @Composable
    fun SplashScreen(navController: NavController) {
        var username by remember { mutableStateOf("") }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Bienvenido al Organizador de Viajes", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de usuario") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (username.isNotBlank()) {
                            navController.navigate("${NavScreens.LIST_TRIPS.name}/${username}")
                        }
                    }
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
            SplashScreen(navController = navController)
        }
    }