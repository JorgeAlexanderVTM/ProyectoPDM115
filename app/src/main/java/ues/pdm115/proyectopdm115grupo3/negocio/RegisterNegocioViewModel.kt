package ues.pdm115.proyectopdm115grupo3.negocio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.ApiService
import ues.pdm115.proyectopdm115grupo3.RetrofitClient
import ues.pdm115.proyectopdm115grupo3.models.UsuarioNegocioResponse

class RegisterNegocioViewModel : ViewModel() {

    private val _registerResult = MutableLiveData<UsuarioNegocioResponse?>()
    val registerResult: LiveData<UsuarioNegocioResponse?> get() = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun registrar(
        nombre: String,
        correo: String,
        contraseña: String,
        nombreNegocio: String,
        correoNegocio: String,
        contactoNegocio: Int,
        idRol: Int
    ) {
        _isLoading.value = true  // Inicia el estado de carga
        viewModelScope.launch {
            delay(1500)
            try {
                val response = api.agregarUsuariosNegocio(nombre, correo, contraseña, nombreNegocio, correoNegocio, contactoNegocio, idRol)
                if (response.isSuccessful && response.body()?.estado == "Exito") {
                    _registerResult.value = response.body()
                }else{
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, UsuarioNegocioResponse::class.java)
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