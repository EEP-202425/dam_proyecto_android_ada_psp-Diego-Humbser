package dam.proyectofinal.mireparto

import dam.proyectofinal.mireparto.viewmodel.EntregaViewModel
import dam.proyectofinal.mireparto.model.ClienteDto
import dam.proyectofinal.mireparto.model.VehiculoDto
import dam.proyectofinal.mireparto.model.ZonaDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class EntregaViewModelTest {
    private lateinit var viewModel: EntregaViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EntregaViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun selectedClienteActualizado() = runTest {
        val cliente = ClienteDto(1, "Juan Perez", "juan@mail.com", "666111222")
        viewModel.onClienteSelected(cliente)
        assertEquals(cliente, viewModel.selectedCliente.value)
    }

    @Test
    fun selectedVehiculoActualizado() = runTest {
        val vehiculo = VehiculoDto(1, "Furgoneta", "1234ABC", 500.0)
        viewModel.onVehiculoSelected(vehiculo)
        assertEquals(vehiculo, viewModel.selectedVehiculo.value)
    }

    @Test
    fun selectedZonaActualizada() = runTest {
        val zona = ZonaDto(1, "Centro", "28001")
        viewModel.onZonaSelected(zona)
        assertEquals(zona, viewModel.selectedZona.value)
    }

    @Test
    fun fechaPrevistaFechaActualizada() = runTest {
        val fecha = LocalDate.of(2025, 5, 23)
        viewModel.onFechaPrevistaFechaChanged(fecha)
        assertEquals(fecha, viewModel.fechaPrevistaFecha.value)
    }

    @Test
    fun fechaPrevistaHoraActualizada() = runTest {
        val hora = LocalTime.of(10, 30)
        viewModel.onFechaPrevistaHoraChanged(hora)
        assertEquals(hora, viewModel.fechaPrevistaHora.value)
    }

    @Test
    fun estadoSeleccionadoActualizado() = runTest {
        viewModel.onEstadoChanged("ENTREGADO")
        assertEquals("ENTREGADO", viewModel.estadoSeleccionado.value)
    }

    @Test
    fun errorMessageLimpiado() = runTest {
        viewModel.clearError()
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun crearEntregaCamposIncompletos() = runTest {
        // Ningún campo seleccionado, ni fecha ni hora
        viewModel.createEntrega("Calle 123", 2.0, "Test paquete")
        assertEquals("Debe completar todos los campos", viewModel.errorMessage.value)
    }
}