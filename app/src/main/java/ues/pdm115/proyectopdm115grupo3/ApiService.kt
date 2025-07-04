package ues.pdm115.proyectopdm115grupo3

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ues.pdm115.proyectopdm115grupo3.models.AllEnviosResponse
import ues.pdm115.proyectopdm115grupo3.models.AllUsuarioRolResponse
import ues.pdm115.proyectopdm115grupo3.models.DireccionResponse
import ues.pdm115.proyectopdm115grupo3.models.EnvioResponse
import ues.pdm115.proyectopdm115grupo3.models.UnidadesResponse
import ues.pdm115.proyectopdm115grupo3.models.UpdateRepartidorRequest
import ues.pdm115.proyectopdm115grupo3.models.UpdateRepartidorResponse
import ues.pdm115.proyectopdm115grupo3.models.UsuarioAuthResponse
import ues.pdm115.proyectopdm115grupo3.models.UsuarioCompradorResponse
import ues.pdm115.proyectopdm115grupo3.models.UsuarioNegocioResponse
import ues.pdm115.proyectopdm115grupo3.models.UsuarioRepartidorResponse

interface ApiService {

    @FormUrlEncoded
    @POST("usuarios")
    suspend fun agregarUsuariosCompradores(
        @Field("nombre_usuario") nombre: String,
        @Field("email_usuario") correo: String,
        @Field("contrasena_usuario") contrasena: String,
        @Field("documento_de_entidad") documento: String,
        @Field("primer_nombre") primerNombre: String,
        @Field("primer_apellido") primerApellido: String,
        @Field("telefono_movil_usuario") telefono: Int,
        @Field("id_rol") idRol: Int
    ): Response<UsuarioCompradorResponse>

    @FormUrlEncoded
    @POST("usuarios")
    suspend fun agregarUsuariosRepartidores(
        @Field("nombre_usuario") nombre: String,
        @Field("email_usuario") correo: String,
        @Field("contrasena_usuario") contrasena: String,
        @Field("documento_de_entidad") documento: String,
        @Field("primer_nombre") primerNombre: String,
        @Field("primer_apellido") primerApellido: String,
        @Field("telefono_movil_usuario") telefono: Int,
        @Field("licencia_usuario") licencia: String,
        @Field("id_rol") idRol: Int
    ): Response<UsuarioRepartidorResponse>

    @FormUrlEncoded
    @POST("usuarios")
    suspend fun agregarUsuariosNegocio(
        @Field("nombre_usuario") nombre: String,
        @Field("email_usuario") correo: String,
        @Field("contrasena_usuario") contrasena: String,
        @Field("nombre_negocio") nombreNegocio: String,
        @Field("correo_del_negocio") correoNegocio: String,
        @Field("contacto1_negocio") contactoNegocio: Int,
        @Field("id_rol") idRol: Int
    ): Response<UsuarioNegocioResponse>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun autentificarUsuario(
        @Field("nombre_usuario") nombre: String,
        @Field("contrasena_usuario") contraseña: String,
    ): Response<UsuarioAuthResponse>

    @GET("direcciones")
    suspend fun getDirecciones(): Response<DireccionResponse>

    @FormUrlEncoded // Indica que los datos se enviarán como application/x-www-form-urlencoded
    @POST("envios") // Asume que este es el endpoint para registrar envíos
    suspend fun agregarEnvio(
        @Field("longitud") longitud: Double,
        @Field("latitud") latitud: Double,
        @Field("ubicacion_textual") ubicacionTextual: String,
        @Field("detalle_envio") detalleEnvio: String,
        @Field("nombre_remitente") nombreRemitente: String,
        @Field("nombre_destinatario") nombreDestinatario: String,
        @Field("numero_telefono_destinatario") numeroTelefonoDestinatario: Int,
        @Field("id_departamento") idDepartamento: Int,
        @Field("id_municipio") idMunicipio: Int,
        @Field("id_distrito") idDistrito: Int,
        @Field("valor_ancho_paquete") valorAnchoPaquete: Double,
        @Field("valor_alto_paquete") valorAltoPaquete: Double,
        @Field("valor_largo_paquete") valorLargoPaquete: Double,
        @Field("valor_peso_paquete") valorPesoPaquete: Double,
        @Field("id_unidad_tamanio") idUnidadTamanio: Int,
        @Field("id_unidad_peso") idUnidadPeso: Int
    ): Response<EnvioResponse>

    @GET("unidades")
    suspend fun getUnidadesDeMedida(): Response<UnidadesResponse>

    @GET("envios")
    suspend fun getDetalleEnvios(): Response<AllEnviosResponse>

    @GET("envios/{id_remitente}")
    suspend fun getDetalleEnviosId(
        @Path("id_remitente") idRemitente: Int
    ): Response<AllEnviosResponse>

    @GET("envios/numeroseguimiento/{numero_seguimiento}")
    suspend fun getDetalleEnviosNumeroSeguimiento(
        @Path("numero_seguimiento") numeroSeguimiento: String
    ): Response<AllEnviosResponse>


    @GET("envios/nombre/{nombre_remitente}")
    suspend fun getDetalleEnviosNombre(
        @Path("nombre_remitente") nombreRemitente: String
    ): Response<AllEnviosResponse>

    @GET("usuarios/rol/{nombre_rol}")
    suspend fun getUsuariosByRoles(
        @Path("nombre_rol") nombreRol: String
    ): Response<AllUsuarioRolResponse>

    @PUT("envios/repartidor")
    suspend fun actualizarRepartidor(
        @Body request: UpdateRepartidorRequest
    ): Response<UpdateRepartidorResponse>

}