package dam.proyectofinal.mireparto.ui.entregas

import dam.proyectofinal.mireparto.viewmodel.EntregaViewModel

import java.time.LocalDate
import java.time.LocalTime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEntregaScreen(viewModel: EntregaViewModel, onBack: () -> Unit) {
    val clientes by viewModel.clientes.collectAsState()
    val vehiculos by viewModel.vehiculos.collectAsState()
    val zonas by viewModel.zonas.collectAsState()

    val selectedCliente by viewModel.selectedCliente.collectAsState()
    val selectedVehiculo by viewModel.selectedVehiculo.collectAsState()
    val selectedZona by viewModel.selectedZona.collectAsState()
    val fechaFecha by viewModel.fechaPrevistaFecha.collectAsState()
    val fechaHora by viewModel.fechaPrevistaHora.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()


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

    val clienteError = if (selectedCliente == null) "Debes seleccionar un cliente" else null
    val vehiculoError = if (selectedVehiculo == null) "Debes seleccionar un vehículo" else null
    val zonaError = if (selectedZona == null) "Debes seleccionar una zona" else null

    val fechaValue = fechaFecha
    val fechaError = when {
        fechaValue == null -> "La fecha prevista es obligatoria"
        fechaValue.isBefore(LocalDate.now()) -> "La fecha prevista debe ser hoy o en el futuro"
        else -> null
    }
    val horaError = if (fechaHora == null) "La hora prevista es obligatoria" else null

    val isFormValid = listOf(
        direccionError,
        pesoError,
        descripcionError,
        clienteError,
        vehiculoError,
        zonaError,
        fechaError,
        horaError
    ).all { it == null }

    // Contextos para pickers
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Entrega") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dirección
            TextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            if (attemptedSubmit && direccionError != null) {
                Text(direccionError, color = MaterialTheme.colorScheme.error)
            }

            // Peso
            TextField(
                value = pesoText,
                onValueChange = { pesoText = it },
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if (attemptedSubmit && pesoError != null) {
                Text(pesoError, color = MaterialTheme.colorScheme.error)
            }

            // Descripción
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción del Paquete") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3
            )
            if (attemptedSubmit && descripcionError != null) {
                Text(descripcionError, color = MaterialTheme.colorScheme.error)
            }

            // Selector de Cliente
            ExposedDropdownMenuBox(
                expanded = clienteExpanded,
                onExpandedChange = { clienteExpanded = !clienteExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedCliente?.nombre ?: "",
                    onValueChange = { },
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

            // Selector de Vehículo
            ExposedDropdownMenuBox(
                expanded = vehiculoExpanded,
                onExpandedChange = { vehiculoExpanded = !vehiculoExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedVehiculo?.let { "${it.tipo} (${it.matricula})" } ?: "",
                    onValueChange = { },
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

            // Selector de Zona
            ExposedDropdownMenuBox(
                expanded = zonaExpanded,
                onExpandedChange = { zonaExpanded = !zonaExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedZona?.nombre ?: "",
                    onValueChange = { },
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

            // Campo de Fecha Prevista
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val today = LocalDate.now()
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                viewModel.onFechaPrevistaFechaChanged(LocalDate.of(y, m + 1, d))
                            },
                            today.year, today.monthValue - 1, today.dayOfMonth
                        ).show()
                    }
            ) {
                TextField(
                    value = fechaFecha?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Fecha Prevista") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    readOnly = true
                )
            }
            if (attemptedSubmit && fechaError != null) {
                Text(fechaError, color = MaterialTheme.colorScheme.error)
            }

            // Campo de Hora Prevista
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val now = LocalTime.now()
                        TimePickerDialog(
                            context,
                            { _, h, m ->
                                viewModel.onFechaPrevistaHoraChanged(LocalTime.of(h, m))
                            },
                            now.hour, now.minute, true
                        ).show()
                    }
            ) {
                TextField(
                    value = fechaHora?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Hora Prevista") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    readOnly = true
                )
            }
            if (attemptedSubmit && horaError != null) {
                Text(horaError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            // Botón Guardar
            Button(
                onClick = {
                    attemptedSubmit = true
                    if (isFormValid) {
                        val pesoKg = pesoText.toDoubleOrNull() ?: 0.0
                        viewModel.createEntrega(direccion, pesoKg, descripcion.ifBlank { null })
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            Spacer(Modifier.height(16.dp))

            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}