package com.example.organizadordeviajes

import MyTravelsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.organizadordeviajes.ui.navigation.NavScreens
import com.example.organizadordeviajes.ui.screens.*
import com.example.organizadordeviajes.ui.theme.OrganizadorDeViajesTheme
import com.example.organizadordeviajes.viewmodels.PlaceViewModel
import com.example.organizadordeviajes.viewmodels.TripViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrganizadorDeViajesTheme {
                val navController = rememberNavController()
                val placeViewModel: PlaceViewModel = viewModel()
                val tripViewModel : TripViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = NavScreens.SPLASH.name
                ) {
                    composable(NavScreens.SPLASH.name) {
                        SplashScreen(navController, placeViewModel)
                    }

                    composable("${NavScreens.LIST_TRIPS.name}/{username}") { backStack ->
                        val username = backStack.arguments?.getString("username") ?: ""
                        // Guardar nombre en el ViewModel
                        placeViewModel.setCurrentUserName(username)
                        ListTripsScreen(navController, username = username)
                    }

                    composable("${NavScreens.MY_TRAVELS.name}/{username}") { backStack ->
                        val username = backStack.arguments?.getString("username") ?: ""
                        MyTravelsScreen(navController, username = username)
                    }

                    composable("${NavScreens.FORM_TRIP.name}/{username}") { backStack ->
                        val username = backStack.arguments?.getString("username") ?: ""
                        FormTripScreen(navController, username = username)
                    }

                    composable("${NavScreens.DETAIL_TRIP.name}/{tripId}/{username}") { backStack ->
                        val tripId = backStack.arguments?.getString("tripId") ?: ""
                        val username = backStack.arguments?.getString("username") ?: ""
                        DetailTripScreen(
                            navController = navController,
                            tripId = tripId,
                            username = username
                        )
                    }

                    composable("${NavScreens.EDIT_TRIP.name}/{tripId}/{username}") { backStack ->
                        val tripId = backStack.arguments?.getString("tripId") ?: ""
                        val username = backStack.arguments?.getString("username") ?: ""
                        EditTripScreen(
                            navController = navController,
                            tripId = tripId,
                            currentUsername = username,
                            vm = tripViewModel
                        )
                    }

                    // Lugares por viaje
                    composable("${NavScreens.PLACES.name}/{tripId}") { backStack ->
                        val tripId = backStack.arguments?.getString("tripId") ?: "0"
                        PlaceScreen(
                            navController = navController,
                            tripId = tripId,
                            vm = placeViewModel
                        )
                    }

                    composable("${NavScreens.DETAIL_PLACE.name}/{username}/{placeId}") { backStack ->
                        val username = backStack.arguments?.getString("username") ?: ""
                        val placeId = backStack.arguments?.getString("placeId") ?: ""
                        placeViewModel.setCurrentUserName(username)
                        DetailPlaceScreen(
                            navController = navController,
                            placeId = placeId,
                            currentUsername = username,
                            vm = placeViewModel
                        )
                    }

                    composable("${NavScreens.FORM_PLACE.name}/{tripId}") { backStack ->
                        val tripId = backStack.arguments?.getString("tripId") ?: ""
                        FormPlaceScreen(
                            navController = navController,
                            tripId = tripId,
                            vm = placeViewModel
                        )
                    }

                    composable("${NavScreens.EDIT_PLACE.name}/{placeId}") { backStack ->
                        val placeId = backStack.arguments?.getString("placeId") ?: ""
                        EditPlaceScreen(
                            navController = navController,
                            placeId = placeId,
                            vm = placeViewModel
                        )
                    }
                }
            }
        }
    }
}
