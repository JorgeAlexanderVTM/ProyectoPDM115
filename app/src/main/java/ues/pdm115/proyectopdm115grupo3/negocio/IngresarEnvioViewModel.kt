package ues.pdm115.proyectopdm115grupo3.negocio // Ajusta tu paquete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ues.pdm115.proyectopdm115grupo3.ApiService
import ues.pdm115.proyectopdm115grupo3.RetrofitClient
import ues.pdm115.proyectopdm115grupo3.models.EnvioResponse
import java.io.IOException

class IngresarEnvioViewModel() : ViewModel() {

    private val _registerResult = MutableLiveData<EnvioResponse?>()
    val registerResult: LiveData<EnvioResponse?> get() = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun registrar(
        longitud: Double,
        latitud: Double,
        ubicacionTextual: String,
        detalleEnvio: String,
        nombreCliente: String,
        numeroTelefonoCliente: Int,
        idDepartamento: Int,
        idMunicipio: Int,
        idDistrito: Int,
        valorAnchoPaquete: Double,
        valorAltoPaquete: Double,
        valorLargoPaquete: Double,
        valorPesoPaquete: Double,
        idUnidadTamanio: Int,
        idUnidadPeso: Int
    ) {
        _isLoading.value = true // Inicia el estado de carga
        _errorMessage.value = null // Limpiar errores anteriores
        viewModelScope.launch {
            delay(500) // Pequeño retraso para que el ProgressBar sea visible
            try {
                // Llamada al método registerEnvio de la ApiService con todos los campos
                val response = api.agregarEnvio(
                    longitud,
                    latitud,
                    ubicacionTextual,
                    detalleEnvio,
                    nombreCliente,
                    numeroTelefonoCliente,
                    idDepartamento,
                    idMunicipio,
                    idDistrito,
                    valorAnchoPaquete,
                    valorAltoPaquete,
                    valorLargoPaquete,
                    valorPesoPaquete,
                    idUnidadTamanio,
                    idUnidadPeso
                )

                if (response.isSuccessful) {
                    _registerResult.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    try {
                        val errorResponse = gson.fromJson(errorBody, EnvioResponse::class.java)
                        _registerResult.value = errorResponse
                        _errorMessage.value = errorResponse?.mensaje ?: "Error desconocido en el servidor."
                    } catch (e: JsonSyntaxException) {
                        _errorMessage.value = "Error al parsear la respuesta de error del servidor."
                        _registerResult.value = null
                    }
                }
            } catch (e: HttpException) {
                _errorMessage.value = "Error HTTP: ${e.code()} - ${e.message()}"
                _registerResult.value = null
            } catch (e: IOException) {
                _errorMessage.value = "Error de red: ${e.message}"
                _registerResult.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
                _registerResult.value = null
            } finally {
                _isLoading.value = false // Finaliza el estado de carga
            }
        }
    }
}