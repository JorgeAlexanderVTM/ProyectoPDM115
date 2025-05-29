package ues.pdm115.proyectopdm115grupo3.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentLoginUserBinding
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R

class LoginUser : Fragment() {

    private var _binding: FragmentLoginUserBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoginUser.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userType = if (username.startsWith("c")) "comprador" else "repartidor" // Lógica simple
            if ((username == "cadmin" && password == "1234") || (username == "radmin" && password == "1234")) {
                lifecycleScope.launch {
                    try {
                        DataStoreManager.saveUsername(requireContext(), username, userType)
                        Toast.makeText(requireContext(), "Login exitoso", Toast.LENGTH_SHORT).show()
                        (requireActivity() as MainActivity).navigateToMainGraph()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Login incorrecto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegisterUser.setOnClickListener {
            findNavController().navigate(R.id.action_loginUserFragment_to_selectUserType)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
