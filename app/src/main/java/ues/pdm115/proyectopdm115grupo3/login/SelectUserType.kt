package ues.pdm115.proyectopdm115grupo3.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentSelectUserTypeBinding

class SelectUserType : Fragment() {

    private var _binding: FragmentSelectUserTypeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SelectUserType()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectUserTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.btnNegocio.setOnClickListener {
            findNavController().navigate(R.id.action_selectUserType_to_registerNegocioFragment)
        }
        binding.btnComprador.setOnClickListener {
            findNavController().navigate(R.id.action_selectUserType_to_registerUserFragment)
        }
        binding.btnRepartidor.setOnClickListener {
            findNavController().navigate(R.id.action_selectUserType_to_registerRepartidorFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}