package com.example.organizadordeviajes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.organizadordeviajes.ui.screens.MainScreen
import com.example.organizadordeviajes.ui.theme.OrganizadorDeViajesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrganizadorDeViajesTheme {
                MainScreen()
            }
        }
    }
}
