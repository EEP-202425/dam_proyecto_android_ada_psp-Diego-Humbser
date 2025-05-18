package dam.proyectofinal.mireparto.repository;

import dam.proyectofinal.mireparto.domain.Entrega;
import dam.proyectofinal.mireparto.domain.EstadoEntrega;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> findByEstado(EstadoEntrega estado);
    List<Entrega> findByClienteId(Long clienteId);
    List<Entrega> findByZonaNombre(String nombreZona);
    List<Entrega> findByEstadoAndZonaNombre(EstadoEntrega estado, String nombreZona);
    
}
