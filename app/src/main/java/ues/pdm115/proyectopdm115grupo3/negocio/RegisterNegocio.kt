package ues.pdm115.proyectopdm115grupo3.negocio

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ues.pdm115.proyectopdm115grupo3.R

class RegisterNegocio : Fragment() {

    companion object {
        fun newInstance() = RegisterNegocio()
    }

    private val viewModel: RegisterNegocioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register_negocio, container, false)
    }
}