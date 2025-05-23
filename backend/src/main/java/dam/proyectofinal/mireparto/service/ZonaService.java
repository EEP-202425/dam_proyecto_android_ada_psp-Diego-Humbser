package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.Zona;
import dam.proyectofinal.mireparto.dto.ZonaDto;
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
    
    public List<ZonaDto> listarTodas() {
        return repo.findAll().stream().map(ZonaDto::new).toList();
    }
    
    public ZonaDto obtenerPorId(Long id) {
        Zona z = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Zona no encontrada: " + id));
        return new ZonaDto(z);
    }
    
    public ZonaDto crear(ZonaDto dto) {
        Zona z = new Zona();
        z.setNombre(dto.getNombre());
        z.setCodigoPostal(dto.getCodigoPostal());

        Zona guardada = repo.save(z);
        return new ZonaDto(guardada);
    }
    
    public ZonaDto actualizar(Long id, ZonaDto dto) {
        Zona existente = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Zona no encontrada: " + id));
        existente.setNombre(dto.getNombre());
        existente.setCodigoPostal(dto.getCodigoPostal());

        Zona actualizada = repo.save(existente);
        return new ZonaDto(actualizada);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
}
