package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.Entrega;
import dam.proyectofinal.mireparto.domain.EstadoEntrega;
import dam.proyectofinal.mireparto.repository.EntregaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EntregaService {

    private final EntregaRepository repo;

    public EntregaService(EntregaRepository repo) {
        this.repo = repo;
    }

    public List<Entrega> listarTodas() {
        return repo.findAll();
    }

    public Entrega obtenerPorId(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada: " + id));
    }

    public List<Entrega> listarPorEstado(EstadoEntrega estado) {
        return repo.findByEstado(estado);
    }

    public Entrega crear(Entrega entrega) {
        entrega.setEstado(EstadoEntrega.PENDIENTE);
        return repo.save(entrega);
    }

    public Entrega actualizar(Long id, Entrega datos) {
        Entrega existente = obtenerPorId(id);
        existente.setDireccion(datos.getDireccion());
        existente.setDestinatario(datos.getDestinatario());
        existente.setHorario(datos.getHorario());
        existente.setEstado(datos.getEstado());
        existente.setCliente(datos.getCliente());
        existente.setVehiculo(datos.getVehiculo());
        existente.setZona(datos.getZona());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
    // Lista todas las entregas pendientes en la zona indicada
    public List<Entrega> listarPendientesPorZona(String nombreZona) {
        return repo.findByEstadoAndZonaNombre(EstadoEntrega.PENDIENTE, nombreZona);
    }
    
    // Marca una entrega como ENTREGADO
    public Entrega marcarEntregada(Long id) {
        Entrega e = repo.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada: " + id));
        e.setEstado(EstadoEntrega.ENTREGADO);
        return repo.save(e);
    }
    
    // Marca una entrega como FALLIDO
    public Entrega marcarFallida(Long id) {
        Entrega e = repo.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada: " + id));
        e.setEstado(EstadoEntrega.FALLIDO);
        return repo.save(e);
    }
    
}
