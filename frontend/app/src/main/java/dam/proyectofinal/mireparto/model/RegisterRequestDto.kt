package dam.proyectofinal.mireparto.model

data class RegisterRequestDto(
    val nombre: String,
    val email: String,
    val password: String
)