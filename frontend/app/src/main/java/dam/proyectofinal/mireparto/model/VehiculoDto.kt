package dam.proyectofinal.mireparto.model

data class VehiculoDto(
    val id: Long,
    val matricula: String,
    val tipo: String,
    val capacidad: Double
)