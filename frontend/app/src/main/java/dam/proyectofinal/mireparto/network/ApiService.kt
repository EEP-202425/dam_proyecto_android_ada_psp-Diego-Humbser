package dam.proyectofinal.mireparto.network

import dam.proyectofinal.mireparto.model.AuthResponseDto
import dam.proyectofinal.mireparto.model.ClienteDto
import dam.proyectofinal.mireparto.model.EntregaDto
import dam.proyectofinal.mireparto.model.LoginRequestDto
import dam.proyectofinal.mireparto.model.RegisterRequestDto
import dam.proyectofinal.mireparto.model.VehiculoDto
import dam.proyectofinal.mireparto.model.ZonaDto

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("entregas")
    suspend fun getEntregas(): List<EntregaDto>

    @POST("entregas")
    suspend fun createEntrega(@Body entrega: EntregaDto): EntregaDto

    @GET("clientes")
    suspend fun getClientes(): List<ClienteDto>

    @GET("vehiculos")
    suspend fun getVehiculos(): List<VehiculoDto>

    @GET("zonas")
    suspend fun getZonas(): List<ZonaDto>

    @GET("entregas/{id}")
    suspend fun getEntrega(@Path("id") id: Long): EntregaDto

    @PUT("entregas/{id}")
    suspend fun updateEntrega(@Path("id") id: Long, @Body entrega: EntregaDto): EntregaDto

    @DELETE("entregas/{id}")
    suspend fun deleteEntrega(@Path("id") id: Long)

    @POST("auth/login")
    suspend fun login(@Body creds: LoginRequestDto): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body info: RegisterRequestDto): AuthResponseDto
}