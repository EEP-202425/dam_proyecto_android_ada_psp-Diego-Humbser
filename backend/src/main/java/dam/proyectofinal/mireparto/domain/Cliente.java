package dam.proyectofinal.mireparto.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "telefono", nullable = true, length = 20)
    private String telefono;
    
    @OneToMany(mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Entrega> entregas = new ArrayList<>();
    
    public Cliente() {}
    
    public Cliente(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
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
    
    public String getEmail() { 
    	return email; 
    }
    
    public void setEmail(String email) { 
    	this.email = email; 
    }
    
    public String getTelefono() { 
    	return telefono; 
    }
    
    public void setTelefono(String telefono) { 
    	this.telefono = telefono; 
    }
    
    public List<Entrega> getEntregas() {
    	return entregas; 
    }

    public void setEntregas(List<Entrega> entregas) { 
    	this.entregas = entregas; 
    }
    
    public void addEntrega(Entrega entrega) {
        entregas.add(entrega);
        entrega.setCliente(this);
    }

    public void removeEntrega(Entrega entrega) {
        entregas.remove(entrega);
        entrega.setCliente(null);
    }
    
    @Override
    public String toString() {
        return "Cliente [id=" + id + ", nombre=" + nombre + ", email=" + email 
        		+ ", telefono=" + telefono + "]";
    }
    
}
