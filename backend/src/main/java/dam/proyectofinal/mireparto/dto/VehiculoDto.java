package dam.proyectofinal.mireparto.dto;

import dam.proyectofinal.mireparto.domain.Vehiculo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDto {
    
	private Long id;
    private String matricula;
    private String tipo;
    private Double capacidad;
    
    public VehiculoDto(Vehiculo v) {
        this.id = v.getId();
        this.matricula = v.getMatricula();
        this.tipo = v.getTipo();
        this.capacidad = v.getCapacidad();
    }
    
}
