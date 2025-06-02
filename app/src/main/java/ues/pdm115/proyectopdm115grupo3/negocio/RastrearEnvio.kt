package ues.pdm115.proyectopdm115grupo3.negocio

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.comprador.Pedidos
import ues.pdm115.proyectopdm115grupo3.comprador.PedidosAdapter
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentRastrearEnvioBinding

class RastrearEnvio : Fragment(), EnviosAdapter.OnEnvioClickListener  {

    private lateinit var binding: FragmentRastrearEnvioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRastrearEnvioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.enviosRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        val enviosData = listOf(
            Envios("EVF44F3345FJ", "Carlos Daniel Morena", "Daniela Sofia Quinteño"),
            Envios("EVG55G1234KL", "Ana María López", "José Miguel Pérez"),
            Envios("EVH66H5678MN", "Sofía Isabel Ramírez", "Luis Fernando Gómez"),
            Envios("EVJ77I9012PQ", "Juan Carlos Hernández", "María Elena Vásquez"),
            Envios("EVK88J3456RS", "Karla Patricia Méndez", "Ricardo Antonio Cruz"),
            Envios("EVL99K7890TU", "Diego Armando Salazar", "Claudia Beatriz Ortiz"),
            Envios("EVM11L2345VW", "Laura Fernanda Castro", "Eduardo José Martínez"),
            Envios("EVN22M6789XY", "Gabriel Alejandro Ruiz", "Carmen Rosa Flores"),
            Envios("EVP33N0123ZA", "Mónica Lisbeth Aguilar", "Francisco Javier Torres"),
            Envios("EVQ44P4567BC", "Roberto Carlos Sánchez", "Patricia Guadalupe Rivas"),
            Envios("EVR55Q8901DE", "Verónica Elizabeth Díaz", "Manuel Esteban Rivera")
        )
        val adapter = EnviosAdapter(enviosData, findNavController(), this)
        binding.enviosRecyclerView.adapter = adapter

        inicializarToolbar()
    }


    // --- Implementación de la interfaz OnEnvioClickListener ---
    override fun onEnvioClick(envio: Envios) {
        // Esta función podría usarse para otras lógicas de clic si no implicaran navegación
        // Para este caso, el clickListener.onNavigateStarted() es más relevante.
    }

    override fun onNavigateStarted() {
        // Muestra el ProgressBar global antes de que comience la navegación
        binding.containerProgressBar.visibility = View.VISIBLE
        binding.globalProgressBar.visibility = View.VISIBLE
        // Opcional: deshabilitar interacciones con el RecyclerView si es necesario
        binding.enviosRecyclerView.isEnabled = false
    }

    private fun inicializarToolbar(){
        // Configura el MenuProvider para manejar el menú
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Infla el menú definido en nav_main_graph.xml (o manualmente)
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        lifecycleScope.launch(Dispatchers.Main) {
                            DataStoreManager.clearUsername(requireContext())
                            (requireActivity() as MainActivity).navigateToLoginGraph()
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}