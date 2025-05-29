package ues.pdm115.proyectopdm115grupo3.repartidor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R

class HistorialEntregas : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entregasRecycler = view.findViewById<RecyclerView>(R.id.entregas_recycler)
        entregasRecycler.layoutManager = LinearLayoutManager(requireContext())

        val entregaData = listOf(
            Entrega("1", "2025-05-25", "14:30", "V123", "Entrega puntual"),
            Entrega("2", "2025-05-26", "15:45", "V124", "Sin observaciones"),
            Entrega("3", "2025-05-26", "16:10", "V125", "Entregado sin novedades"),
            Entrega("4", "2025-05-26", "17:00", "V126", "Paquete ligeramente dañado"),
            Entrega("5", "2025-05-26", "17:45", "V127", "Entregado fuera del horario"),
            Entrega("6", "2025-05-26", "18:20", "V128", "Cliente no respondió al llamado"),
            Entrega("7", "2025-05-26", "18:50", "V129", "Entrega completada con éxito"),
            Entrega("8", "2025-05-26", "19:30", "V130", "Observaciones pendientes de registrar"),
            Entrega("9", "2025-05-27", "08:15", "V131", "Entrega anticipada"),
            Entrega("10", "2025-05-27", "09:00", "V132", "Demora por tráfico"),
            Entrega("11", "2025-05-27", "09:45", "V133", "Firmado por tercero"),
            Entrega("12", "2025-05-27", "10:30", "V134", "Entrega incompleta"),
        )

        val adapter = EntregasAdapter(entregaData)
        entregasRecycler.adapter = adapter

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_historial_entregas, container, false)
    }
}