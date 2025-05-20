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
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fecha_creacion;
    
    @Column(name = "fecha_prevista", nullable = false)
    private LocalDateTime fecha_prevista;
    
    @Column(name = "fecha_efectiva", nullable = true)
    private LocalDateTime fecha_efectiva;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoEntrega estado;
    
    @Column(name = "peso_kg", nullable = false)
    private Double peso_kg;
    
    @Column(name = "descripcion_paquete", length = 300)
    private String descripcion_paquete;

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

    public Entrega(String direccion, LocalDateTime fecha_prevista, EstadoEntrega estado, Double peso_kg, String descripcion_paquete,
    		Cliente cliente, Vehiculo vehiculo, Zona zona) {
    	  this.direccion = direccion;
    	  this.fecha_prevista = fecha_prevista;
    	  this.estado = estado;
    	  this.peso_kg = peso_kg;
    	  this.descripcion_paquete = descripcion_paquete;
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
    
    public LocalDateTime getFechaCreacion() {
    	return fecha_creacion;
    }
    
    @PrePersist
    protected void onCreate() {
        this.fecha_creacion = LocalDateTime.now();
    }
    
    public LocalDateTime getFechaPrevista() { 
    	return fecha_prevista; 
    }
    
    public void setFechaPrevista(LocalDateTime fecha_prevista) { 
    	this.fecha_prevista = fecha_prevista; 
    }

    public LocalDateTime getFechaEfectiva() {
    	return fecha_efectiva;
    }
    
    public void setFechaEfectiva(LocalDateTime fecha_efectiva) {
    	this.fecha_efectiva = fecha_efectiva;
    }    
    
    public EstadoEntrega getEstado() { 
    	return estado; 
    }
    
    public void setEstado(EstadoEntrega estado) { 
    	this.estado = estado; 
    }
    
    public Double getPesoKg() {
    	return peso_kg;
    }
    
    public void setPesoKg(Double peso_kg) {
    	this.peso_kg = peso_kg;
    }
    
    public String getDescripcionPaquete() {
    	return descripcion_paquete;
    }
    
    public void setDescripcionPaquete(String descripcion_paquete) {
    	this.descripcion_paquete = descripcion_paquete;
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
               ", fechaCreacion=" + fecha_creacion +
               ", fechaPrevista=" + fecha_prevista +
               ", fechaEfectiva=" + fecha_efectiva +
               ", estado=" + estado +
               ", pesoKg=" + peso_kg +
               ", descripcionPaquete='" + descripcion_paquete + '\'' +
               ", clienteId=" + (cliente != null ? cliente.getId() : "null") +
               ", vehiculoId=" + (vehiculo != null ? vehiculo.getId() : "null") +
               ", zonaId=" + (zona != null ? zona.getId() : "null") + "]";
    }
    
}
