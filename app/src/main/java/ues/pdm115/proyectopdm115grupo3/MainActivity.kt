package ues.pdm115.proyectopdm115grupo3

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        verificarPermisosUbicacion()
        // Configura el Toolbar
        setSupportActionBar(binding.toolbar)

        // Configura el NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configura AppBarConfiguration para evitar la flecha de retroceso en home
        appBarConfiguration = AppBarConfiguration(setOf(R.id.locatePackageUserFragment, R.id.homeRepartidor, R.id.loginUserFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        temaToolbar()

        // Comprobación de si el usuario está logueado
        checkUserLoginState()


        // Ajusta los insets para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkUserLoginState() {
        lifecycleScope.launch {
            val username = DataStoreManager.getUsername(this@MainActivity)

            if (username != null) {
                // Si hay un username guardado, lo consideramos como logueado
                val userType = DataStoreManager.getUserType(this@MainActivity) ?: "Comprador"
                navigateToMainGraphWithUserInfo(username, userType)
            } else {
                // Si no hay usuario guardado, lo mandamos al grafo de login
                navController.setGraph(R.navigation.nav_login_graph)
            }

            // Ocultamos el indicador de carga después de la verificación
            binding.loadingIndicator.visibility = View.GONE
            binding.containerProgressBar.visibility = View.GONE
        }
    }

    private fun temaToolbar(){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginUserFragment, R.id.registerUserFragment, R.id.registerRepartidorFragment, R.id.registerNegocioFragment, R.id.selectUserType -> {
                    binding.toolbar.visibility = View.GONE
                    binding.userInfoContainer.visibility = View.GONE
                }
                R.id.homeRepartidor, R.id.entregasAsignadas, R.id.mapaRutaActiva, R.id.historialEntregas, R.id.validacionCodigoEntrega -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.userInfoContainer.visibility = View.VISIBLE
                    binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbar_repartidor)) // Color negro para repartidor
                    binding.userInfoContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbar_repartidor))
                }
                else -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.userInfoContainer.visibility = View.VISIBLE
                    binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    binding.userInfoContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun navigateToMainGraph() {
        lifecycleScope.launch {
            val username = DataStoreManager.getUsername(this@MainActivity)
            if (username != null) {
                val userType = DataStoreManager.getUserType(this@MainActivity)
                navigateToMainGraphWithUserInfo(username, userType ?: "Comprador")
            }
        }
    }

    // Navegar al grafo principal según el tipo de usuario
    private fun navigateToMainGraphWithUserInfo(username: String, userType: String) {
        when (userType) {
            "Comprador" -> navController.setGraph(R.navigation.nav_main_graph)
            "Repartidor" -> navController.setGraph(R.navigation.nav_repartidor_graph)
            "Negocio" -> navController.setGraph(R.navigation.nav_negocio_graph)
        }
        binding.userNameText.text = username
        binding.userProfileImage.setImageResource(R.drawable.__default) // Asegúrate de que el recurso exista
    }


    fun navigateToLoginGraph() {
        navController.setGraph(R.navigation.nav_login_graph)
    }

    private fun verificarPermisosUbicacion() {
        val permisosNecesarios = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val permisosNoConcedidos = permisosNecesarios.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permisosNoConcedidos.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permisosNoConcedidos.toTypedArray(), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            } else {
                Toast.makeText(this, "Los permisos de ubicación son necesarios para continuar.", Toast.LENGTH_LONG).show()
            }
        }
    }
}

fun Fragment.hideKeyboard() {
    view?.let { v ->
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}