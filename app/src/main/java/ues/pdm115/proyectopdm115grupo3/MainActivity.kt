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

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var userInfoContainer: LinearLayout
    private lateinit var userProfileImage: ShapeableImageView
    private lateinit var userNameText: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configura el Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configura los elementos del usuario
        userInfoContainer = findViewById(R.id.userInfoContainer)
        userProfileImage = findViewById(R.id.userProfileImage)
        userNameText = findViewById(R.id.userNameText)
        loadingIndicator = findViewById(R.id.loadingIndicator)


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
                loadingIndicator.visibility = View.GONE
            }
        }

        // Escucha cambios en el destino para ocultar/mostrar el Toolbar y los elementos del usuario
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginUserFragment, R.id.registerUserFragment, R.id.registerRepartidorFragment, R.id.registerNegocioFragment, R.id.selectUserType -> {
                    toolbar.visibility = View.GONE
                    userInfoContainer.visibility = View.GONE
                }
                R.id.homeRepartidor, R.id.entregasAsignadas, R.id.mapaRutaActiva, R.id.historialEntregas, R.id.validacionCodigoEntrega -> {
                    toolbar.visibility = View.VISIBLE
                    userInfoContainer.visibility = View.VISIBLE
                    toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbar_repartidor)) // Color negro para repartidor
                    userInfoContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbar_repartidor))
                    lifecycleScope.launch {
                        val currentUsername = DataStoreManager.getUsername(this@MainActivity)
                        if (currentUsername != null) {
                            userNameText.text = currentUsername
                            userProfileImage.setImageResource(R.drawable.__default)
                        }
                    }
                }
                else -> {
                    toolbar.visibility = View.VISIBLE
                    userInfoContainer.visibility = View.VISIBLE
                    toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white)) // Color por defecto
                    userInfoContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    lifecycleScope.launch {
                        val currentUsername = DataStoreManager.getUsername(this@MainActivity)
                        if (currentUsername != null) {
                            userNameText.text = currentUsername
                            userProfileImage.setImageResource(R.drawable.__default) // Asegúrate de que el recurso exista
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
        val graphId = if (userType == "comprador") R.navigation.nav_main_graph else R.navigation.nav_repartidor_graph
        navController.setGraph(graphId)
        userNameText.text = username
        userProfileImage.setImageResource(R.drawable.__default) // Asegúrate de que el recurso exista
    }


    fun navigateToLoginGraph() {
        navController.setGraph(R.navigation.nav_login_graph)
    }
}