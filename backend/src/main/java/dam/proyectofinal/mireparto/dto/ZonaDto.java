package dam.proyectofinal.mireparto.dto;

import dam.proyectofinal.mireparto.domain.Zona;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaDto {

    private Long id;
    
    @NotBlank @Size(max = 100)
    private String nombre;
    
    @NotBlank @Size(max = 10)
    private String codigoPostal;
    
    public ZonaDto(Zona z) {
        this.id = z.getId();
        this.nombre = z.getNombre();
        this.codigoPostal = z.getCodigoPostal();
    }
	
}
