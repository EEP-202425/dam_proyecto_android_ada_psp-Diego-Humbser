package dam.proyectofinal.mireparto.ui.entregas

import dam.proyectofinal.mireparto.viewmodel.EntregaViewModel

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarEntregaScreen(
    viewModel: EntregaViewModel,
    entregaId: Long,
    onBack: () -> Unit
) {
    val entrega by viewModel.currentEntrega.collectAsState()
    val clientes by viewModel.clientes.collectAsState()
    val vehiculos by viewModel.vehiculos.collectAsState()
    val zonas by viewModel.zonas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val fechaFecha by viewModel.fechaPrevistaFecha.collectAsState()
    val fechaHora by viewModel.fechaPrevistaHora.collectAsState()
    val clienteSel by viewModel.selectedCliente.collectAsState()
    val vehiculoSel by viewModel.selectedVehiculo.collectAsState()
    val zonaSel by viewModel.selectedZona.collectAsState()
    val estadoSeleccionado by viewModel.estadoSeleccionado.collectAsState()

    val context = LocalContext.current

    // Cargar entrega y datos maestros
    LaunchedEffect(entregaId) {
        viewModel.loadEntrega(entregaId)
        viewModel.loadClientes()
        viewModel.loadVehiculos()
        viewModel.loadZonas()
    }

    // Estados locales para formulario
    var direccion by remember { mutableStateOf("") }
    var pesoText by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var clienteExpanded by remember { mutableStateOf(false) }
    var vehiculoExpanded by remember { mutableStateOf(false) }
    var zonaExpanded by remember { mutableStateOf(false) }

    var attemptedSubmit by remember { mutableStateOf(false) }

    val direccionError = when {
        direccion.isBlank() -> "La dirección es obligatoria"
        direccion.length > 200 -> "La dirección no puede superar 200 caracteres"
        else -> null
    }
    val pesoError = when {
        pesoText.isBlank() -> "El peso es obligatorio"
        pesoText.toDoubleOrNull() == null -> "El peso debe ser un número"
        pesoText.toDoubleOrNull() != null && pesoText.toDouble() <= 0.0 -> "El peso debe ser mayor que cero"
        else -> null
    }
    val descripcionError = if (descripcion.length > 300) "La descripción no puede superar 300 caracteres" else null

    val clienteError = if (clienteSel == null) "Debes seleccionar un cliente" else null
    val vehiculoError = if (vehiculoSel == null) "Debes seleccionar un vehículo" else null
    val zonaError = if (zonaSel == null) "Debes seleccionar una zona" else null

    val fechaValue = fechaFecha
    val fechaError = when {
        fechaValue == null -> "La fecha prevista es obligatoria"
        fechaValue.isBefore(LocalDate.now()) -> "La fecha prevista debe ser hoy o en el futuro"
        else -> null
    }
    val horaError = if (fechaHora == null) "La hora prevista es obligatoria" else null

    val estadoError = if (estadoSeleccionado.isBlank()) "El estado es obligatorio" else null

    val isFormValid = listOf(
        direccionError, pesoError, descripcionError,
        clienteError, vehiculoError, zonaError,
        fechaError, horaError, estadoError
    ).all { it == null }


    // Pre-poblar cuando cambia la entrega
    LaunchedEffect(entrega) {
        entrega?.let { e ->
            direccion = e.direccion
            pesoText = e.pesoKg.toString()
            descripcion = e.descripcionPaquete ?: ""
            clientes.find { it.id == e.clienteId }?.let { viewModel.onClienteSelected(it) }
            vehiculos.find { it.id == e.vehiculoId }?.let { viewModel.onVehiculoSelected(it) }
            zonas.find { it.id == e.zonaId }?.let { viewModel.onZonaSelected(it) }
            val dt = LocalDateTime.parse(e.fechaPrevista)
            viewModel.onFechaPrevistaFechaChanged(dt.toLocalDate())
            viewModel.onFechaPrevistaHoraChanged(dt.toLocalTime())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Entrega") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator()
                return@Box
            }
            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error
                )
                return@Box
            }
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Dirección, peso y descripción
                TextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (attemptedSubmit && direccionError != null) {
                    Text(direccionError, color = MaterialTheme.colorScheme.error)
                }
                TextField(
                    value = pesoText,
                    onValueChange = { pesoText = it },
                    label = { Text("Peso (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (attemptedSubmit && pesoError != null) {
                    Text(pesoError , color = MaterialTheme.colorScheme.error)
                }
                TextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción paquete") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (attemptedSubmit && descripcionError != null) {
                    Text(descripcionError, color = MaterialTheme.colorScheme.error)
                }
                // Cliente dropdown
                ExposedDropdownMenuBox(
                    expanded = clienteExpanded,
                    onExpandedChange = { clienteExpanded = !clienteExpanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = clienteSel?.nombre ?: "",
                        onValueChange = {},
                        label = { Text("Cliente") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(clienteExpanded) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = clienteExpanded,
                        onDismissRequest = { clienteExpanded = false }
                    ) {
                        clientes.forEach { cli ->
                            DropdownMenuItem(
                                text = { Text(cli.nombre) },
                                onClick = {
                                    viewModel.onClienteSelected(cli)
                                    clienteExpanded = false
                                }
                            )
                        }
                    }
                    if (attemptedSubmit && clienteError != null) {
                        Text(clienteError, color = MaterialTheme.colorScheme.error)
                    }
                }
                // Vehículo dropdown
                ExposedDropdownMenuBox(
                    expanded = vehiculoExpanded,
                    onExpandedChange = { vehiculoExpanded = !vehiculoExpanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = vehiculoSel?.let { "${it.tipo} (${it.matricula})" } ?: "",
                        onValueChange = {},
                        label = { Text("Vehículo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(vehiculoExpanded) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = vehiculoExpanded,
                        onDismissRequest = { vehiculoExpanded = false }
                    ) {
                        vehiculos.forEach { v ->
                            DropdownMenuItem(
                                text = { Text("${v.tipo} (${v.matricula})") },
                                onClick = {
                                    viewModel.onVehiculoSelected(v)
                                    vehiculoExpanded = false
                                }
                            )
                        }
                    }
                    if (attemptedSubmit && vehiculoError != null) {
                        Text(vehiculoError, color = MaterialTheme.colorScheme.error)
                    }
                }
                // Zona dropdown
                ExposedDropdownMenuBox(
                    expanded = zonaExpanded,
                    onExpandedChange = { zonaExpanded = !zonaExpanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = zonaSel?.nombre ?: "",
                        onValueChange = {},
                        label = { Text("Zona") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(zonaExpanded) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = zonaExpanded,
                        onDismissRequest = { zonaExpanded = false }
                    ) {
                        zonas.forEach { z ->
                            DropdownMenuItem(
                                text = { Text(z.nombre) },
                                onClick = {
                                    viewModel.onZonaSelected(z)
                                    zonaExpanded = false
                                }
                            )
                        }
                    }
                    if (attemptedSubmit && zonaError != null) {
                        Text(zonaError, color = MaterialTheme.colorScheme.error)
                    }
                }
                // Fecha y Hora previstta
                TextField(
                    readOnly = true,
                    value = fechaFecha?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Fecha Prevista") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val today = LocalDate.now()
                            DatePickerDialog(
                                context,
                                { _, y, m, d -> viewModel.onFechaPrevistaFechaChanged(LocalDate.of(y, m + 1, d)) },
                                today.year, today.monthValue - 1, today.dayOfMonth
                            ).show()
                        }
                )
                if (attemptedSubmit && fechaError != null) {
                    Text(fechaError, color = MaterialTheme.colorScheme.error)
                }
                TextField(
                    readOnly = true,
                    value = fechaHora?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Hora Prevista") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val now = LocalTime.now()
                            TimePickerDialog(
                                context,
                                { _, h, m -> viewModel.onFechaPrevistaHoraChanged(LocalTime.of(h, m)) },
                                now.hour, now.minute, true
                            ).show()
                        }
                )
                if (attemptedSubmit && horaError != null) {
                    Text(horaError, color = MaterialTheme.colorScheme.error)
                }
                // Selector de Estado
                Text(text = "Estado:", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("PENDIENTE", "ENTREGADO", "FALLIDO").forEach { estado ->
                        FilterChip(
                            selected = (estadoSeleccionado == estado),
                            onClick = { viewModel.onEstadoChanged(estado) },
                            label = { Text(estado) }
                        )
                    }
                }
                if (attemptedSubmit && estadoError != null) {
                    Text(estadoError, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Botón Actualizar
                Button(
                    onClick = {
                        attemptedSubmit = true
                        val entregaValue = entrega  // <--- variable local
                        if (isFormValid && entregaValue != null) {
                            val pesoKg = pesoText.toDoubleOrNull() ?: 0.0
                            val dt = LocalDateTime.of(fechaValue, fechaHora)
                            val actualizado = entregaValue.copy(  // <--- ahora sí funciona
                                direccion = direccion,
                                fechaPrevista = dt.toString(),
                                pesoKg = pesoKg,
                                descripcionPaquete = descripcion.ifBlank { null },
                                clienteId = clienteSel!!.id,
                                vehiculoId = vehiculoSel!!.id,
                                zonaId = zonaSel!!.id,
                                estado = estadoSeleccionado
                            )
                            viewModel.updateEntrega(actualizado)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Actualizar")
                }
            }
        }
    }
}