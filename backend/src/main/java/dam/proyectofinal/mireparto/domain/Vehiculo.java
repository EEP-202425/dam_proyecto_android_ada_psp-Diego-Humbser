package dam.proyectofinal.mireparto.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula", nullable = false, unique = true, length = 15)
    private String matricula;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo; // Ej: "bicicleta", "moto", "furgoneta"

    @Column(name = "capacidad", nullable = false)
    private Double capacidad; // Capacidad de carga en kg, litros, etc.

    @OneToMany(mappedBy = "vehiculo")
    private Set<Entrega> entregas = new HashSet<>();
    
    public Vehiculo() {}
    
    public Vehiculo(String matricula, String tipo, Double capacidad) {
        this.matricula = matricula;
        this.tipo = tipo;
        this.capacidad = capacidad;
    }
    
    public Long getId() { 
    	return id; 
    }

	public void setId(long id) {
		this.id = id;
	}
    
    public String getMatricula() { 
    	return matricula; 
    }
    
    public void setMatricula(String matricula) { 
    	this.matricula = matricula; 
    }
    
    public String getTipo() { 
    	return tipo; 
    }
    
    public void setTipo(String tipo) { 
    	this.tipo = tipo; 
    }
    
    public Double getCapacidad() { 
    	return capacidad; 
    }
    
    public void setCapacidad(Double capacidad) { 
    	this.capacidad = capacidad; 
    }
    
    public Set<Entrega> getEntregas() { 
    	return entregas; 
    }

    public void setEntregas(Set<Entrega> entregas) { 
    	this.entregas = entregas; 
    }
    
    public void addEntrega(Entrega entrega) {
        entregas.add(entrega);
        entrega.setVehiculo(this);
    }

    public void removeEntrega(Entrega entrega) {
        entregas.remove(entrega);
        entrega.setVehiculo(null);
    }
    
    @Override
    public String toString() {
        return "Vehiculo [id=" + id + ", matricula=" + matricula + ", tipo=" + tipo + 
        		", capacidad=" + capacidad + "]";
    }
    
}
