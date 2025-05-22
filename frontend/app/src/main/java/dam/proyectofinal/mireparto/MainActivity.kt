package dam.proyectofinal.mireparto

import dam.proyectofinal.mireparto.ui.theme.MiRepartoTheme
import dam.proyectofinal.mireparto.viewmodel.EntregaViewModel
import dam.proyectofinal.mireparto.ui.navigation.Navigation
import dam.proyectofinal.mireparto.viewmodel.AuthViewModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiRepartoTheme {
                val authViewModel: AuthViewModel = viewModel()
                val entregaViewModel: EntregaViewModel = viewModel()
                Navigation(
                    authViewModel    = authViewModel,
                    entregaViewModel = entregaViewModel
                )
            }
        }
    }
}
