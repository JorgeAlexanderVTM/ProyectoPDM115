package ues.pdm115.proyectopdm115grupo3.models

import com.google.gson.annotations.SerializedName

data class DireccionData(
    @SerializedName("id_departamento") val idDepartamento: Int,
    @SerializedName("nombre_departamento") val nombreDepartamento: String,
    @SerializedName("id_municipio") val idMunicipio: Int,
    @SerializedName("nombre_municipio") val nombreMunicipio: String,
    @SerializedName("id_distrito") val idDistrito: Int,
    @SerializedName("nombre_distrito") val nombreDistrito: String
)

data class DireccionResponse(
    @SerializedName("state") val estado: String,
    @SerializedName("message") val mensaje: String,
    @SerializedName("data") val data: List<DireccionData>? // La lista de objetos DireccionData
)
