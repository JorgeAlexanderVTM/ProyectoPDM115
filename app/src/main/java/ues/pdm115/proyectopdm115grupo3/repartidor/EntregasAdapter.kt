package ues.pdm115.proyectopdm115grupo3.repartidor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ues.pdm115.proyectopdm115grupo3.R


// Clase modelo para los datos
data class Entrega(
    val idEntrega: String,
    val fecha: String,
    val hora: String,
    val codigoVenta: String,
    val observaciones: String
)

class EntregasAdapter(private val entregas: List<Entrega>) :
    RecyclerView.Adapter<EntregasAdapter.EntregaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntregaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_entrega, parent, false)
        return EntregaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntregaViewHolder, position: Int) {
        val entrega = entregas[position]
        holder.tvIdEntrega.text = entrega.idEntrega
        holder.tvFecha.text = entrega.fecha
        holder.tvHora.text = entrega.hora
        holder.tvCodigoVenta.text = entrega.codigoVenta
        holder.tvObservaciones.text = entrega.observaciones
    }

    override fun getItemCount(): Int = entregas.size

    class EntregaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIdEntrega: TextView = itemView.findViewById(R.id.tv_id_entrega)
        val tvFecha: TextView = itemView.findViewById(R.id.tv_fecha)
        val tvHora: TextView = itemView.findViewById(R.id.tv_hora)
        val tvCodigoVenta: TextView = itemView.findViewById(R.id.tv_codigo_venta)
        val tvObservaciones: TextView = itemView.findViewById(R.id.tv_observaciones)
    }

}