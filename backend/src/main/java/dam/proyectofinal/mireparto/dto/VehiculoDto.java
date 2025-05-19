package dam.proyectofinal.mireparto.dto;

import dam.proyectofinal.mireparto.domain.Vehiculo;

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
public class VehiculoDto {
    
	private Long id;
	
    @NotBlank @Size(max = 15)
    private String matricula;
    
    @NotBlank @Size(max = 50)
    private String tipo;
    
    @NotNull @Positive
    private Double capacidad;
    
    public VehiculoDto(Vehiculo v) {
        this.id = v.getId();
        this.matricula = v.getMatricula();
        this.tipo = v.getTipo();
        this.capacidad = v.getCapacidad();
    }
    
}
