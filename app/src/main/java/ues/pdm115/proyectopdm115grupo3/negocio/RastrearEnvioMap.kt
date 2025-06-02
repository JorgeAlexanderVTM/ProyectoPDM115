package ues.pdm115.proyectopdm115grupo3.negocio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.comprador.Pedidos
import ues.pdm115.proyectopdm115grupo3.comprador.PedidosAdapter
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentRastrearEnvioMapBinding

class RastrearEnvioMap : Fragment(), OnRepartidorClickListener, OnMapReadyCallback  {

    private var _binding: FragmentRastrearEnvioMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var repartidoreData: List<Repartidor>
    private lateinit var googleMap: GoogleMap // Variable para almacenar la instancia del mapa


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRastrearEnvioMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Muestra el ProgressBar justo antes de iniciar la carga del mapa
        binding.progressBarMap.visibility = View.VISIBLE

        binding.btnAsignarRepartidor.setOnClickListener {
            if(binding.btnAsignarRepartidor.text == "Asignar repartidor"){
                binding.flCodigoPaquete.visibility = View.GONE
                binding.mapContainer.visibility = View.GONE
                binding.asignarRepartidorVista.visibility = View.VISIBLE
                binding.btnAsignarRepartidor.text = "Cancelar"
            }else{
                binding.mapContainer.visibility = View.VISIBLE
                binding.asignarRepartidorVista.visibility = View.GONE
                binding.btnAsignarRepartidor.text = "Asignar repartidor"
            }

        }

        binding.btnCompartirCodigo.setOnClickListener {
            binding.flCodigoPaquete.visibility = View.VISIBLE
        }

        binding.btnOcultarCodigo.setOnClickListener {
            binding.flCodigoPaquete.visibility = View.GONE
        }

        binding.btnCopiarCodigo.setOnClickListener {
            Toast.makeText(context,"Código copiado", Toast.LENGTH_SHORT).show()
        }

        binding.repartidoreRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        repartidoreData = listOf(
            Repartidor("1", "Carlos Gabriel Mendoza", "Disponible", null),
            Repartidor("2", "Ana Sofía Ramírez", "Disponible", null),
            Repartidor("3", "José Luis Pérez", "Disponible", null),
            Repartidor("4", "María Fernanda López", "Disponible", null),
            Repartidor("5", "Juan Carlos Vásquez", "Disponible", null),
            Repartidor("6", "Karla Patricia Gómez", "Disponible", null),
            Repartidor("7", "Luis Fernando Ortiz", "Disponible", null),
            Repartidor("8", "Claudia Beatriz Sánchez", "Disponible", null),
            Repartidor("9", "Ricardo Antonio Cruz", "Disponible", null),
            Repartidor("10", "Laura Isabel Torres", "Disponible", null),
            Repartidor("11", "Manuel Esteban Rivera", "Disponible", null)
        )
        val adapter = RepartidorAdapter(repartidoreData, this)
        binding.repartidoreRecyclerView.adapter = adapter
        binding.txtCodigoPaquete.text = "1223-22333" //Codigo recibido por API REST

        inicializarMapaGoogle()
        inicializarToolbar()


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

    private fun inicializarMapaGoogle(){
        val mapFragment = childFragmentManager.findFragmentById(binding.mapContainer.id) as SupportMapFragment?
        // Si el fragmento del mapa no existe, créalo y agrégalo
        if (mapFragment == null) {
            val newMapFragment = SupportMapFragment.newInstance()
            childFragmentManager.beginTransaction()
                .replace(binding.mapContainer.id, newMapFragment) // Usar el ID del FrameLayout a través de binding
                .commit()
            newMapFragment.getMapAsync(this) // Obtén el mapa de forma asíncrona
        } else {
            mapFragment.getMapAsync(this) // Si ya existe, simplemente obtén el mapa
        }

    }

    override fun onAsignarClick(position: Int) {
        val repartidor = repartidoreData[position]
        Toast.makeText(requireContext(), "Asignaste a ${repartidor.nombreRepartidor}", Toast.LENGTH_SHORT).show()

        binding.mapContainer.visibility = View.VISIBLE
        binding.asignarRepartidorVista.visibility = View.GONE
        binding.btnAsignarRepartidor.visibility = View.GONE
    }

    override fun onMapReady(p0: GoogleMap) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.progressBarMap.visibility = View.GONE
            googleMap = p0

            // Ejemplo: Añadir un marcador en San Salvador y mover la cámara
            val sanSalvador = LatLng(13.6929, -89.2182) // Coordenadas de San Salvador
            googleMap.addMarker(
                MarkerOptions().position(sanSalvador).title("Marcador en San Salvador")
            )
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    sanSalvador,
                    12f
                )
            ) // Zoom a nivel de ciudad

            // Habilitar controles de zoom (opcional)
            googleMap.uiSettings.isZoomControlsEnabled = true
            // Habilitar gestos de zoom (opcional)
            googleMap.uiSettings.isZoomGesturesEnabled = true

            // Puedes añadir más configuraciones o lógica aquí
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpiar la referencia al binding cuando la vista es destruida
    }
}