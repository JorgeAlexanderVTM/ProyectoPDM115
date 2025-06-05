package ues.pdm115.proyectopdm115grupo3.negocio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.ApiService
import ues.pdm115.proyectopdm115grupo3.RetrofitClient
import ues.pdm115.proyectopdm115grupo3.models.DireccionData

class DireccionViewModel: ViewModel()
{
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // --- Datos de Ubicación ---
    private val _allDirecciones = MutableLiveData<List<DireccionData>>()

    private val _departamentos = MutableLiveData<List<Pair<Int, String>>>()
    val departamentos: LiveData<List<Pair<Int, String>>> = _departamentos

    private val _municipios = MutableLiveData<List<Pair<Int, String>>>()
    val municipios: LiveData<List<Pair<Int, String>>> = _municipios

    private val _distritos = MutableLiveData<List<Pair<Int, String>>>()
    val distritos: LiveData<List<Pair<Int, String>>> = _distritos

    private val _selectedDepartamentoId = MutableLiveData<Int?>()
    val selectedDepartamentoId: LiveData<Int?> = _selectedDepartamentoId

    private val _selectedMunicipioId = MutableLiveData<Int?>()
    val selectedMunicipioId: LiveData<Int?> = _selectedMunicipioId

    private val _selectedDistritoId = MutableLiveData<Int?>()
    val selectedDistritoId: LiveData<Int?> = _selectedDistritoId

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun fetchDirecciones() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _errorMessage.postValue(null)
            try {
                // Cargar Direcciones
                val dirResponse = api.getDirecciones()
                if (dirResponse.isSuccessful) {
                    val apiDirResponse = dirResponse.body()
                    if (apiDirResponse != null && apiDirResponse.estado == "Exito" && !apiDirResponse.data.isNullOrEmpty()) {
                        _allDirecciones.postValue(apiDirResponse.data)
                        populateDepartamentos(apiDirResponse.data)
                    } else {
                        _errorMessage.postValue(apiDirResponse?.mensaje ?: "No se encontraron datos de dirección.")
                        _allDirecciones.postValue(emptyList())
                        populateDepartamentos(emptyList())
                    }
                } else {
                    _errorMessage.postValue("Error HTTP al cargar direcciones: ${dirResponse.code()} - ${dirResponse.errorBody()?.string()}")
                    _allDirecciones.postValue(emptyList())
                    populateDepartamentos(emptyList())
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red o inesperado al cargar el formulario: ${e.message}")
                _allDirecciones.postValue(emptyList())
                populateDepartamentos(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // --- Lógica de Ubicación (se mantiene) ---
    private fun populateDepartamentos(direcciones: List<DireccionData>) {
        val uniqueDepartamentos = direcciones
            .map { Pair(it.idDepartamento, it.nombreDepartamento) }
            .distinct()
            .sortedBy { it.second }
            .toMutableList()

        uniqueDepartamentos.add(0, Pair(0, "Seleccione un Departamento"))
        _departamentos.postValue(uniqueDepartamentos)

        _selectedDepartamentoId.postValue(null)
        _selectedMunicipioId.postValue(null)
        _selectedDistritoId.postValue(null)
        _municipios.postValue(listOf(Pair(0, "Seleccione un Municipio")))
        _distritos.postValue(listOf(Pair(0, "Seleccione un Distrito")))
    }

    fun onDepartamentoSelected(id: Int) {
        _selectedDepartamentoId.value = id
        _selectedMunicipioId.value = null
        _selectedDistritoId.value = null

        val all = _allDirecciones.value ?: emptyList()
        if (id == 0) {
            _municipios.postValue(listOf(Pair(0, "Seleccione un Municipio")))
            _distritos.postValue(listOf(Pair(0, "Seleccione un Distrito")))
        } else {
            val filteredMunicipios = all
                .filter { it.idDepartamento == id }
                .map { Pair(it.idMunicipio, it.nombreMunicipio) }
                .distinct()
                .sortedBy { it.second }
                .toMutableList()
            filteredMunicipios.add(0, Pair(0, "Seleccione un Municipio"))
            _municipios.postValue(filteredMunicipios)
            _distritos.postValue(listOf(Pair(0, "Seleccione un Distrito")))
        }
    }

    fun onMunicipioSelected(id: Int) {
        _selectedMunicipioId.value = id
        _selectedDistritoId.value = null

        val all = _allDirecciones.value ?: emptyList()
        val selectedDeptId = _selectedDepartamentoId.value
        if (id == 0 || selectedDeptId == null) {
            _distritos.postValue(listOf(Pair(0, "Seleccione un Distrito")))
        } else {
            val filteredDistritos = all
                .filter { it.idDepartamento == selectedDeptId && it.idMunicipio == id }
                .map { Pair(it.idDistrito, it.nombreDistrito) }
                .distinct()
                .sortedBy { it.second }
                .toMutableList()
            filteredDistritos.add(0, Pair(0, "Seleccione un Distrito"))
            _distritos.postValue(filteredDistritos)
        }
    }

    fun onDistritoSelected(id: Int) {
        _selectedDistritoId.value = id
    }
}