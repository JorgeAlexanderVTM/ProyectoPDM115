package ues.pdm115.proyectopdm115grupo3.repartidor

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentRegisterRepartidorBinding
import ues.pdm115.proyectopdm115grupo3.hideKeyboard
import kotlin.getValue

class RegisterRepartidor : Fragment() {

    private var _binding: FragmentRegisterRepartidorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterRepartidorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterRepartidorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTexts = arrayOf(
            binding.txtNombreUsuario,
            binding.txtNumeroTel,
            binding.txtDUI,
            binding.txtCorreo,
            binding.txtContrasena,
            binding.txtRepetirContrasena,
            binding.txtPrimerNombre,
            binding.txtPrimerApellido,
            binding.txtLicenciaConducir
        )

        for (editText in editTexts) {
            editText.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
        }

        binding.btnCancelar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.txtNombreUsuario.text.toString()
            val correo = binding.txtCorreo.text.toString()
            val contrasena = binding.txtContrasena.text.toString()
            val repetirContrasena = binding.txtRepetirContrasena.text.toString()
            val telefono = binding.txtNumeroTel.text.toString()
            val dui = binding.txtDUI.text.toString()
            val primerNombre = binding.txtPrimerNombre.text.toString()
            val primerApellido = binding.txtPrimerApellido.text.toString()
            val licencia = binding.txtLicenciaConducir.text.toString()
            val rol = 2

            hideKeyboard()

            if (nombre.isNotBlank() && correo.isNotBlank() &&
                contrasena.isNotBlank() && repetirContrasena.isNotBlank() &&
                telefono.isNotBlank() && dui.isNotBlank() &&
                primerNombre.isNotBlank() && primerApellido.isNotBlank()
                && licencia.isNotBlank())
            {

                if (contrasena == repetirContrasena){
                    viewModel.registrar(nombre, correo, contrasena, dui, primerNombre, primerApellido, telefono.toInt(), licencia, rol)

                }else{
                    Toast.makeText(requireContext(), "La contraseña no coincide", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "Campos requeridos", Toast.LENGTH_SHORT).show()
            }
        }

        // Observar el estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                // Mostrar barra de progreso
                binding.containerProgressBar.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
                binding.containerFormulario.visibility = View.GONE
            }else{
                binding.containerFormulario.visibility = View.VISIBLE
                binding.containerProgressBar.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.registerResult.observe(viewLifecycleOwner, Observer { result ->
            lifecycleScope.launch {
                if (result != null) {
                    binding.containerMessage.visibility = View.VISIBLE
                    binding.txtMessage.text = result.mensaje
                    binding.txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32f)
                    if (result.estado == "Exito"){
                        Log.e("Resultado", result.data.toString())
                        Toast.makeText(
                            requireContext(),
                            "Bienvenido ${result.data.nombreUsuario}",
                            Toast.LENGTH_LONG
                        ).show()
                        // Guarda el usuario
                        DataStoreManager.saveUsername(requireContext(), result.data.nombreUsuario)
                        DataStoreManager.saveUserType(requireContext(), "Repartidor")
                        delay(1500)
                        (requireActivity() as MainActivity).navigateToMainGraph()
                    } else if (result.estado == "Error"){
                        delay(1500)
                        binding.containerMessage.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(requireContext(), "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}