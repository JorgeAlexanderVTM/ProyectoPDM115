package ues.pdm115.proyectopdm115grupo3.login

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentLoginUserBinding
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.hideKeyboard

class LoginUser : Fragment() {

    private var _binding: FragmentLoginUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginUserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegisterUser.setOnClickListener {
            findNavController().navigate(R.id.action_loginUserFragment_to_selectUserType)
        }

        binding.txtUsername.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) hideKeyboard()
        }

        binding.txtPassword.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) hideKeyboard()
        }

        binding.btnLoginUser.setOnClickListener {
            val nombre = binding.txtUsername.text.toString()
            val contraseña = binding.txtPassword.text.toString()

            hideKeyboard()

            if (nombre.isNotBlank() && contraseña.isNotBlank()) {

                viewModel.login(nombre, contraseña)
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
            } else {
                binding.containerFormulario.visibility = View.VISIBLE
                binding.containerProgressBar.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.loginResult.observe(viewLifecycleOwner, Observer { result ->
            lifecycleScope.launch {
                if (result != null) {
                    binding.containerMessage.visibility = View.VISIBLE
                    binding.txtMessage.text = result.mensaje
                    binding.txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32f)


                    if (result.estado == "Exito"){

                        Toast.makeText(
                            requireContext(),
                            "Bienvenido ${result.data.nombre}",
                            Toast.LENGTH_LONG
                        ).show()
                        // Guarda el usuario
                        DataStoreManager.saveUsername(requireContext(), result.data.nombre)
                        DataStoreManager.saveUserType(requireContext(), result.data.nombre_rol)
                        delay(1500)
                        (requireActivity() as MainActivity).navigateToMainGraph()
                    } else if (result.estado == "Error"){
                        delay(1500)
                        binding.containerMessage.visibility = View.GONE

                    }
                } else {
                    Toast.makeText(context, "Parece que hubo un problema, intetalo mas tarde", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
