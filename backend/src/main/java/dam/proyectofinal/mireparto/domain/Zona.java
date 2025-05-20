package dam.proyectofinal.mireparto.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "zona")
public class Zona {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_postal", nullable = false, length = 20)
    private String codigoPostal;
    
    @OneToMany(mappedBy = "zona")
    private Set<Entrega> entregas = new HashSet<>();
    
    public Zona() {}

    public Zona(String nombre, String codigoPostal) {
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
    }
    
    public Long getId() { 
    	return id; 
    }
    
	public void setId(long id) {
		this.id = id;
	}
    
    public String getNombre() { 
    	return nombre; 
    }
    
    public void setNombre(String nombre) { 
    	this.nombre = nombre; 
    }
    
    public String getCodigoPostal() { 
    	return codigoPostal; 
    }
    
    public void setCodigoPostal(String codigoPostal) { 
    	this.codigoPostal = codigoPostal; 
    }
    
    public Set<Entrega> getEntregas() { 
    	return entregas; 
    }

    public void setEntregas(Set<Entrega> entregas) { 
    	this.entregas = entregas;
    }
    
    public void addEntrega(Entrega entrega) {
        entregas.add(entrega);
        entrega.setZona(this);
    }

    public void removeEntrega(Entrega entrega) {
        entregas.remove(entrega);
        entrega.setZona(null);
    }
    
    @Override
    public String toString() {
        return "Zona [id=" + id + ", nombre=" + nombre + ", codigoPostal=" + codigoPostal + "]";
    }
    
}
