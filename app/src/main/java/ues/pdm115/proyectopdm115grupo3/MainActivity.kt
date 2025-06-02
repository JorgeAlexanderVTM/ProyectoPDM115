package ues.pdm115.proyectopdm115grupo3

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Configura el Toolbar
        setSupportActionBar(binding.toolbar)

        // Configura los elementos del usuario


        // Configura el NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configura AppBarConfiguration para evitar la flecha de retroceso en home
        appBarConfiguration = AppBarConfiguration(setOf(R.id.locatePackageUserFragment, R.id.homeRepartidor))
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Verifica el estado de login y configura el grafo inicial de forma asíncrona
        if (savedInstanceState == null) {
            lifecycleScope.launch {
                val username = DataStoreManager.getUsername(this@MainActivity)
                if (username != null) {
                    val userType = DataStoreManager.getUserType(this@MainActivity)
                    navigateToMainGraphWithUserInfo(username, userType ?: "comprador") // Valor por defecto
                } else {
                    navController.setGraph(R.navigation.nav_login_graph)
                }
                // Oculta el indicador de carga después de determinar el grafo
                binding.loadingIndicator.visibility = View.GONE
                binding.containerProgressBar.visibility = View.GONE
            }
        }

        // Escucha cambios en el destino para ocultar/mostrar el Toolbar y los elementos del usuario
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
                    lifecycleScope.launch {
                        val currentUsername = DataStoreManager.getUsername(this@MainActivity)
                        if (currentUsername != null) {
                            binding.userNameText.text = currentUsername
                            binding.userProfileImage.setImageResource(R.drawable.__default)
                        }
                    }
                }
                else -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.userInfoContainer.visibility = View.VISIBLE
                    binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    binding.userInfoContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    lifecycleScope.launch {
                        val currentUsername = DataStoreManager.getUsername(this@MainActivity)
                        if (currentUsername != null) {
                            binding.userNameText.text = currentUsername
                            binding.userProfileImage.setImageResource(R.drawable.__default)
                        }
                    }
                }
            }
        }

        // Ajusta los insets para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
                navigateToMainGraphWithUserInfo(username, userType ?: "comprador")
            }
        }
    }

    private fun navigateToMainGraphWithUserInfo(username: String, userType: String) {
        // Carga el grafo correspondiente según el tipo de usuario
        if(userType == "comprador"){
            navController.setGraph(R.navigation.nav_main_graph)
        }else if(userType == "repartidor"){
            navController.setGraph(R.navigation.nav_repartidor_graph)
        }else{
            navController.setGraph(R.navigation.nav_negocio_graph)
        }
        binding.userNameText.text = username
        binding.userProfileImage.setImageResource(R.drawable.__default) // Asegúrate de que el recurso exista
    }


    fun navigateToLoginGraph() {
        navController.setGraph(R.navigation.nav_login_graph)
    }
}