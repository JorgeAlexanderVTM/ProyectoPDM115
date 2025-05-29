package ues.pdm115.proyectopdm115grupo3.comprador

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ues.pdm115.proyectopdm115grupo3.R

class RegisterUser : Fragment() {

    companion object {
        fun newInstance() = RegisterUser()
    }

    private val viewModel: RegisterUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register_user, container, false)
    }
}