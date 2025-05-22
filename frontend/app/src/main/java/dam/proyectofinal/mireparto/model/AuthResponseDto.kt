package dam.proyectofinal.mireparto.model

data class AuthResponseDto(
    val token: String,
    val userId: Long,
    val nombre: String
)