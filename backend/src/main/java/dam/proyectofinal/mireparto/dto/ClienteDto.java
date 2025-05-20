package dam.proyectofinal.mireparto.dto;

import dam.proyectofinal.mireparto.domain.Cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
	
    private Long id;
    
    @NotBlank(message = "El nombre no puede quedar vacío")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;   
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Size(max = 100, message = "El email no puede superar 100 caracteres")
    private String email;
       
    @Pattern(regexp = "\\+?[0-9\\- ]{7,20}", message = "Formato de teléfono inválido")
    private String telefono;
	
    public ClienteDto(Cliente c) {
        this.id = c.getId();
        this.nombre = c.getNombre();
        this.email = c.getEmail();
        this.telefono = c.getTelefono();
    }
    
}
