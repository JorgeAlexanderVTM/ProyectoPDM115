package ues.pdm115.proyectopdm115grupo3.negocio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import ues.pdm115.proyectopdm115grupo3.R

// Clase modelo para los datos
data class Envios(
    val codigoEnvio: String,
    val nombreComprador: String,
    val nombreRepartidor: String
)

class EnviosAdapter(
    private val envios: List<Envios>,
    private val navController: NavController,
    private val clickListener: OnEnvioClickListener
) : RecyclerView.Adapter<EnviosAdapter.EnviosViewHolder>() {

    // Define la interfaz de callback aquí
    interface OnEnvioClickListener {
        fun onEnvioClick(envio: Envios)
        fun onNavigateStarted() // <-- Nuevo método para indicar que la navegación ha comenzado
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnviosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_envios, parent, false)
        return EnviosViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnviosViewHolder, position: Int) {
        val envio = envios[position]
        holder.tvCodigoEnvio.text = envio.codigoEnvio
        holder.tvNombreComprador.text = envio.nombreComprador
        holder.tvNombreRepartidor.text = envio.nombreRepartidor

        holder.itemView.setOnClickListener {
            // Notificar al Fragmento que la navegación está a punto de comenzar
            clickListener.onNavigateStarted() // <-- LLAMAR ESTE MÉTODO

            val action = RastrearEnvioDirections
                .actionRastrearEnvioToRastrearEnvioMap(
                    envio.codigoEnvio, envio.nombreComprador, envio.nombreRepartidor
                )
            navController.navigate(action)
        }

    }

    override fun getItemCount(): Int = envios.size

    class EnviosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCodigoEnvio: TextView = itemView.findViewById(R.id.txtCodigoEnvio)
        val tvNombreComprador: TextView = itemView.findViewById(R.id.txtNombreComprador)
        val tvNombreRepartidor: TextView = itemView.findViewById(R.id.txtNombreRepartidor)
    }

}