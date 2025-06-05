package ues.pdm115.proyectopdm115grupo3.repartidor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.ApiService
import ues.pdm115.proyectopdm115grupo3.RetrofitClient
import ues.pdm115.proyectopdm115grupo3.models.UsuarioRepartidorResponse

class RegisterRepartidorViewModel : ViewModel() {

    private val _registerResult = MutableLiveData<UsuarioRepartidorResponse?>()
    val registerResult: LiveData<UsuarioRepartidorResponse?> get() = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun registrar(
        nombre: String,
        correo: String,
        contraseña: String,
        dui: String,
        primerNombre: String,
        primerApellido: String,
        telefono: Int,
        licencia: String,
        idRol: Int
    ) {
        _isLoading.value = true  // Inicia el estado de carga
        viewModelScope.launch {
            delay(1500)
            try {
                val response = api.agregarUsuariosRepartidores(nombre, correo, contraseña, dui, primerNombre, primerApellido, telefono, licencia, idRol)
                if (response.isSuccessful && response.body()?.estado == "Exito") {
                    _registerResult.value = response.body()
                }else{
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, UsuarioRepartidorResponse::class.java)
                    _registerResult.value = errorResponse
                }

            } catch (e: Exception) {
                _registerResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}