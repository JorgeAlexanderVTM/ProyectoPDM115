package ues.pdm115.proyectopdm115grupo3.comprador

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentLocatePackageUserMapBinding

class LocatePackageUserMap : Fragment(), OnMapReadyCallback{

    private var _binding: FragmentLocatePackageUserMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private val args: LocatePackageUserMapArgs by navArgs()


    private val LOCATION_PERMISSION_REQUEST_CODE = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocatePackageUserMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.txtTitulo.text = "Numero de seguimiento: " + args.numeroSeguimiento
        binding.tvIdCodigo.text = args.numeroTelefonico
        binding.tvFechaEntrega.text = args.fechaEntrega
        binding.tvObservaciones.text = args.observaciones
        binding.txtCodigoConfirmacion.text = args.codigoSeguro
        binding.txtNombreRepartidor.text = args.nombreRepartidor

        binding.btnConfirmarPedido.setOnClickListener {
            binding.flConfirmar.visibility = View.VISIBLE
            binding.contentDescription.visibility = View.GONE
            binding.btnConfirmarPedido.visibility = View.GONE
        }

        binding.btnOcultarCodigo.setOnClickListener {
            binding.flConfirmar.visibility = View.GONE
            binding.contentDescription.visibility = View.VISIBLE
            binding.btnConfirmarPedido.visibility = View.VISIBLE
        }

        inicializarToolbar()
        inicializarMapaGoogle()
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
                    MarkerOptions()
                        .position(ubicacion)
                        .title("Ubicación destinatario")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
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

            if (checkLocationPermission()) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                requestLocationPermission()
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

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}