package ues.pdm115.proyectopdm115grupo3.negocio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.ApiService
import ues.pdm115.proyectopdm115grupo3.RetrofitClient
import ues.pdm115.proyectopdm115grupo3.models.Unidades

class UnidadesViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // --- Datos de Unidades de Medida ---
    private val _allUnidades = MutableLiveData<List<Unidades>>()

    private val _unidadesTamanio = MutableLiveData<List<Pair<Int, String>>>()
    val unidadesTamanio: LiveData<List<Pair<Int, String>>> = _unidadesTamanio

    private val _unidadesPeso = MutableLiveData<List<Pair<Int, String>>>()
    val unidadesPeso: LiveData<List<Pair<Int, String>>> = _unidadesPeso

    private val _selectedUnidadTamanioId = MutableLiveData<Int?>()
    val selectedUnidadTamanioId: LiveData<Int?> = _selectedUnidadTamanioId

    private val _selectedUnidadPesoId = MutableLiveData<Int?>()
    val selectedUnidadPesoId: LiveData<Int?> = _selectedUnidadPesoId

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun fetchDirecciones() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _errorMessage.postValue(null)
            try {
                // Cargar Unidades de Medida
                val unidadesResponse = api.getUnidadesDeMedida()
                if (unidadesResponse.isSuccessful) {
                    val apiUnidadesResponse = unidadesResponse.body()
                    if (apiUnidadesResponse != null && apiUnidadesResponse.estado == "Exito" && !apiUnidadesResponse.data.isNullOrEmpty()) {
                        _allUnidades.postValue(apiUnidadesResponse.data)
                        populateUnidades(apiUnidadesResponse.data)
                    } else {
                        _errorMessage.postValue(apiUnidadesResponse?.mensaje ?: "No se encontraron datos de unidades de medida.")
                        _allUnidades.postValue(emptyList())
                        populateUnidades(emptyList())
                    }
                } else {
                    _errorMessage.postValue("Error HTTP al cargar unidades: ${unidadesResponse.code()} - ${unidadesResponse.errorBody()?.string()}")
                    _allUnidades.postValue(emptyList())
                    populateUnidades(emptyList())
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red o inesperado al cargar el formulario: ${e.message}")
                _allUnidades.postValue(emptyList())
                populateUnidades(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // --- Lógica de Unidades de Medida (NUEVO) ---
    private fun populateUnidades(unidades: List<Unidades>) {
        val unidadesTamanioList = unidades
            .filter { it.tipoUnidad == "Longitud" } // O "Dimension" si tu backend usa ese tipo
            .map { Pair(it.idUnidadDeMedida, it.nombreUnidad) }
            .sortedBy { it.second }
            .toMutableList()
        unidadesTamanioList.add(0, Pair(0, "Seleccione unidad de tamaño"))
        _unidadesTamanio.postValue(unidadesTamanioList)

        val unidadesPesoList = unidades
            .filter { it.tipoUnidad == "Peso" }
            .map { Pair(it.idUnidadDeMedida, it.nombreUnidad) }
            .sortedBy { it.second }
            .toMutableList()
        unidadesPesoList.add(0, Pair(0, "Seleccione unidad de peso"))
        _unidadesPeso.postValue(unidadesPesoList)

        _selectedUnidadTamanioId.postValue(null)
        _selectedUnidadPesoId.postValue(null)
    }

    fun onUnidadTamanioSelected(id: Int) {
        _selectedUnidadTamanioId.value = id
    }

    fun onUnidadPesoSelected(id: Int) {
        _selectedUnidadPesoId.value = id
    }
}