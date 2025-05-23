package dam.proyectofinal.mireparto.controller;

import dam.proyectofinal.mireparto.service.ClienteService;
import dam.proyectofinal.mireparto.dto.ClienteDto;
import dam.proyectofinal.mireparto.security.JwtAuthenticationFilter;
import dam.proyectofinal.mireparto.security.JwtUtils;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {
	
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private ClienteService service;
    
	@MockitoBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockitoBean
	private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/clientes devuelve lista de clientes")
    void listarTodosDevuelveListaClientes() throws Exception {
        List<ClienteDto> lista = Arrays.asList(
                new ClienteDto(1L, "Juan", "juan@mail.com", "111111111"),
                new ClienteDto(2L, "Ana", "ana@mail.com", "222222222")
        );
        Mockito.when(service.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[1].nombre", is("Ana")));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} devuelve un cliente")
    void obtenerPorIdDevuelveCliente() throws Exception {
        ClienteDto cliente = new ClienteDto(1L, "Juan", "juan@mail.com", "111111111");
        Mockito.when(service.obtenerPorId(1L)).thenReturn(cliente);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.email", is("juan@mail.com")));
    }

    @Test
    @DisplayName("POST /api/clientes crea y devuelve cliente")
    void crearClienteDevuelveCreado() throws Exception {
        ClienteDto input = new ClienteDto(null, "Pepe", "pepe@mail.com", "333333333");
        ClienteDto creado = new ClienteDto(10L, "Pepe", "pepe@mail.com", "333333333");
        Mockito.when(service.crear(any(ClienteDto.class))).thenReturn(creado);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/clientes/10")))
                .andExpect(jsonPath("$.nombre", is("Pepe")))
                .andExpect(jsonPath("$.id", is(10)));
    }

    @Test
    @DisplayName("PUT /api/clientes/{id} actualiza y devuelve cliente")
    void actualizarClienteDevuelveActualizado() throws Exception {
        ClienteDto input = new ClienteDto(null, "Juan", "nuevo@mail.com", "999999999");
        ClienteDto actualizado = new ClienteDto(1L, "Juan", "nuevo@mail.com", "999999999");
        Mockito.when(service.actualizar(eq(1L), any(ClienteDto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("nuevo@mail.com")));
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} elimina cliente")
    void eliminarClienteNoContent() throws Exception {
        Mockito.doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }
}
