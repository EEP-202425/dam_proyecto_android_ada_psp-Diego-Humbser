package dam.proyectofinal.mireparto.dto;

import dam.proyectofinal.mireparto.domain.Cliente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
	
    private Long id;
    private String nombre;
    private String telefono;
    private String email;
	
    public ClienteDto(Cliente c) {
        this.id = c.getId();
        this.nombre = c.getNombre();
        this.telefono = c.getTelefono();
        this.email = c.getEmail();
    }
    
}
