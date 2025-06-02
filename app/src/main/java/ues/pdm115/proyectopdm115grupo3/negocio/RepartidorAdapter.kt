package ues.pdm115.proyectopdm115grupo3.negocio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import ues.pdm115.proyectopdm115grupo3.R

// Clase modelo para los datos
data class Repartidor(
    val id: String,
    val nombreRepartidor: String,
    val estado: String,
    val imagenUrl: String?
)

interface OnRepartidorClickListener {
    fun onAsignarClick(position: Int)
}


class RepartidorAdapter(
    private val repartidores: List<Repartidor>,
    private val listener: OnRepartidorClickListener
) : RecyclerView.Adapter<RepartidorAdapter.RepartidoresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepartidoresViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_repartidores, parent, false)
        return RepartidoresViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepartidoresViewHolder, position: Int) {
        val repartidor = repartidores[position]
        holder.id.text = repartidor.id
        holder.nombreRepartidor.text = repartidor.nombreRepartidor
        holder.estado.text = repartidor.estado
        Glide.with(holder.itemView.context)
            .load(repartidor.imagenUrl)
            .error(R.drawable.__default)
            .into(holder.imagenUrl)
    }

    override fun getItemCount(): Int = repartidores.size

    inner class RepartidoresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenUrl: ShapeableImageView = itemView.findViewById(R.id.repartidorImage)
        val btnAsignar: Button = itemView.findViewById(R.id.btnAsignar)
        val id: TextView = itemView.findViewById(R.id.txtIdRepartidor)
        val nombreRepartidor: TextView = itemView.findViewById(R.id.txtNombreRepartidor)
        val estado: TextView = itemView.findViewById(R.id.txtEstadoRepartidor)

        init {
            btnAsignar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onAsignarClick(position)
                }
            }
        }
    }

}