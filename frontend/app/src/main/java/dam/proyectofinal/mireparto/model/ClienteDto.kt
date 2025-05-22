package dam.proyectofinal.mireparto.model

data class ClienteDto(
    val id: Long,
    val nombre: String,
    val email: String,
    val telefono: String? = null
)