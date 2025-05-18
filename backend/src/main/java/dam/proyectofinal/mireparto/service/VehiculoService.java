package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.Vehiculo;
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

    public List<Vehiculo> listarTodos() {
        return repo.findAll();
    }

    public Vehiculo obtenerPorId(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new NoSuchElementException("Vehículo no encontrado: " + id));
    }

    public Vehiculo crear(Vehiculo vehiculo) {
        return repo.save(vehiculo);
    }

    public Vehiculo actualizar(Long id, Vehiculo datos) {
        Vehiculo existente = obtenerPorId(id);
        existente.setMatricula(datos.getMatricula());
        existente.setTipo(datos.getTipo());
        existente.setCapacidad(datos.getCapacidad());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
}
