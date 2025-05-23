package dam.proyectofinal.mireparto.viewmodel

import dam.proyectofinal.mireparto.model.LoginRequestDto
import dam.proyectofinal.mireparto.model.RegisterRequestDto
import dam.proyectofinal.mireparto.model.AuthResponseDto
import dam.proyectofinal.mireparto.network.RetrofitInstance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _authResponse = MutableStateFlow<AuthResponseDto?>(null)
    val authResponse: StateFlow<AuthResponseDto?> = _authResponse

    // Intenta autenticar al usuario con las credenciales proporcionadas
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = LoginRequestDto(email = email, password = password)
                val response = RetrofitInstance.api.login(request)
                _authResponse.value = response
                _errorMessage.value = null
            } catch (e: IOException) {
                _errorMessage.value = "Error de red: ${e.message}"
            } catch (e: HttpException) {
                when (e.code()) {
                    401, 403 -> _errorMessage.value = "Correo o contraseña incorrectos"
                    500      -> _errorMessage.value = "Error del servidor. Intenta más tarde."
                    else     -> _errorMessage.value = "Error desconocido (${e.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Registra un nuevo usuario con los datos proporcionados
    fun register(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = RegisterRequestDto(nombre = nombre, email = email, password = password)
                val response = RetrofitInstance.api.register(request)
                _authResponse.value = response
                _errorMessage.value = null
            } catch (e: IOException) {
                _errorMessage.value = "Error de red: ${e.message}"
            } catch (e: HttpException) {
                when (e.code()) {
                    409 -> _errorMessage.value = "El email ya está registrado"
                    400 -> _errorMessage.value = "Campos inválidos"
                    403, 401 -> _errorMessage.value = "No tienes permisos para registrarte"
                    else -> _errorMessage.value = "Error desconocido (${e.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearAuthState() {
        _authResponse.value = null
        _errorMessage.value = null
    }
}