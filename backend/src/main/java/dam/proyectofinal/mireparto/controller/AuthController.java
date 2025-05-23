package dam.proyectofinal.mireparto.controller;

import dam.proyectofinal.mireparto.dto.AuthResponseDto;
import dam.proyectofinal.mireparto.dto.LoginRequestDto;
import dam.proyectofinal.mireparto.dto.RegisterRequestDto;
import dam.proyectofinal.mireparto.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Registra un nuevo usuario y devuelve un JWT
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto dto) {
        AuthResponseDto response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

	// Autentica un usuario existente y devuelve un JWT
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto creds) {
        AuthResponseDto response = authService.login(creds);
        return ResponseEntity.ok(response);
    }
    
}
