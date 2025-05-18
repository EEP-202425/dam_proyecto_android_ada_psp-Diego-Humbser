package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.Zona;
import dam.proyectofinal.mireparto.repository.ZonaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ZonaService {

    private final ZonaRepository repo;

    public ZonaService(ZonaRepository repo) {
        this.repo = repo;
    }

    public List<Zona> listarTodos() {
        return repo.findAll();
    }

    public Zona obtenerPorId(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new NoSuchElementException("Zona no encontrada: " + id));
    }

    public Zona crear(Zona zona) {
        return repo.save(zona);
    }

    public Zona actualizar(Long id, Zona datos) {
        Zona existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setCodigoPostal(datos.getCodigoPostal());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
}
