package ues.pdm115.proyectopdm115grupo3.models

import com.google.gson.annotations.SerializedName

data class Unidades(
    @SerializedName("id_unidad_de_medida") val idUnidadDeMedida: Int,
    @SerializedName("nombre_unidad") val nombreUnidad: String,
    @SerializedName("tipo_unidad") val tipoUnidad: String,
    @SerializedName("factor_convercion") val factorConversion: String
)

data class UnidadesResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: List<Unidades>?
)
