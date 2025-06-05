package ues.pdm115.proyectopdm115grupo3.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.ApiService
import ues.pdm115.proyectopdm115grupo3.RetrofitClient
import ues.pdm115.proyectopdm115grupo3.models.UsuarioAuthResponse

class LoginUserViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<UsuarioAuthResponse?>()
    val loginResult: LiveData<UsuarioAuthResponse?> get() = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun login(nombre: String, contraseña: String) {
        _isLoading.value = true  // Inicia el estado de carga
        viewModelScope.launch {
            delay(1500)
            try {
                val response = api.autentificarUsuario(nombre, contraseña)

                if (response.isSuccessful && response.body()?.estado == "Exito") {
                    _loginResult.value = response.body()
                }else{
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, UsuarioAuthResponse::class.java)
                    _loginResult.value = errorResponse
                }

            } catch (e: Exception) {
                _loginResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}