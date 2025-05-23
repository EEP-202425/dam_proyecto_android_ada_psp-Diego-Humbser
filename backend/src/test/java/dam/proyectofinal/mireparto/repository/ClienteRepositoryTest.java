package dam.proyectofinal.mireparto.repository;

import dam.proyectofinal.mireparto.domain.Cliente;
import dam.proyectofinal.mireparto.repository.ClienteRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Guardar un cliente y buscar por ID")
    void guardarYBuscarCliente() {
        Cliente cliente = new Cliente("Juan Perez", "juan@mail.com", "666111222");
        Cliente guardado = clienteRepository.save(cliente);

        Optional<Cliente> resultado = clienteRepository.findById(guardado.getId());
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Juan Perez");
        assertThat(resultado.get().getEmail()).isEqualTo("juan@mail.com");
    }

    @Test
    @DisplayName("Actualizar un cliente existente")
    void actualizarCliente() {
        Cliente cliente = new Cliente("Ana", "ana@mail.com", "777888999");
        Cliente guardado = clienteRepository.save(cliente);

        guardado.setNombre("Ana Modificado");
        clienteRepository.save(guardado);

        Cliente actualizado = clienteRepository.findById(guardado.getId()).orElseThrow();
        assertThat(actualizado.getNombre()).isEqualTo("Ana Modificado");
    }

    @Test
    @DisplayName("Eliminar un cliente")
    void eliminarCliente() {
        Cliente cliente = new Cliente("Carlos", "carlos@mail.com", "222333444");
        Cliente guardado = clienteRepository.save(cliente);

        clienteRepository.deleteById(guardado.getId());
        Optional<Cliente> resultado = clienteRepository.findById(guardado.getId());
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Buscar todos los clientes")
    void buscarTodos() {
        clienteRepository.save(new Cliente("A", "a@mail.com", "111"));
        clienteRepository.save(new Cliente("B", "b@mail.com", "222"));

        var lista = clienteRepository.findAll();
        assertThat(lista).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Buscar por email")
    void buscarPorEmail() {
        Cliente cliente = new Cliente("Eva", "eva@mail.com", "333444555");
        clienteRepository.save(cliente);

        Optional<Cliente> resultado = clienteRepository.findByEmail("eva@mail.com");
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Eva");
    }
}
