package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.Cliente;
import dam.proyectofinal.mireparto.dto.ClienteDto;
import dam.proyectofinal.mireparto.repository.ClienteRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }
    
    public List<ClienteDto> listarTodos() {
        return repo.findAll().stream().map(ClienteDto::new).toList();
    }
    
    public ClienteDto obtenerPorId(Long id) {
        Cliente c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
        return new ClienteDto(c);
    }
    
    public ClienteDto crear(ClienteDto dto) {
        // Construir la entidad Cliente desde el DTO
        Cliente c = new Cliente();
        c.setNombre(dto.getNombre());
        c.setEmail(dto.getEmail());
        c.setTelefono(dto.getTelefono());
        // Guardar y devolver DTO resultante
        Cliente guardado = repo.save(c);
        return new ClienteDto(guardado);
    }

    public ClienteDto actualizar(Long id, ClienteDto dto) {
        Cliente existente = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
        existente.setNombre(dto.getNombre());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        Cliente actualizado = repo.save(existente);
        return new ClienteDto(actualizado);
    }
    
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
}
