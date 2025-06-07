package ues.pdm115.proyectopdm115grupo3.negocio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.models.DetalleEnvio
import kotlin.text.toDouble

class EnviosAdapter(
    private val detalleEnvios: List<DetalleEnvio>,
    private val navController: NavController,
    private val clickListener: OnEnvioClickListener
) : RecyclerView.Adapter<EnviosAdapter.EnviosViewHolder>() {

    // Define la interfaz de callback aquí
    interface OnEnvioClickListener {
        fun onEnvioClick(detalleEnvio: DetalleEnvio)
        fun onNavigateStarted() // <-- Nuevo método para indicar que la navegación ha comenzado
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnviosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_envios, parent, false)
        return EnviosViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnviosViewHolder, position: Int) {
        val detalleEnvio = detalleEnvios[position]
        holder.tvNumeroSeguimiento.text = detalleEnvio.numeroSeguimiento
        holder.tvNombreDestinatario.text = detalleEnvio.nombreDestinatario
        holder.tvNombreRepartidor.text = detalleEnvio.nombreRepartidor

        holder.itemView.setOnClickListener {
            // Notificar al Fragmento que la navegación está a punto de comenzar
            clickListener.onNavigateStarted() // <-- LLAMAR ESTE MÉTODO

            val action = RastrearEnvioDirections
                .actionRastrearEnvioToRastrearEnvioMap(
                    detalleEnvio.idEnvio.toString(),
                    detalleEnvio.codigoSeguimiento,
                    detalleEnvio.numeroSeguimiento,
                    detalleEnvio.nombreDestinatario,
                    detalleEnvio.nombreRepartidor,
                    detalleEnvio.longitud.toString(),
                    detalleEnvio.latitud.toString()
                )
            navController.navigate(action)
        }

    }

    override fun getItemCount(): Int = detalleEnvios.size

    class EnviosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumeroSeguimiento: TextView = itemView.findViewById(R.id.txtNumeroSeguimiento)
        val tvNombreDestinatario: TextView = itemView.findViewById(R.id.txtNombreDestinatario)
        val tvNombreRepartidor: TextView = itemView.findViewById(R.id.txtNombreRepartidor)
    }

}