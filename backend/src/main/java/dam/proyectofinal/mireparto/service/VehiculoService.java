package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.Vehiculo;
import dam.proyectofinal.mireparto.dto.VehiculoDto;
import dam.proyectofinal.mireparto.repository.VehiculoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VehiculoService {

    private final VehiculoRepository repo;

    public VehiculoService(VehiculoRepository repo) {
        this.repo = repo;
    }
    
    public List<VehiculoDto> listarTodos() {
        return repo.findAll().stream().map(VehiculoDto::new).toList();
    }
    
    public VehiculoDto obtenerPorId(Long id) {
        Vehiculo v = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Vehículo no encontrado: " + id));
        return new VehiculoDto(v);
    }
    
    public VehiculoDto crear(VehiculoDto dto) {
        Vehiculo v = new Vehiculo();
        v.setMatricula(dto.getMatricula());
        v.setTipo(dto.getTipo());
        v.setCapacidad(dto.getCapacidad());

        Vehiculo guardado = repo.save(v);
        return new VehiculoDto(guardado);
    }
    
    public VehiculoDto actualizar(Long id, VehiculoDto dto) {
        Vehiculo existente = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Vehículo no encontrado: " + id));
        existente.setMatricula(dto.getMatricula());
        existente.setTipo(dto.getTipo());
        existente.setCapacidad(dto.getCapacidad());

        Vehiculo actualizado = repo.save(existente);
        return new VehiculoDto(actualizado);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
}
