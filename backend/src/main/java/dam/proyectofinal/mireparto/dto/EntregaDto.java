package dam.proyectofinal.mireparto.dto;

import java.time.LocalDateTime;

import dam.proyectofinal.mireparto.domain.Entrega;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaDto {
    
	private Long id;
    private String direccion;
    private String destinatario;
    private LocalDateTime horario;
    private String estado;
    private Long clienteId;
    private Long vehiculoId;
    private Long zonaId;
    
    public EntregaDto(Entrega e) {
        this.id = e.getId();
        this.direccion = e.getDireccion();
        this.horario = e.getHorario();
        this.estado = e.getEstado().name();
        this.clienteId = e.getCliente().getId();
        this.vehiculoId = e.getVehiculo().getId();
        this.zonaId = e.getZona().getId();
    }
    
}
