package ues.pdm115.proyectopdm115grupo3.comprador

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.ApiService
import ues.pdm115.proyectopdm115grupo3.RetrofitClient
import ues.pdm115.proyectopdm115grupo3.models.AllEnviosResponse

class LocatePackageUserViewModel : ViewModel() {

    private val _responseResult = MutableLiveData<AllEnviosResponse?>()
    val responseResult: LiveData<AllEnviosResponse?> get() = _responseResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun verEnviosNumeroSeguimiento(numeroSeguimiento: String) {
        _isLoading.value = true  // Inicia el estado de carga
        viewModelScope.launch {
            delay(1500)
            try {
                val response = api.getDetalleEnviosNumeroSeguimiento(numeroSeguimiento)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.estado == "Exito" && !responseBody.data.isNullOrEmpty()) {
                        _responseResult.value = responseBody
                    } else{
                        _errorMessage.postValue(responseBody?.mensaje?: "No se encontraron datos.")
                    }
                }else{
                    _errorMessage.postValue("Error HTTP al cargar los envios: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red o inesperado al cargar el formulario: ${e.message}")
                _responseResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}