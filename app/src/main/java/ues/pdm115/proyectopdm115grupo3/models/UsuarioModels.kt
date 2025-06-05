package ues.pdm115.proyectopdm115grupo3.models

import com.google.gson.annotations.SerializedName

data class UsuarioComprador(
    @SerializedName("nombre_usuario")
    val nombreUsuario: String,
    @SerializedName("email_usuario")
    val correo: String,
    @SerializedName("contrasena_usuario")
    val contraseña: String,
    @SerializedName("documento_de_entidad")
    val dui: String,
    @SerializedName("primer_nombre")
    val primerNombre: String,
    @SerializedName("primer_apellido")
    val primerApellido: String,
    @SerializedName("telefono_movil_usuario")
    val telefono: String,
    @SerializedName("id_rol")
    val idRol: Int = 1
)

data class UsuarioRepartidor(
    @SerializedName("nombre_usuario")
    val nombreUsuario: String,
    @SerializedName("email_usuario")
    val correo: String,
    @SerializedName("contrasena_usuario")
    val contraseña: String,
    @SerializedName("documento_de_entidad")
    val dui: String,
    @SerializedName("licencia_usuario")
    val licenciaUsuario: String,
    @SerializedName("primer_nombre")
    val primerNombre: String,
    @SerializedName("primer_apellido")
    val primerApellido: String,
    @SerializedName("telefono_movil_usuario")
    val telefono: String,
    @SerializedName("id_rol")
    val idRol: Int = 2
)

data class UsuarioNegocio(
    @SerializedName("nombre_usuario")
    val nombreUsuario: String,
    @SerializedName("email_usuario")
    val correo: String,
    @SerializedName("contrasena_usuario")
    val contraseña: String,
    @SerializedName("nombre_negocio")
    val nombreNegocio: String,
    @SerializedName("correo_del_negocio")
    val correoNegocio: String,
    @SerializedName("contacto1_negocio")
    val contacto1Negocio: String,
    @SerializedName("id_rol")
    val idRol: Int = 3
)

data class UsuarioCompradorResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: UsuarioComprador
)

data class UsuarioRepartidorResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: UsuarioRepartidor
)

data class UsuarioNegocioResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: UsuarioNegocio
)

data class UsuarioAuth(
    @SerializedName("id_usuario") val id: Int,
    @SerializedName("nombre_usuario") val nombre: String,
    @SerializedName("nombre_rol") val nombre_rol: String
)

data class UsuarioAuthResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: UsuarioAuth
)
