package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.Entrega;
import dam.proyectofinal.mireparto.domain.EstadoEntrega;
import dam.proyectofinal.mireparto.dto.EntregaDto;
import dam.proyectofinal.mireparto.repository.EntregaRepository;
import dam.proyectofinal.mireparto.repository.ClienteRepository;
import dam.proyectofinal.mireparto.repository.VehiculoRepository;
import dam.proyectofinal.mireparto.repository.ZonaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EntregaService {

    private final EntregaRepository repo;
    private final ClienteRepository clienteRepo;
    private final VehiculoRepository vehiculoRepo;
    private final ZonaRepository zonaRepo;

    public EntregaService(EntregaRepository repo, ClienteRepository clienteRepo, 
    		VehiculoRepository vehiculoRepo, ZonaRepository zonaRepo) {
        this.repo = repo;
        this.clienteRepo = clienteRepo;
        this.vehiculoRepo = vehiculoRepo;
        this.zonaRepo = zonaRepo;
    }
    
    public List<EntregaDto> listarTodas() {
        return repo.findAll().stream().map(EntregaDto::new).toList();
    }
    
    public EntregaDto obtenerPorId(Long id) {
        Entrega e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Entrega no encontrada: " + id));
        return new EntregaDto(e);
    }

    public List<Entrega> listarPorEstado(EstadoEntrega estado) {
        return repo.findByEstado(estado);
    }

    public EntregaDto crear(EntregaDto dto) {
        // Validar y cargar entidades relacionadas
        var cliente = clienteRepo.findById(dto.getClienteId())
        				.orElseThrow(() -> new NoSuchElementException("Cliente no existe: " + dto.getClienteId()));
        var vehiculo = vehiculoRepo.findById(dto.getVehiculoId())
        				.orElseThrow(() -> new NoSuchElementException("Vehículo no existe: " + dto.getVehiculoId()));
        var zona = zonaRepo.findById(dto.getZonaId())
        				.orElseThrow(() -> new NoSuchElementException("Zona no existe: " + dto.getZonaId()));

        // Construir la entidad Entrega
        Entrega e = new Entrega(
        		dto.getDireccion(),
        		dto.getFechaPrevista(),
        		EstadoEntrega.valueOf(dto.getEstado()),
        		dto.getPesoKg(),
        		dto.getDescripcionPaquete(),
        		cliente, 
        		vehiculo, 
        		zona);

        // Guardar y devolver como DTO
        Entrega guardada = repo.save(e);
        return new EntregaDto(guardada);
    }    

    public EntregaDto actualizar(Long id, EntregaDto dto) {
        Entrega e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Entrega no encontrada: " + id));

        e.setDireccion(dto.getDireccion());
        e.setFechaPrevista(dto.getFechaPrevista());
        e.setFechaEfectiva(dto.getFechaEfectiva());
        e.setEstado(EstadoEntrega.valueOf(dto.getEstado()));
        e.setPesoKg(dto.getPesoKg());
        e.setDescripcionPaquete(dto.getDescripcionPaquete());
        
        Entrega actualizada = repo.save(e);
        return new EntregaDto(actualizada);
    }
    
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
    
    // Lista todas las entregas pendientes en la zona indicada
    
    public List<EntregaDto> listarPendientesPorZona(String nombreZona) {
        return repo.findByEstadoAndZonaNombre(EstadoEntrega.PENDIENTE, nombreZona)
                   .stream()
                   .map(EntregaDto::new)
                   .toList();
    }
    
    // Marca una entrega como ENTREGADO
    
    public EntregaDto marcarEntregada(Long id) {
        Entrega e = repo.findById(id)
                     .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada: " + id));
        e.setEstado(EstadoEntrega.ENTREGADO);
        return new EntregaDto(repo.save(e));
    }
    
    // Marca una entrega como FALLIDO
    
    public EntregaDto marcarFallida(Long id) {
        Entrega e = repo.findById(id)
                     .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada: " + id));
        e.setEstado(EstadoEntrega.FALLIDO);
        return new EntregaDto(repo.save(e));
    }
    
}
