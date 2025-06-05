package ues.pdm115.proyectopdm115grupo3.negocio // Ajusta tu paquete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.SpinnerAdapter
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentIngresarEnvioBinding
import ues.pdm115.proyectopdm115grupo3.hideKeyboard

class IngresarEnvio : Fragment() {

    private lateinit var binding : FragmentIngresarEnvioBinding
    // ViewModel para datos de dirección (Departamentos, Municipios, Distritos)
    private val direccionViewModel: DireccionViewModel by viewModels()

    // ViewModel para datos las unidades de medida (Peso, Tamaño)
    private val unidadesViewModel: UnidadesViewModel by viewModels()

    // ViewModel para registrar el envío
    private val envioViewModel: IngresarEnvioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIngresarEnvioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTexts = arrayOf(
            binding.txtNombreCliente,
            binding.txtNumeroContactoCliente,
            binding.txtUbicacionTextualCliente,
            binding.txtDetalleEnvio,
            binding.txtValorAnchoPaquete,
            binding.txtValorAltoPaquete,
            binding.txtValorLargoPaquete,
            binding.txtValorPesoPaquete
        )

        for (editText in editTexts) {
            editText.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
        }

        // Inicializar Spinners con opciones por defecto (vacías o placeholder)
        setupSpinner(binding.spinnerDepartamento, emptyList())
        setupSpinner(binding.spinnerMunicipio, emptyList())
        setupSpinner(binding.spinnerDistrito, emptyList())
        setupSpinner(binding.spinnerUnidadTamanio, emptyList())
        setupSpinner(binding.spinnerUnidadPeso, emptyList())

        // Observar LiveData de ambos ViewModels
        observeDireccionViewModel()
        observeEnvioViewModel()

        // Iniciar la carga de datos de direcciones desde el ViewModel
        direccionViewModel.fetchDirecciones()
        unidadesViewModel.fetchDirecciones()

        // Configurar la barra de herramientas
        setupToolbar()

        // Listener para el botón de enviar paquete
        binding.btnEnviarPaquete.setOnClickListener {
            // Aquí se recopilan los datos y se llama al ViewModel para registrar
            if (validateInputs()) {
                sendEnvioData()
            }
        }
    }

    private fun setupSpinner(spinner: android.widget.Spinner, items: List<Pair<Int, String>>) {
        val itemNames = items.map { it.second }.toTypedArray()
        val adapter = SpinnerAdapter(requireContext(), R.layout.spinner_item, itemNames)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun observeDireccionViewModel() {
        direccionViewModel.departamentos.observe(viewLifecycleOwner) { departamentos ->
            setupSpinner(binding.spinnerDepartamento, departamentos)
            binding.spinnerDepartamento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedPair = departamentos[position]
                    if (position > 0) { // No es la opción por defecto
                        binding.spinnerDepartamento.setBackgroundResource(R.drawable.border_searchview)
                        // Nombre seleccionado si se necesita para UI, el ID va al ViewModel
                        direccionViewModel.onDepartamentoSelected(selectedPair.first)
                    } else {
                        direccionViewModel.onDepartamentoSelected(0) // Resetea la selección en ViewModel
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    direccionViewModel.onDepartamentoSelected(0)
                }
            }
        }

        direccionViewModel.municipios.observe(viewLifecycleOwner) { municipios ->
            setupSpinner(binding.spinnerMunicipio, municipios)
            binding.spinnerMunicipio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedPair = municipios[position]
                    if (position > 0) {
                        binding.spinnerMunicipio.setBackgroundResource(R.drawable.border_searchview)
                        direccionViewModel.onMunicipioSelected(selectedPair.first)
                    } else {
                        direccionViewModel.onMunicipioSelected(0)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    direccionViewModel.onMunicipioSelected(0)
                }
            }
        }

        direccionViewModel.distritos.observe(viewLifecycleOwner) { distritos ->
            setupSpinner(binding.spinnerDistrito, distritos)
            binding.spinnerDistrito.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedPair = distritos[position]
                    if (position > 0) {
                        binding.spinnerDistrito.setBackgroundResource(R.drawable.border_searchview)
                        direccionViewModel.onDistritoSelected(selectedPair.first)
                    } else {
                        direccionViewModel.onDistritoSelected(0)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    direccionViewModel.onDistritoSelected(0)
                }
            }
        }

        // --- Observadores para Unidades de Medida (NUEVO) ---
        unidadesViewModel.unidadesTamanio.observe(viewLifecycleOwner) { unidades ->
            setupSpinner(binding.spinnerUnidadTamanio, unidades)
            binding.spinnerUnidadTamanio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedPair = unidades[position]
                    if (position > 0) {
                        binding.spinnerUnidadTamanio.setBackgroundResource(R.drawable.border_searchview)
                        unidadesViewModel.onUnidadTamanioSelected(selectedPair.first) // Pasa el ID
                    } else {
                        unidadesViewModel.onUnidadTamanioSelected(0)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    unidadesViewModel.onUnidadTamanioSelected(0)
                }
            }
        }

        unidadesViewModel.unidadesPeso.observe(viewLifecycleOwner) { unidades ->
            setupSpinner(binding.spinnerUnidadPeso, unidades)
            binding.spinnerUnidadPeso.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedPair = unidades[position]
                    if (position > 0) {
                        binding.spinnerUnidadPeso.setBackgroundResource(R.drawable.border_searchview)
                        unidadesViewModel.onUnidadPesoSelected(selectedPair.first) // Pasa el ID
                    } else {
                        unidadesViewModel.onUnidadPesoSelected(0)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    unidadesViewModel.onUnidadPesoSelected(0)
                }
            }
        }

        direccionViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
        }

        direccionViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Error al cargar direcciones: $message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observeEnvioViewModel() {
        envioViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.containerProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.containerFormularioEnvio.alpha = if (isLoading) 0.5f else 1.0f
            binding.containerFormularioEnvio.isClickable = !isLoading
            binding.containerFormularioEnvio.isFocusable = !isLoading
            setFormEnabled(!isLoading)
        }

        envioViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            lifecycleScope.launch {
                if (result != null) {
                    if (result.estado == "Exito") {
                        binding.containerMessage.visibility = View.VISIBLE
                        binding.txtMessage.text = result.mensaje
                        Toast.makeText(requireContext(), result.mensaje, Toast.LENGTH_LONG).show()
                        clearForm()
                    } else {
                        binding.containerMessage.visibility = View.VISIBLE
                        binding.txtMessage.text = result.mensaje
                        Toast.makeText(requireContext(), "Error: ${result.mensaje}", Toast.LENGTH_LONG).show()
                    }

                    delay(1500)
                    binding.containerMessage.visibility = View.GONE
                    binding.containerProgressBar.visibility = View.GONE
                }
            }
        }

        envioViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                binding.containerMessage.visibility = View.VISIBLE
                binding.txtMessage.text = message
                Toast.makeText(requireContext(), "Error de registro: $message", Toast.LENGTH_LONG).show()
            } else {
                binding.containerMessage.visibility = View.GONE
            }
        }
    }

    // Habilita o deshabilita los elementos de entrada del formulario
    private fun setFormEnabled(enabled: Boolean) {
        binding.txtNombreCliente.isEnabled = enabled
        binding.txtNumeroContactoCliente.isEnabled = enabled
        binding.txtUbicacionTextualCliente.isEnabled = enabled
        binding.spinnerDepartamento.isEnabled = enabled
        binding.spinnerMunicipio.isEnabled = enabled
        binding.spinnerDistrito.isEnabled = enabled
        binding.txtDetalleEnvio.isEnabled = enabled
        binding.txtValorAnchoPaquete.isEnabled = enabled
        binding.txtValorAltoPaquete.isEnabled = enabled
        binding.txtValorLargoPaquete.isEnabled = enabled
        binding.txtValorPesoPaquete.isEnabled = enabled
        binding.spinnerUnidadTamanio.isEnabled = enabled
        binding.spinnerUnidadPeso.isEnabled = enabled
        binding.btnEnviarPaquete.isEnabled = enabled
    }

    private fun validateInputs(): Boolean {
        // Validación básica: comprobar que los campos no estén vacíos
        if (binding.txtNombreCliente.text.isNullOrBlank() ||
            binding.txtNumeroContactoCliente.text.isNullOrBlank() ||
            binding.txtUbicacionTextualCliente.text.isNullOrBlank() ||
            binding.txtDetalleEnvio.text.isNullOrBlank() ||
            binding.txtValorAnchoPaquete.text.isNullOrBlank() ||
            binding.txtValorAltoPaquete.text.isNullOrBlank() ||
            binding.txtValorLargoPaquete.text.isNullOrBlank() ||
            binding.txtValorPesoPaquete.text.isNullOrBlank()
        ) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (direccionViewModel.selectedDepartamentoId.value == null || direccionViewModel.selectedDepartamentoId.value == 0) {
            Toast.makeText(requireContext(), "Por favor, seleccione un departamento.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (direccionViewModel.selectedMunicipioId.value == null || direccionViewModel.selectedMunicipioId.value == 0) {
            Toast.makeText(requireContext(), "Por favor, seleccione un municipio.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (direccionViewModel.selectedDistritoId.value == null || direccionViewModel.selectedDistritoId.value == 0) {
            Toast.makeText(requireContext(), "Por favor, seleccione un distrito.", Toast.LENGTH_SHORT).show()
            return false
        }

        try {
            binding.txtValorAnchoPaquete.text.toString().toDouble()
            binding.txtValorAltoPaquete.text.toString().toDouble()
            binding.txtValorLargoPaquete.text.toString().toDouble()
            binding.txtValorPesoPaquete.text.toString().toDouble()
            binding.txtNumeroContactoCliente.text.toString().toInt() // Si es un int en el backend
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Por favor, ingrese valores numéricos válidos en los campos de dimensiones, peso y número de contacto.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun sendEnvioData() {
        val longitud = 0.0 // Aquí deberías obtener la longitud real (GPS)
        val latitud = 0.0 // Aquí deberías obtener la latitud real (GPS)

        val ubicacionTextual = binding.txtUbicacionTextualCliente.text.toString()
        val detalleEnvio = binding.txtDetalleEnvio.text.toString()
        val nombreCliente = binding.txtNombreCliente.text.toString()
        val numeroTelefonoCliente = binding.txtNumeroContactoCliente.text.toString().toInt()

        val idDepartamento = direccionViewModel.selectedDepartamentoId.value ?: 0
        val idMunicipio = direccionViewModel.selectedMunicipioId.value ?: 0
        val idDistrito = direccionViewModel.selectedDistritoId.value ?: 0

        val valorAnchoPaquete = binding.txtValorAnchoPaquete.text.toString().toDouble()
        val valorAltoPaquete = binding.txtValorAltoPaquete.text.toString().toDouble()
        val valorLargoPaquete = binding.txtValorLargoPaquete.text.toString().toDouble()
        val valorPesoPaquete = binding.txtValorPesoPaquete.text.toString().toDouble()

        val idUnidadTamanio = unidadesViewModel.selectedUnidadTamanioId.value ?: 0
        val idUnidadPeso = unidadesViewModel.selectedUnidadPesoId.value ?: 0

        // Llamar al ViewModel para registrar el envío
        envioViewModel.registrar(
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
    }

    private fun setupToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        lifecycleScope.launch {
                            DataStoreManager.clearUsername(requireContext())
                            DataStoreManager.clearUserType(requireContext())
                            (requireActivity() as MainActivity).navigateToLoginGraph()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    // Función para limpiar los campos del formulario
    suspend private fun clearForm() {
        binding.txtNombreCliente.text?.clear()
        binding.txtNumeroContactoCliente.text?.clear()
        binding.txtUbicacionTextualCliente.text?.clear()
        binding.txtDetalleEnvio.text?.clear()
        binding.txtValorAnchoPaquete.text?.clear()
        binding.txtValorAltoPaquete.text?.clear()
        binding.txtValorLargoPaquete.text?.clear()
        binding.txtValorPesoPaquete.text?.clear()

        // Restablecer Spinners a la primera posición (opción por defecto)
        binding.spinnerDepartamento.setSelection(0)
        binding.spinnerMunicipio.setSelection(0)
        binding.spinnerDistrito.setSelection(0)
        binding.spinnerUnidadTamanio.setSelection(0)
        binding.spinnerUnidadPeso.setSelection(0)
    }
}

