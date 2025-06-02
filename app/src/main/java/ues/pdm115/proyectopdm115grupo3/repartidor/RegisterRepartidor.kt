package ues.pdm115.proyectopdm115grupo3.repartidor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentRegisterNegocioBinding
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentRegisterRepartidorBinding

class RegisterRepartidor : Fragment() {

    private var _binding: FragmentRegisterRepartidorBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterRepartidorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegistrar.setOnClickListener {
            binding.containerProgressBar.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    delay(1500)
                    DataStoreManager.saveUsername(requireContext(), "radmin", "repartidor")
                    Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                    (requireActivity() as MainActivity).navigateToMainGraph()
                } catch (e: Exception) {
                    binding.containerProgressBar.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCancelar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}