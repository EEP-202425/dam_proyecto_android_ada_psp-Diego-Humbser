package dam.proyectofinal.mireparto.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dam.proyectofinal.mireparto.dto.EntregaDto;
import dam.proyectofinal.mireparto.service.EntregaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

	private final EntregaService service;

    public EntregaController(EntregaService service) {
        this.service = service;
    }
    
    @GetMapping
    public List<EntregaDto> listarTodas() {
        return service.listarTodas();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EntregaDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
    
    @PostMapping
    public ResponseEntity<EntregaDto> crear(@Valid @RequestBody EntregaDto dto) {
        EntregaDto creado = service.crear(dto);
        URI location = ServletUriComponentsBuilder
                         .fromCurrentRequest()
                         .path("/{id}")
                         .buildAndExpand(creado.getId())
                         .toUri();
        return ResponseEntity.created(location).body(creado);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EntregaDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EntregaDto dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    // endpoints adicionales:
    @GetMapping("/pendientes")
    public List<EntregaDto> listarPendientesPorZona(@RequestParam String zona) {
        return service.listarPendientesPorZona(zona);
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<EntregaDto> marcarEntregada(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarEntregada(id));
    }

    @PatchMapping("/{id}/fallar")
    public ResponseEntity<EntregaDto> marcarFallida(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarFallida(id));
    }
	
}
