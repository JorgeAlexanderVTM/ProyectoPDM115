package ues.pdm115.proyectopdm115grupo3.models

import com.google.gson.annotations.SerializedName

data class Envio(
    // Datos del punto de entrega
    @SerializedName("longitud") val longitud: Double,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("ubicacion_textual") val ubicacionTextual: String,

    // Datos del envío
    @SerializedName("detalle_envio") val detalleEnvio: String,

    // Datos del detalle del envío
    @SerializedName("nombre_remitente") val nombreRemitente: String,
    @SerializedName("nombre_destinatario") val nombreDestinatario: String,
    @SerializedName("numero_telefono_destinatario") val numeroTelefonoDestinatario: Int,
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

data class DetalleEnvio(
    // Datos del punto de entrega
    @SerializedName("longitud") val longitud: Double,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("ubicacion_textual") val ubicacionTextual: String,

    // Datos del envío
    @SerializedName("id_envio") val idEnvio: Int,
    @SerializedName("id_punto_entrega") val idPuntoEntrega: Int,
    @SerializedName("detalle_envio") val detalleEnvio: String,
    @SerializedName("codigo_seguro") val codigoSeguimiento: String,
    @SerializedName("numero_seguimiento") val numeroSeguimiento: String,
    @SerializedName("longitud_reparidor") val longitudRepartidor: Double,
    @SerializedName("latitud_reparidor") val latitudRepartidor: Double,

    // Datos del detalle del envío
    @SerializedName("id_remitente") val idRemitente: Int,
    @SerializedName("nombre_remitente") val nombreRemitente: String,
    @SerializedName("id_destinatario") val idDestinatario: Int,
    @SerializedName("nombre_destinatario") val nombreDestinatario: String,
    @SerializedName("id_repartidor") val idRepartidor: Int,
    @SerializedName("nombre_repartidor") val nombreRepartidor: String,
    @SerializedName("numero_telefono_destinatario") val numeroTelefonoDestinatario: Int,

)

data class UpdateRepartidorRequest(
    @SerializedName("id_repartidor") val idRepartidor: Int,
    @SerializedName("id_envio") val idEnvio: Int
)

data class UpdateRepartidorResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: UpdateRepartidorRequest
)

data class EnvioResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: Envio
)

data class AllEnviosResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: List<DetalleEnvio>
)
