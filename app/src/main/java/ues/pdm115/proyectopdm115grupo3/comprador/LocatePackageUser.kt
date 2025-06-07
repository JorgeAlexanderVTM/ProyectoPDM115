package ues.pdm115.proyectopdm115grupo3.comprador

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentLocatePackageUserBinding
import ues.pdm115.proyectopdm115grupo3.models.DetalleEnvio
import ues.pdm115.proyectopdm115grupo3.negocio.EnviosAdapter
import ues.pdm115.proyectopdm115grupo3.negocio.RastrearEnvioMapViewModel
import java.io.IOException
import kotlin.getValue

class LocatePackageUser : Fragment() {

    private var _binding: FragmentLocatePackageUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LocatePackageUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocatePackageUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        //val pedidosRecycler = view.findViewById<RecyclerView>(R.id.recyclerView)
        //pedidosRecycler.layoutManager = LinearLayoutManager(requireContext())

        //pedidosRecycler.adapter = adapter


        inicializarToolBar()

        binding.swBuscarPedido.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isNotEmpty() == true) {
                    buscarNumeroSeguimiento(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Aquí puedes manejar lo que sucede cuando el texto está vacío
                }
                return false
            }
        })
    }

    private fun inicializarToolBar(){
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

    private fun buscarNumeroSeguimiento(query: String){

        viewModel.verEnviosNumeroSeguimiento(query)
        observarDetalleEnvios()


    }
    //CSSiiIzwC1cz
    private fun observarDetalleEnvios(){
        viewModel.responseResult.observe(viewLifecycleOwner) { response ->
            response?.let {
                val enviosData: List<DetalleEnvio> = it.data ?: emptyList()
                val adapter = PedidosAdapter(enviosData, findNavController())
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter
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
}

