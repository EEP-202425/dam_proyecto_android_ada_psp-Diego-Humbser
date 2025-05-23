package dam.proyectofinal.mireparto.repository;

import dam.proyectofinal.mireparto.domain.Cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
