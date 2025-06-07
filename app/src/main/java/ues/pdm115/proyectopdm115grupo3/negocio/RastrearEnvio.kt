package ues.pdm115.proyectopdm115grupo3.negocio

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Html
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentRastrearEnvioBinding
import ues.pdm115.proyectopdm115grupo3.models.DetalleEnvio

class RastrearEnvio : Fragment() {

    private var _binding: FragmentRastrearEnvioBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RastrearEnvioViewModel by viewModels()

    private var nombreRemitente: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRastrearEnvioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.enviosRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            nombreRemitente = DataStoreManager.getUsername(requireContext())
            viewModel.verEnviosNombreRemitente(nombreRemitente!!)
        }

        observarDetalleEnvios()
        inicializarToolbar()
    }

    private fun observarDetalleEnvios(){
        viewModel.responseResult.observe(viewLifecycleOwner) { response ->
            response?.let {
                val enviosData: List<DetalleEnvio> = it.data ?: emptyList()
                val adapter = EnviosAdapter(enviosData, findNavController(), object : EnviosAdapter.OnEnvioClickListener {
                    override fun onEnvioClick(detalleEnvio: DetalleEnvio) {
                        // Aquí podrías hacer algo si se requiere
                    }

                    override fun onNavigateStarted() {
                        // Muestra el ProgressBar global antes de que comience la navegación
                        binding.containerProgressBar.visibility = View.VISIBLE
                        binding.globalProgressBar.visibility = View.VISIBLE
                        // Opcional: deshabilitar interacciones con el RecyclerView si es necesario
                        binding.enviosRecyclerView.isEnabled = false
                        // Opcional: mostrar progress bar o bloquear UI
                    }
                })
                binding.enviosRecyclerView.adapter = adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}