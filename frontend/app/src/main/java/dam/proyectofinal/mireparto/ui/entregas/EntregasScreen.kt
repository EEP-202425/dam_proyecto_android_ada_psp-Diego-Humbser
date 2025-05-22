package dam.proyectofinal.mireparto.ui.entregas

import dam.proyectofinal.mireparto.viewmodel.EntregaViewModel
import dam.proyectofinal.mireparto.model.EntregaDto
import dam.proyectofinal.mireparto.model.ClienteDto
import dam.proyectofinal.mireparto.model.VehiculoDto
import dam.proyectofinal.mireparto.model.ZonaDto
import dam.proyectofinal.mireparto.ui.theme.MiRepartoTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EntregasScreen(viewModel: EntregaViewModel, onNavigateToCreate: () -> Unit, onItemClick: (Long) -> Unit, onDelete: (Long) -> Unit) {
    val entregas by viewModel.entregas.collectAsState()
    val clientes by viewModel.clientes.collectAsState()
    val vehiculos by viewModel.vehiculos.collectAsState()
    val zonas by viewModel.zonas.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = onNavigateToCreate,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nueva Entrega")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(entregas, key = { it.id ?: 0L }) { entrega ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { entrega.id?.let(onItemClick) },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Datos principales
                        Text(
                            text = "Destino: ${entrega.direccion}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(text = "Cliente: ${clientes.find { it.id == entrega.clienteId }?.nombre ?: "-"}")
                        Text(text = "Vehículo: ${vehiculos.find { it.id == entrega.vehiculoId }?.let { "${it.tipo} (${it.matricula})" } ?: "-"}")
                        Text(text = "Zona: ${zonas.find { it.id == entrega.zonaId }?.nombre ?: "-"}")
                    }
                    IconButton(
                        onClick = { entrega.id?.let(onDelete) },
                        modifier = Modifier.alignByBaseline()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar entrega"
                        )
                    }
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun EntregaItem(
    entrega: EntregaDto,
    clientes: List<ClienteDto>,
    vehiculos: List<VehiculoDto>,
    zonas: List<ZonaDto>
) {
    // Resuelve los nombres a partir de los IDs
    val clienteNombre = clientes.find { it.id == entrega.clienteId }?.nombre
        ?: "Cliente desconocido"
    val vehiculoInfo = vehiculos.find { it.id == entrega.vehiculoId }?.let { "${it.tipo} (${it.matricula})" }
        ?: "Vehículo desconocido"
    val zonaNombre = zonas.find { it.id == entrega.zonaId }?.nombre
        ?: "Zona desconocida"

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Destino: ${entrega.direccion}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(text = "Cliente: $clienteNombre")
        Text(text = "Vehículo: $vehiculoInfo")
        Text(text = "Zona: $zonaNombre")

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Creada: ${entrega.fechaCreacion}")
            Text("Prevista: ${entrega.fechaPrevista}")
            entrega.fechaEfectiva?.let { Text("Realizada: $it") }
        }

        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Peso: ${entrega.pesoKg} kg")
            entrega.descripcionPaquete?.let { Text("Descripción: $it") }
        }

        Text(
            text = "Estado: ${entrega.estado}",
            color = when (entrega.estado) {
                "PENDIENTE" -> Color.Gray
                "ENTREGADO" -> Color.Green
                "FALLIDO" -> Color.Red
                else -> Color.Black
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEntregasScreen() {
    val vm = remember { EntregaViewModel().apply {
        cargarEntregasMock()
        cargarClientesMock()
        cargarVehiculosMock()
        cargarZonasMock()
        }
    }
    MiRepartoTheme {
        EntregasScreen(
            viewModel = vm,
            onNavigateToCreate = {},
            onItemClick = {},
            onDelete = {}
        )
    }
}