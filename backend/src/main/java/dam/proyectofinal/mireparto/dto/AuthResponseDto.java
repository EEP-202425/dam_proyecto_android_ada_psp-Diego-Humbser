package dam.proyectofinal.mireparto.dto;

public class AuthResponseDto {

    private String token;
    private Long userId;
    private String nombre;

    public AuthResponseDto() {
    }

    public AuthResponseDto(String token, Long userId, String nombre) {
        this.token = token;
        this.userId = userId;
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
