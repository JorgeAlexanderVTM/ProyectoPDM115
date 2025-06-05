package ues.pdm115.proyectopdm115grupo3.models

import com.google.gson.annotations.SerializedName

data class Envio(
    // Datos del punto de entrega
    @SerializedName("longitud") val longitud: Double,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("ubicacion_textual") val ubicacionTextual: String,

    // Datos del envío
    @SerializedName("detalle_envio") val detalleEnvio: String,

    // Datos del detalle del envío (cliente)
    @SerializedName("nombre_cliente") val nombreCliente: String,
    @SerializedName("numero_telefono_cliente") val numeroTelefonoCliente: Int,
    @SerializedName("id_departamento") val idDepartamento: Int,
    @SerializedName("id_municipio") val idMunicipio: Int,
    @SerializedName("id_distrito") val idDistrito: Int,

    // Datos del paquete
    @SerializedName("valor_ancho_paquete") val valorAnchoPaquete: Double,
    @SerializedName("valor_alto_paquete") val valorAltoPaquete: Double,
    @SerializedName("valor_largo_paquete") val valorLargoPaquete: Double,
    @SerializedName("valor_peso_paquete") val valorPesoPaquete: Double,
    @SerializedName("id_unidad_tamanio") val idUnidadTamanio: Int,
    @SerializedName("id_unidad_peso") val idUnidadPeso: Int
)

data class EnvioResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: Envio
)
