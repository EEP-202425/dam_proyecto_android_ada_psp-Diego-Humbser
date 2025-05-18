package dam.proyectofinal.mireparto.dto;

import dam.proyectofinal.mireparto.domain.Zona;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaDto {

    private Long id;
    private String nombre;
    private String codigoPostal;
    
    public ZonaDto(Zona z) {
        this.id = z.getId();
        this.nombre = z.getNombre();
        this.codigoPostal = z.getCodigoPostal();
    }
	
}
