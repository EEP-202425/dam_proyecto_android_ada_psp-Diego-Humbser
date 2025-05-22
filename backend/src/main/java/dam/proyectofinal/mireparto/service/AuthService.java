package dam.proyectofinal.mireparto.service;

import dam.proyectofinal.mireparto.domain.User;
import dam.proyectofinal.mireparto.repository.UserRepository;
import dam.proyectofinal.mireparto.dto.AuthResponseDto;
import dam.proyectofinal.mireparto.dto.LoginRequestDto;
import dam.proyectofinal.mireparto.dto.RegisterRequestDto;
import dam.proyectofinal.mireparto.security.JwtUtils;

import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    @Autowired
    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }
    
    // Registra un nuevo usuario y devuelve un token JWT

    @Transactional
    public AuthResponseDto register(@Valid RegisterRequestDto dto) {
        // Verificar que el email no exista
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        // Crear entidad User
        User user = new User();
        user.setNombre(dto.getNombre());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of("ROLE_USER"));

        // Guardar en BD
        user = userRepository.save(user);

        // Generar token
        String token = jwtUtils.generateToken(user);
        return new AuthResponseDto(token, user.getId(), user.getNombre());
    }
    
	// Autentica al usuario y devuelve un token JWT
    
    public AuthResponseDto login(@Valid LoginRequestDto dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            User user = (User) auth.getPrincipal();
            String token = jwtUtils.generateToken(user);
            return new AuthResponseDto(token, user.getId(), user.getNombre());
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }
    
}
