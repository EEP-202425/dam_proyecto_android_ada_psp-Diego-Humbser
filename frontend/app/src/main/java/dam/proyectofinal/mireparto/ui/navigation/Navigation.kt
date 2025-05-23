package dam.proyectofinal.mireparto.ui.navigation

import dam.proyectofinal.mireparto.ui.auth.LoginScreen
import dam.proyectofinal.mireparto.ui.auth.RegisterScreen
import dam.proyectofinal.mireparto.ui.entregas.CrearEntregaScreen
import dam.proyectofinal.mireparto.ui.entregas.EntregaDetailScreen
import dam.proyectofinal.mireparto.ui.entregas.EditarEntregaScreen
import dam.proyectofinal.mireparto.ui.entregas.EntregasScreen
import dam.proyectofinal.mireparto.viewmodel.AuthViewModel
import dam.proyectofinal.mireparto.viewmodel.EntregaViewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType

sealed class Screen(val route: String) {
    data object LoginScreen    : Screen("login")
    data object RegisterScreen : Screen("register")
    data object ListScreen   : Screen("entregas")
    data object CreateScreen : Screen("crear_entrega")
    data object DetailScreen : Screen("entrega/{entregaId}")
    data object EditScreen   : Screen("entrega_editar/{entregaId}")
}

@Composable
fun Navigation(authViewModel: AuthViewModel, entregaViewModel: EntregaViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        // Login
        composable(Screen.LoginScreen.route) {
            LaunchedEffect(Unit) { authViewModel.clearAuthState() }
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.ListScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            )
        }
        // Register
        composable(Screen.RegisterScreen.route) {
            LaunchedEffect(Unit) { authViewModel.clearAuthState() }
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.ListScreen.route) {
                        popUpTo(Screen.RegisterScreen.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        // Listado de entregas
        composable(Screen.ListScreen.route) {
            EntregasScreen(
                viewModel = entregaViewModel,
                onNavigateToCreate = { navController.navigate(Screen.CreateScreen.route) },
                onItemClick = { id -> navController.navigate("entrega/$id") },
                onDelete = { id -> entregaViewModel.deleteEntrega(id) }
            )
        }
        // Creación de entrega
        composable(Screen.CreateScreen.route) {
            LaunchedEffect(Unit) { entregaViewModel.clearError() }
            CrearEntregaScreen(
                viewModel = entregaViewModel,
                onBack = {
                    entregaViewModel.loadEntregas()
                    navController.popBackStack()
                }
            )
        }
        // Detalle de entrega
        composable(
            Screen.DetailScreen.route,
            listOf(navArgument("entregaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val entregaId = backStackEntry.arguments?.getLong("entregaId") ?: 0L
            LaunchedEffect(Unit) { entregaViewModel.clearError() }
            EntregaDetailScreen(
                viewModel = entregaViewModel,
                entregaId = entregaId,
                onBack = { navController.popBackStack() },
                onEdit = { id -> navController.navigate("entrega_editar/$id") },
                onDelete = { id ->
                    entregaViewModel.deleteEntrega(id)
                    navController.popBackStack()
                }
            )
        }
        // Edición de entrega
        composable(
            Screen.EditScreen.route,
            listOf(navArgument("entregaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val entregaId = backStackEntry.arguments?.getLong("entregaId") ?: 0L
            EditarEntregaScreen(
                viewModel = entregaViewModel,
                entregaId = entregaId,
                onBack = {
                    entregaViewModel.loadEntregas()
                    navController.popBackStack()
                }
            )
        }
    }
}