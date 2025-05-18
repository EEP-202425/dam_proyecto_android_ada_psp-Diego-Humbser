package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.Cliente;
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

    public List<Cliente> listarTodos() {
        return repo.findAll();
    }

    public Cliente obtenerPorId(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
    }

    public Cliente crear(Cliente cliente) {
        return repo.save(cliente);
    }

    public Cliente actualizar(Long id, Cliente datos) {
        Cliente existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setTelefono(datos.getTelefono());
        existente.setEmail(datos.getEmail());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
}
