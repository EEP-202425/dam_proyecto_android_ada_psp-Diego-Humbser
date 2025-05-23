package dam.proyectofinal.mireparto.security;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;

import jakarta.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private static final long DEFAULT_EXPIRATION_MS = 86400000;
    private SecretKey signingKey;
    
    @PostConstruct
    public void init() {
        this.signingKey = Jwts.SIG.HS256.key().build();
    }

    // Genera un token JWT para el usuario dado
    
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION_MS))
                .signWith(signingKey)
                .compact();
    }
    
    // Extrae el email (subject) del token
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Valida el token contra los detalles del usuario
    
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

     // Extrae cualquier claim usando una función
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
