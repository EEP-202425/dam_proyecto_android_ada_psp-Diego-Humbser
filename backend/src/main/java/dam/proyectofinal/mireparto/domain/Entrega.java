package dam.proyectofinal.mireparto.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrega")
public class Entrega {
	
	@Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;
    
    @Column(name = "horario", nullable = false)
    private LocalDateTime horario;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoEntrega estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;
    
    public Entrega() {}

    public Entrega(String direccion, LocalDateTime horario, EstadoEntrega estado, 
    				Cliente cliente, Vehiculo vehiculo, Zona zona) {
        this.direccion = direccion;
        this.horario = horario;
        this.estado = estado;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.zona = zona;
    }
    
    public Long getId() { 
    	return id; 
    }
    
	public void setId(long id) {
		this.id = id;
	}
    
    public String getDireccion() { 
    	return direccion; 
    }
    
    public void setDireccion(String direccion) { 
    	this.direccion = direccion; 
    }    
    
    public LocalDateTime getHorario() { 
    	return horario; 
    }
    
    public void setHorario(LocalDateTime horario) { 
    	this.horario = horario; 
    }
    
    public EstadoEntrega getEstado() { 
    	return estado; 
    }
    
    public void setEstado(EstadoEntrega estado) { 
    	this.estado = estado; 
    }
    
    public Cliente getCliente() { 
    	return cliente; 
    }
    
    public void setCliente(Cliente cliente) { 
        // evita ciclos infinitos
        if (sameAsFormer(cliente)) {
            return;
        }
        // rompe la antigua relación
        Cliente old = this.cliente;
        this.cliente = cliente;
        if (old != null) {
            old.getEntregas().remove(this);
        }
        // establece la nueva relación
        if (cliente != null && !cliente.getEntregas().contains(this)) {
            cliente.getEntregas().add(this);
        }
    }
    
    private boolean sameAsFormer(Cliente newCliente) {
        return cliente == null ? newCliente == null : cliente.equals(newCliente);
    }
    
    public Vehiculo getVehiculo() { 
    	return vehiculo; 
    }
    
    public void setVehiculo(Vehiculo vehiculo) { 
        if (sameVehiculo(vehiculo)) {
        	return;
        }
        Vehiculo old = this.vehiculo;
        this.vehiculo = vehiculo;
        if (old != null) {
            old.getEntregas().remove(this);
        }
        if (vehiculo != null) {
            vehiculo.getEntregas().add(this);
        }
    }
    
    private boolean sameVehiculo(Vehiculo newVehiculo) {
        return vehiculo == null ? newVehiculo == null : vehiculo.equals(newVehiculo);
    }
    
    public Zona getZona() { 
    	return zona; 
    }
    
    public void setZona(Zona zona) { 
        if (sameZona(zona)) {
        	return;
        }
        Zona old = this.zona;
        this.zona = zona;
        if (old != null) {
            old.getEntregas().remove(this);
        }
        if (zona != null) {
            zona.getEntregas().add(this);
        }
    }
    
    private boolean sameZona(Zona newZona) {
        return zona == null ? newZona == null : zona.equals(newZona);
    }
    
    @Override
    public String toString() {
        return "Entrega [id=" + id +
               ", direccion='" + direccion + '\'' +
               ", horario=" + horario +
               ", estado=" + estado +               
               ", clienteId=" + (cliente != null ? cliente.getId() : "null") +
               ", vehiculoId=" + (vehiculo != null ? vehiculo.getId() : "null") +
               ", zonaId=" + (zona != null ? zona.getId() : "null") + "]";
    }
    
}
