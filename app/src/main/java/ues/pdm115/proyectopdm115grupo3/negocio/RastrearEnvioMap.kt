package ues.pdm115.proyectopdm115grupo3.negocio

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentRastrearEnvioMapBinding
import ues.pdm115.proyectopdm115grupo3.models.UsuarioRol
import kotlin.getValue

class RastrearEnvioMap : Fragment(), OnRepartidorClickListener, OnMapReadyCallback  {

    private var _binding: FragmentRastrearEnvioMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap // Variable para almacenar la instancia del mapa

    private val args: RastrearEnvioMapArgs by navArgs()

    private var usuariosData: List<UsuarioRol>? = null

    private val viewModel: RastrearEnvioMapViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRastrearEnvioMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.verUsuariosByRoles("Repartidor")
        }
        observarUsuariosRoles()

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
            val texto = binding.txtCodigoPaquete.text.toString()
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Código de Paquete", texto)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context,"Código copiado", Toast.LENGTH_SHORT).show()
        }

        binding.repartidoreRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.txtCodigoPaquete.text = args.codigoSeguro
        val nombreDestinatario = args.nombreDestinatario
        val numeroSeguimiento = args.numeroSeguimiento
        val nombreRepartidor = args.nombreRepartidor
        val htmlTexto = "<b>Nombre: $nombreDestinatario</b>" +
                        "<br><small>Repartidor: $nombreRepartidor</small>" +
                        "<br><small><i>Numero seguimiento: $numeroSeguimiento</i></small>"
        binding.txtTitulo.text = Html.fromHtml(htmlTexto, Html.FROM_HTML_MODE_LEGACY)

        inicializarMapaGoogle()
        inicializarToolbar()

        if(args.nombreRepartidor == " "){
            binding.btnAsignarRepartidor.visibility = View.VISIBLE
        }
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
                            DataStoreManager.clearUserType(requireContext())
                            (requireActivity() as MainActivity).navigateToLoginGraph()
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observarUsuariosRoles() {
        viewModel.responseResult.observe(viewLifecycleOwner) { response ->
            response?.let {
                usuariosData = it.data ?: emptyList()
                val adapter = RepartidorAdapter(usuariosData!!, this)
                binding.repartidoreRecyclerView.adapter = adapter
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.containerProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                binding.containerMessage.visibility = View.VISIBLE
                val html = "<html><body><h1>Servicio suspendido</h1><p>Intentalo mas tarde.</p></body></html>"
                binding.txtMessage.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
                binding.txtMessage.textSize = 20f
                //Toast.makeText(requireContext(), "Error al cargar direcciones: $message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observarActualizacionRepartidor() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.containerProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                binding.containerMessage.visibility = View.VISIBLE
                val html = "<html><body><h1>Servicio suspendido</h1><p>Intentalo más tarde.</p></body></html>"
                binding.txtMessage.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
                binding.txtMessage.textSize = 20f
            }
        }

        viewModel.updateResult.observe(viewLifecycleOwner) { updateResponse ->
            updateResponse?.let {
                Toast.makeText(requireContext(), "Repartidor asignado con éxito", Toast.LENGTH_SHORT).show()
                // Muestra el Toast y oculta elementos de la UI
                binding.mapContainer.visibility = View.VISIBLE
                binding.asignarRepartidorVista.visibility = View.GONE
                binding.btnAsignarRepartidor.visibility = View.GONE
                val nombreDestinatario = args.nombreDestinatario
                val numeroSeguimiento = args.numeroSeguimiento
                val nombreRepartidor = args.nombreRepartidor
                val htmlTexto = "<b>Nombre: $nombreDestinatario</b>" +
                        "<br><small>Repartidor: $nombreRepartidor</small>" +
                        "<br><small><i>Numero seguimiento: $numeroSeguimiento</i></small>"
                binding.txtTitulo.text = Html.fromHtml(htmlTexto, Html.FROM_HTML_MODE_LEGACY)
            }
        }
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
        val repartidor = usuariosData!![position]

        // Verifica que el id del envío no esté vacío o nulo
        if (!args.idEnvio.isNullOrEmpty()) {
            // Actualiza el repartidor
            lifecycleScope.launch {
                viewModel.actualizarEnvioRepartidor(repartidor.idUsuario, args.idEnvio.toInt())
            }
            // Observa la actualización
            observarActualizacionRepartidor()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.containerProgressBar.visibility = View.GONE
            googleMap = p0

            if (args.latitud != null && args.longitud != null) {
                val ubicacion = LatLng(args.latitud.toDouble(), args.longitud.toDouble())
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        ubicacion,
                        15f
                    )
                )
                googleMap.addMarker(
                    MarkerOptions().position(ubicacion).title("Ubicación destinatario")
                )
            }else{
                val sanSalvador = LatLng(13.6929, -89.2182)
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        sanSalvador,
                        15f
                    )
                )
            }
            // Habilitar controles de zoom (opcional)
            googleMap.uiSettings.isZoomControlsEnabled = true
            // Habilitar gestos de zoom (opcional)
            googleMap.uiSettings.isZoomGesturesEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpiar la referencia al binding cuando la vista es destruida
    }
}