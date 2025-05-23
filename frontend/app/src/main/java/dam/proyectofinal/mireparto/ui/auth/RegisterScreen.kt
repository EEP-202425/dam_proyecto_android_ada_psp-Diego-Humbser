package dam.proyectofinal.mireparto.ui.auth

import dam.proyectofinal.mireparto.viewmodel.AuthViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val authResponse by viewModel.authResponse.collectAsState()

    LaunchedEffect(authResponse) {
        if (authResponse != null) {
            onRegisterSuccess()
        }
    }

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var attemptedSubmit by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val nombreError = when {
        nombre.isBlank() -> "El nombre es obligatorio"
        nombre.length < 3 -> "Mínimo 3 caracteres"
        else -> null
    }
    val emailError = when {
        email.isBlank() -> "El email es obligatorio"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato de email inválido"
        else -> null
    }
    val passwordError = when {
        password.isBlank() -> "La contraseña es obligatoria"
        password.length < 6 -> "Mínimo 6 caracteres"
        else -> null
    }
    val isFormValid = nombreError == null && emailError == null && passwordError == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registrarse", fontSize = 24.sp, modifier = Modifier.padding(bottom = 24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            isError = attemptedSubmit && nombreError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (attemptedSubmit && nombreError != null) {
            Text(nombreError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            isError = attemptedSubmit && emailError != null,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (attemptedSubmit && emailError != null) {
            Text(emailError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            isError = attemptedSubmit && passwordError != null,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (attemptedSubmit && passwordError != null) {
            Text(passwordError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                attemptedSubmit = true
                if (isFormValid && !isLoading) {
                    viewModel.register(nombre.trim(), email.trim(), password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(text = if (isLoading) "Registrando..." else "Crear Cuenta")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text(text = "¿Ya tienes cuenta? Inicia sesión")
        }
    }
}