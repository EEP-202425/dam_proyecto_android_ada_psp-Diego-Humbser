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

//    public List<Cliente> listarTodos() {
//        return repo.findAll();
//    }
    
    public List<ClienteDto> listarTodos() {
        return repo.findAll().stream().map(ClienteDto::new).toList();
    }

//    public Cliente obtenerPorId(Long id) {
//        return repo.findById(id)
//                   .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
//    }
    
    public ClienteDto obtenerPorId(Long id) {
        Cliente c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
        return new ClienteDto(c);
    }

//    public Cliente crear(Cliente cliente) {
//        return repo.save(cliente);
//    }
    
    public ClienteDto crear(ClienteDto dto) {
        // Construir la entidad Cliente desde el DTO
        Cliente c = new Cliente();
        c.setNombre(dto.getNombre());
        c.setTelefono(dto.getTelefono());
        c.setEmail(dto.getEmail());
        // Guardar y devolver DTO resultante
        Cliente guardado = repo.save(c);
        return new ClienteDto(guardado);
    }

//    public Cliente actualizar(Long id, Cliente datos) {
//        Cliente existente = obtenerPorId(id);
//        existente.setNombre(datos.getNombre());
//        existente.setTelefono(datos.getTelefono());
//        existente.setEmail(datos.getEmail());
//        return repo.save(existente);
//    }

    public ClienteDto actualizar(Long id, ClienteDto dto) {
        Cliente existente = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
        existente.setNombre(dto.getNombre());
        existente.setTelefono(dto.getTelefono());
        existente.setEmail(dto.getEmail());
        Cliente actualizado = repo.save(existente);
        return new ClienteDto(actualizado);
    }
    
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
}
