package dam.proyectofinal.mireparto.dto;

import java.time.LocalDateTime;

import dam.proyectofinal.mireparto.domain.Entrega;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    
    @NotNull(message = "El horario es obligatorio")
    @FutureOrPresent(message = "El horario debe ser presente o futuro") 
    private LocalDateTime horario;
    
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
    
    @NotNull @Positive(message = "clienteId debe ser un valor positivo")
    private Long clienteId;
    
    @NotNull @Positive(message = "vehiculoId debe ser un valor positivo")
    private Long vehiculoId;
    
    @NotNull @Positive(message = "zonaId debe ser un valor positivo")
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
