package dam.proyectofinal.mireparto.model

data class EntregaDto(
    val id: Long? = null,
    val direccion: String,
    val fechaCreacion: String,
    val fechaPrevista: String,
    val fechaEfectiva: String? = null,
    val estado: String,
    val pesoKg: Double,
    val descripcionPaquete: String? = null,
    val clienteId: Long,
    val vehiculoId: Long,
    val zonaId: Long
)
