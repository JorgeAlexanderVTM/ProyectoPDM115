package ues.pdm115.proyectopdm115grupo3.comprador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentLocatePackageUserBinding

class LocatePackageUser : Fragment() {

    private var _binding: FragmentLocatePackageUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocatePackageUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val pedidosRecycler = view.findViewById<RecyclerView>(R.id.recyclerView)
        pedidosRecycler.layoutManager = LinearLayoutManager(requireContext())

        val pedidosData = listOf(
            Pedidos("1", "2025-05-25", "Entrega puntual"),
            Pedidos("2", "2025-05-26", "Sin observaciones"),
            Pedidos("3", "2025-05-26", "Entregado sin novedades"),
            Pedidos("4", "2025-05-26", "Paquete ligeramente dañado"),
            Pedidos("5", "2025-05-26", "Entregado fuera del horario"),
            Pedidos("6", "2025-05-26", "Cliente no respondió al llamado"),
            Pedidos("7", "2025-05-26", "Entrega completada con éxito"),
            Pedidos("8", "2025-05-26", "Observaciones pendientes de registrar"),
            Pedidos("9", "2025-05-27", "Entrega anticipada"),
            Pedidos("10", "2025-05-27", "Demora por tráfico"),
            Pedidos("11", "2025-05-27", "Firmado por tercero"),
            Pedidos("12", "2025-05-27", "Entrega incompleta"),
        )
        val adapter = PedidosAdapter(pedidosData, findNavController())
        pedidosRecycler.adapter = adapter


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
                        lifecycleScope.launch {
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
