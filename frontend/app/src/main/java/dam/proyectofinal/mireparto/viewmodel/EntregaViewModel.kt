package dam.proyectofinal.mireparto.viewmodel

import dam.proyectofinal.mireparto.model.EntregaDto
import dam.proyectofinal.mireparto.model.ClienteDto
import dam.proyectofinal.mireparto.model.VehiculoDto
import dam.proyectofinal.mireparto.model.ZonaDto
import dam.proyectofinal.mireparto.network.RetrofitInstance

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import retrofit2.HttpException
import java.io.IOException

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EntregaViewModel : ViewModel() {

    private val _entregas = MutableStateFlow<List<EntregaDto>>(emptyList())
    val entregas: StateFlow<List<EntregaDto>> = _entregas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Estado de la entrega seleccionada (detalle/edición)
    private val _currentEntrega = MutableStateFlow<EntregaDto?>(null)
    val currentEntrega: StateFlow<EntregaDto?> = _currentEntrega

    // Estado seleccionado para edición (estadoEntrega)
    private val _estadoSeleccionado = MutableStateFlow<String>("PENDIENTE")
    val estadoSeleccionado: StateFlow<String> = _estadoSeleccionado

    // Indicador de actualización/eliminación
    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    // Listas para selectores
    private val _clientes = MutableStateFlow<List<ClienteDto>>(emptyList())
    val clientes: StateFlow<List<ClienteDto>> = _clientes

    private val _vehiculos = MutableStateFlow<List<VehiculoDto>>(emptyList())
    val vehiculos: StateFlow<List<VehiculoDto>> = _vehiculos

    private val _zonas = MutableStateFlow<List<ZonaDto>>(emptyList())
    val zonas: StateFlow<List<ZonaDto>> = _zonas

    // Estados de selección en el formulario
    private val _selectedCliente = MutableStateFlow<ClienteDto?>(null)
    val selectedCliente: StateFlow<ClienteDto?> = _selectedCliente

    private val _selectedVehiculo = MutableStateFlow<VehiculoDto?>(null)
    val selectedVehiculo: StateFlow<VehiculoDto?> = _selectedVehiculo

    private val _selectedZona = MutableStateFlow<ZonaDto?>(null)
    val selectedZona: StateFlow<ZonaDto?> = _selectedZona

    // Fecha y hora previstas como flujos separados
    private val _fechaPrevistaFecha = MutableStateFlow<LocalDate?>(null)
    val fechaPrevistaFecha: StateFlow<LocalDate?> = _fechaPrevistaFecha

    private val _fechaPrevistaHora = MutableStateFlow<LocalTime?>(null)
    val fechaPrevistaHora: StateFlow<LocalTime?> = _fechaPrevistaHora

    init {
        loadEntregas()
        loadClientes()
        loadVehiculos()
        loadZonas()
    }

    /** Carga de entregas **/
    fun loadEntregas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _entregas.value = RetrofitInstance.api.getEntregas()
                _errorMessage.value = null
            } catch (e: IOException) {
                _errorMessage.value = "Error de red: ${'$'}{e.message}"
            } catch (e: HttpException) {
                _errorMessage.value = "Error HTTP: ${'$'}{e.code()}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Carga una entrega por ID **/
    fun loadEntrega(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _currentEntrega.value = RetrofitInstance.api.getEntrega(id)
                _estadoSeleccionado.value = _currentEntrega.value?.estado ?: "PENDIENTE"
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Carga para selectores **/
    fun loadClientes() = viewModelScope.launch {
        _clientes.value = try { RetrofitInstance.api.getClientes() } catch (_: Exception) { emptyList() }
    }
    fun loadVehiculos() = viewModelScope.launch {
        _vehiculos.value = try { RetrofitInstance.api.getVehiculos() } catch (_: Exception) { emptyList() }
    }
    fun loadZonas() = viewModelScope.launch {
        _zonas.value = try { RetrofitInstance.api.getZonas() } catch (_: Exception) { emptyList() }
    }

    /** Selecciones **/
    fun onClienteSelected(cliente: ClienteDto) { _selectedCliente.value = cliente }
    fun onVehiculoSelected(vehiculo: VehiculoDto) { _selectedVehiculo.value = vehiculo }
    fun onZonaSelected(zona: ZonaDto) { _selectedZona.value = zona }

    /** Actualizaciones fecha y hora **/
    fun onFechaPrevistaFechaChanged(fecha: LocalDate) { _fechaPrevistaFecha.value = fecha }
    fun onFechaPrevistaHoraChanged(hora: LocalTime) { _fechaPrevistaHora.value = hora }

    /** Crea nueva entrega **/
    fun createEntrega(
        direccion: String,
        pesoKg: Double,
        descripcionPaquete: String?
    ) {
        val cliente = selectedCliente.value
        val vehiculo = selectedVehiculo.value
        val zona = selectedZona.value
        val fechaF = fechaPrevistaFecha.value
        val hora = fechaPrevistaHora.value

        if (cliente == null || vehiculo == null || zona == null || fechaF == null || hora == null) {
            _errorMessage.value = "Debe completar todos los campos"
            return
        }

        val fechaPrevista = LocalDateTime.of(fechaF, hora)
        val nuevaEntrega = EntregaDto(
            direccion = direccion,
            fechaCreacion = LocalDateTime.now().toString(),
            fechaPrevista = fechaPrevista.toString(),
            estado = "PENDIENTE",
            pesoKg = pesoKg,
            descripcionPaquete = descripcionPaquete,
            clienteId = cliente.id,
            vehiculoId = vehiculo.id,
            zonaId = zona.id
        )

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val creado = RetrofitInstance.api.createEntrega(nuevaEntrega)
                _entregas.update { it + creado }
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Fallo al crear: ${'$'}{e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Actualiza una entrega **/
    fun updateEntrega(entrega: EntregaDto) {
        val entregaConEstado = entrega.copy(estado = _estadoSeleccionado.value)
        viewModelScope.launch {
            _isUpdating.value = true
            try {
                val updated = RetrofitInstance.api.updateEntrega(entregaConEstado.id!!, entregaConEstado)
                _entregas.update { list -> list.map { if (it.id == updated.id) updated else it } }
                _currentEntrega.value = updated
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Fallo al actualizar: ${e.message}"
            } finally {
                _isUpdating.value = false
            }
        }
    }

    /** Elimina una entrega **/
    fun deleteEntrega(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                RetrofitInstance.api.deleteEntrega(id)
                _entregas.update { it.filterNot { it.id == id } }
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Fallo al eliminar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Cambia el estado seleccionado en el formulario **/
    fun onEstadoChanged(nuevoEstado: String) {
        _estadoSeleccionado.value = nuevoEstado
    }

    /** Datos mock para previews **/
    fun cargarEntregasMock() {
        _entregas.value = listOf(
            EntregaDto(
                id = 1L,
                direccion = "Calle Falsa 123",
                fechaCreacion = LocalDateTime.now().minusDays(1).toString(),
                fechaPrevista = LocalDateTime.now().plusDays(1).toString(),
                fechaEfectiva = null,
                estado = "PENDIENTE",
                pesoKg = 2.5,
                descripcionPaquete = "Libro",
                clienteId = 1L,
                vehiculoId = 1L,
                zonaId = 1L
            ),
            EntregaDto(
                id = 2L,
                direccion = "Avenida Siempre Viva 742",
                fechaCreacion = LocalDateTime.now().minusHours(5).toString(),
                fechaPrevista = LocalDateTime.now().plusHours(3).toString(),
                fechaEfectiva = LocalDateTime.now().toString(),
                estado = "ENTREGADO",
                pesoKg = 1.2,
                descripcionPaquete = "Carta",
                clienteId = 2L,
                vehiculoId = 2L,
                zonaId = 2L
            )
        )
    }
    fun cargarClientesMock() {
        _clientes.value = listOf(
            ClienteDto(1L, "Juan Pérez", "juan@e.com", "123456789"),
            ClienteDto(2L, "Ana Gómez", "ana@e.com", "987654321")
        )
    }
    fun cargarVehiculosMock() {
        _vehiculos.value = listOf(
            VehiculoDto(1L, "ABC123", "moto", 15.0),
            VehiculoDto(2L, "XYZ789", "bicicleta", 5.0)
        )
    }
    fun cargarZonasMock() {
        _zonas.value = listOf(
            ZonaDto(1L, "Centro", "28001"),
            ZonaDto(2L, "Norte", "28002")
        )
    }
}