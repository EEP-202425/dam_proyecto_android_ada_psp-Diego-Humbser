package dam.proyectofinal.mireparto.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dam.proyectofinal.mireparto.domain.Cliente;
import dam.proyectofinal.mireparto.domain.Vehiculo;
import dam.proyectofinal.mireparto.domain.Zona;
import dam.proyectofinal.mireparto.repository.ClienteRepository;
import dam.proyectofinal.mireparto.repository.VehiculoRepository;
import dam.proyectofinal.mireparto.repository.ZonaRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
        ClienteRepository clienteRepository,
        VehiculoRepository vehiculoRepository,
        ZonaRepository zonaRepository
    ) {
        return args -> {
            // Poblar algunos clientes
            if (clienteRepository.count() == 0) {
                clienteRepository.save(new Cliente("Juan Perez", "juan@mail.com", "666111222"));
                clienteRepository.save(new Cliente("María López", "maria@mail.com", "666222333"));
                clienteRepository.save(new Cliente("Carlos Gómez", "carlos@mail.com", null));
                clienteRepository.save(new Cliente("Ana Ruiz", "ana@mail.com", "654333111"));
            }
            // Poblar algunos vehículos
            if (vehiculoRepository.count() == 0) {
                vehiculoRepository.save(new Vehiculo("1234ABC", "Furgoneta", 500.0));
                vehiculoRepository.save(new Vehiculo("5678DEF", "Moto", 50.0));
                vehiculoRepository.save(new Vehiculo("2468GHI", "Bicicleta", 15.0));
                vehiculoRepository.save(new Vehiculo("1357JKL", "Furgoneta", 600.0));
            }
            // Poblar algunas zonas
            if (zonaRepository.count() == 0) {
                zonaRepository.save(new Zona("Centro", "28001"));
                zonaRepository.save(new Zona("Norte", "28020"));
                zonaRepository.save(new Zona("Sur", "28030"));
                zonaRepository.save(new Zona("Este", "28040"));
                zonaRepository.save(new Zona("Oeste", "28050"));
            }
        };
    }
}
