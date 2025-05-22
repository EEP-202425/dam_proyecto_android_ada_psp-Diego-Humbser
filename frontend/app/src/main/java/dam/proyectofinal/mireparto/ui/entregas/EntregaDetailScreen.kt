package dam.proyectofinal.mireparto.ui.entregas

import dam.proyectofinal.mireparto.model.EntregaDto
import dam.proyectofinal.mireparto.viewmodel.EntregaViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntregaDetailScreen(
    viewModel: EntregaViewModel,
    entregaId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    // Cargar datos al entrar
    LaunchedEffect(entregaId) {
        viewModel.loadEntrega(entregaId)
        viewModel.loadClientes()
        viewModel.loadVehiculos()
        viewModel.loadZonas()
    }

    val entrega by viewModel.currentEntrega.collectAsState()
    val clientes by viewModel.clientes.collectAsState()
    val vehiculos by viewModel.vehiculos.collectAsState()
    val zonas by viewModel.zonas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle de Entrega") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(entregaId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(entregaId) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                entrega != null -> {
                    val e: EntregaDto = entrega!!
                    // Resolver nombres
                    val clienteNombre = clientes.find { it.id == e.clienteId }?.nombre ?: "-"
                    val vehiculoInfo = vehiculos.find { it.id == e.vehiculoId }?.let { "${it.tipo} (${it.matricula})" } ?: "-"
                    val zonaNombre = zonas.find { it.id == e.zonaId }?.nombre ?: "-"

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "Dirección:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = e.direccion)

                        Text(text = "Cliente:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = clienteNombre)

                        Text(text = "Vehículo:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = vehiculoInfo)

                        Text(text = "Zona:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = zonaNombre)

                        Text(text = "Fecha Creación:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = e.fechaCreacion)

                        Text(text = "Fecha Prevista:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = e.fechaPrevista)

                        e.fechaEfectiva?.let {
                            Text(text = "Fecha Efectiva:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = it)
                        }

                        Text(text = "Peso (kg):", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "${e.pesoKg}")

                        e.descripcionPaquete?.let {
                            Text(text = "Descripción:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = it)
                        }

                        Text(text = "Estado:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = e.estado)
                    }
                }
            }
        }
    }
}
