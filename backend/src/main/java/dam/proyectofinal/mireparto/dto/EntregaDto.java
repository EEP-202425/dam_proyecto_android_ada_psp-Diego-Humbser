package dam.proyectofinal.mireparto.dto;

import java.time.LocalDateTime;

import dam.proyectofinal.mireparto.domain.Entrega;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaDto {
    
	private Long id;
	
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede superar 200 caracteres")
    private String direccion;
        
    private LocalDateTime fechaCreacion;
    
    @NotNull(message = "La fecha prevista es obligatoria")
    @FutureOrPresent(message = "La fecha prevista debe ser hoy o en el futuro")
    private LocalDateTime fechaPrevista;
        
    @PastOrPresent(message = "La fecha efectiva no puede estar en el futuro")
    private LocalDateTime fechaEfectiva;
    
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
    
    @NotNull(message = "El peso del paquete es obligatorio")
    @Positive(message = "El peso debe ser mayor que cero")
    private Double pesoKg;
    
    @Size(max = 300, message = "La descripción no puede superar 300 caracteres")
    private String descripcionPaquete;
    
    @NotNull @Positive(message = "clienteId debe ser un valor positivo")
    private Long clienteId;
    
    @NotNull @Positive(message = "vehiculoId debe ser un valor positivo")
    private Long vehiculoId;
    
    @NotNull @Positive(message = "zonaId debe ser un valor positivo")
    private Long zonaId;
    
    public EntregaDto(Entrega e) {
        this.id = e.getId();
        this.direccion = e.getDireccion();
        this.fechaCreacion = e.getFechaCreacion();
        this.fechaPrevista = e.getFechaPrevista();
        this.fechaEfectiva = e.getFechaEfectiva();
        this.estado = e.getEstado().name();
        this.pesoKg = e.getPesoKg();
        this.descripcionPaquete = e.getDescripcionPaquete();
        this.clienteId = e.getCliente().getId();
        this.vehiculoId = e.getVehiculo().getId();
        this.zonaId = e.getZona().getId();
    }
    
}
