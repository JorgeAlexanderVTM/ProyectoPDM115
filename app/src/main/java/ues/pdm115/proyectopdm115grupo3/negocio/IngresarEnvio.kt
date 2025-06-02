package ues.pdm115.proyectopdm115grupo3.negocio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ues.pdm115.proyectopdm115grupo3.DataStoreManager
import ues.pdm115.proyectopdm115grupo3.MainActivity
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.SpinnerAdapter
import ues.pdm115.proyectopdm115grupo3.databinding.FragmentIngresarEnvioBinding


class IngresarEnvio : Fragment() {

    private lateinit var binding : FragmentIngresarEnvioBinding

    private var departamentoSeleccionado: String = ""
    private var municipioSeleccionado: String = ""
    private var distritoSeleccionado: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIngresarEnvioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        seleccionarDepartamentosSpinner()
        seleccionarMunicipiosSpinner(emptyArray())
        seleccionarDistritosSpinner(emptyArray())

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

    private fun seleccionarDepartamentosSpinner() {
        val departamentos = getResources().getStringArray(R.array.departamentos_el_salvador)
        val adapter = SpinnerAdapter(requireContext(), R.layout.spinner_item, departamentos)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerDepartamento.adapter = adapter

        binding.spinnerDepartamento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDepartamento = parent.getItemAtPosition(position).toString()

                if (position > 0) {
                    binding.spinnerDepartamento.setBackgroundResource(R.drawable.border_searchview)
                    departamentoSeleccionado = selectedDepartamento
                    loadMunicipiosForDepartamento(departamentoSeleccionado)
                } else {
                    departamentoSeleccionado = ""
                    municipioSeleccionado = ""
                    seleccionarMunicipiosSpinner(emptyArray())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                departamentoSeleccionado = ""
                municipioSeleccionado = ""
                seleccionarMunicipiosSpinner(emptyArray())
            }
        }
    }

    private fun seleccionarMunicipiosSpinner(municipios: Array<String>) {

        val adapter = SpinnerAdapter(requireContext(), R.layout.spinner_item, municipios)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerMunicipio.adapter = adapter

        binding.spinnerMunicipio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    binding.spinnerMunicipio.setBackgroundResource(R.drawable.border_searchview)
                    municipioSeleccionado = parent.getItemAtPosition(position).toString()
                    loadDistritosForMunicipios(municipioSeleccionado)
                } else {
                    municipioSeleccionado = ""
                    seleccionarDistritosSpinner(emptyArray())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                municipioSeleccionado = ""
                distritoSeleccionado  = ""
                seleccionarDistritosSpinner(emptyArray())
            }
        }
    }

    private fun seleccionarDistritosSpinner(distritos: Array<String>) {

        val adapter = SpinnerAdapter(requireContext(), R.layout.spinner_item, distritos)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerDistrito.adapter = adapter

        binding.spinnerDistrito.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    binding.spinnerMunicipio.setBackgroundResource(R.drawable.border_searchview)
                    distritoSeleccionado = parent.getItemAtPosition(position).toString()
                } else {
                    distritoSeleccionado = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                distritoSeleccionado = ""
            }
        }
    }

    private fun loadDistritosForMunicipios(municipio: String) {
        val distritosResId: Int = when (municipio) {
            "Ahuachapán Centro" -> R.array.districts_ahuachapan_centro
            "Ahuachapán Norte" -> R.array.districts_ahuachapan_norte
            "Ahuachapán Sur" -> R.array.districts_ahuachapan_sur
            "Cabañas Este" -> R.array.districts_cabanas_este
            "Cabañas Oeste" -> R.array.districts_cabanas_oeste
            "Chalatenango Norte" -> R.array.districts_chalatenango_norte
            "Chalatenango Centro" -> R.array.districts_chalatenango_centro
            "Chalatenango Sur" -> R.array.districts_chalatenango_sur
            "Cuscatlán Norte" -> R.array.districts_cuscatlan_norte
            "Cuscatlán Sur" -> R.array.districts_cuscatlan_sur
            "La Libertad Norte" -> R.array.districts_la_libertad_norte
            "La Libertad Centro" -> R.array.districts_la_libertad_centro
            "La Libertad Oeste" -> R.array.districts_la_libertad_oeste
            "La Libertad Este" -> R.array.districts_la_libertad_este
            "La Libertad Costa" -> R.array.districts_la_libertad_costa
            "La Libertad Sur" -> R.array.districts_la_libertad_sur
            "La Paz Centro" -> R.array.districts_la_paz_centro
            "La Paz Este" -> R.array.districts_la_paz_este
            "La Paz Oeste" -> R.array.districts_la_paz_oeste
            "La Unión Norte" -> R.array.districts_la_union_norte
            "La Unión Sur" -> R.array.districts_la_union_sur
            "Morazán Norte" -> R.array.districts_morazan_norte
            "Morazán Sur" -> R.array.districts_morazan_sur
            "San Miguel Norte" -> R.array.districts_san_miguel_norte
            "San Miguel Centro" -> R.array.districts_san_miguel_centro
            "San Miguel Oeste" -> R.array.districts_san_miguel_oeste
            "San Salvador Norte" -> R.array.districts_san_salvador_norte
            "San Salvador Oeste" -> R.array.districts_san_salvador_oeste
            "San Salvador Este" -> R.array.districts_san_salvador_este
            "San Salvador Centro" -> R.array.districts_san_salvador_centro
            "San Salvador Sur" -> R.array.districts_san_salvador_sur
            "San Vicente Norte" -> R.array.districts_san_vicente_norte
            "San Vicente Sur" -> R.array.districts_san_vicente_sur
            "Santa Ana Centro" -> R.array.districts_santa_ana_centro
            "Santa Ana Norte" -> R.array.districts_santa_ana_norte
            "Santa Ana Este" -> R.array.districts_santa_ana_este
            "Santa Ana Oeste" -> R.array.districts_santa_ana_oeste
            "Sonsonate Centro" -> R.array.districts_sonsonete_centro
            "Sonsonate Este" -> R.array.districts_sonsonete_este
            "Sonsonate Oeste" -> R.array.districts_sonsonete_oeste
            "Sonsonate Norte" -> R.array.districts_sonsonete_norte
            "Usulután Norte" -> R.array.districts_usulutan_norte
            "Usulután Centro" -> R.array.districts_usulutan_centro
            "Usulután Este" -> R.array.districts_usulutan_este
            else -> 0
        }

        if (distritosResId != 0) {
            val distritosArray = resources.getStringArray(distritosResId)
            seleccionarDistritosSpinner(distritosArray)
        } else {
            seleccionarDistritosSpinner(emptyArray())
            Toast.makeText(requireContext(), "No se encontraron distritos para este municipio.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadMunicipiosForDepartamento(departamento: String) {
        val municipiosResId: Int = when (departamento) {
            "Ahuachapán" -> R.array.municipios_ahuachapan
            "Cabañas" -> R.array.municipios_cabanas
            "Chalatenango" -> R.array.municipios_chalatenango
            "Cuscatlán" -> R.array.municipios_cuscatlan
            "La Libertad" -> R.array.municipios_la_libertad
            "La Paz" -> R.array.municipios_la_paz
            "La Unión" -> R.array.municipios_la_union
            "Morazán" -> R.array.municipios_morazan
            "San Miguel" -> R.array.municipios_san_miguel
            "San Salvador" -> R.array.municipios_san_salvador
            "San Vicente" -> R.array.municipios_san_vicente
            "Santa Ana" -> R.array.municipios_santa_ana
            "Sonsonate" -> R.array.municipios_sonsonate
            "Usulután" -> R.array.municipios_usulutan
            else -> 0
        }

        if (municipiosResId != 0) {
            val municipiosArray = resources.getStringArray(municipiosResId)
            seleccionarMunicipiosSpinner(municipiosArray)
        } else {
            seleccionarMunicipiosSpinner(emptyArray())
            Toast.makeText(requireContext(), "No se encontraron municipios para este departamento.", Toast.LENGTH_SHORT).show()
        }
    }

}