package ues.pdm115.proyectopdm115grupo3.comprador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import ues.pdm115.proyectopdm115grupo3.R


// Clase modelo para los datos
data class Pedidos(
    val idPedido: String,
    val fechaEntrega: String,
    val observaciones: String
)

class PedidosAdapter(
    private val pedidos: List<Pedidos>,
    private val navController: NavController
) :
    RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.tvIdPedido.text = pedido.idPedido
        holder.tvFechaEntrega.text = pedido.fechaEntrega
        holder.tvObservaciones.text = pedido.observaciones

        holder.itemView.setOnClickListener {
            val action = LocatePackageUserDirections
                .actionLocatePackageUserFragmentToLocatePackageUserFragmentMap(
                    pedido.idPedido, pedido.fechaEntrega, pedido.observaciones
                )
            navController.navigate(action)
        }
    }

    override fun getItemCount(): Int = pedidos.size

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIdPedido: TextView = itemView.findViewById(R.id.tv_id_codigo)
        val tvFechaEntrega: TextView = itemView.findViewById(R.id.tv_fecha_entrega)
        val tvObservaciones: TextView = itemView.findViewById(R.id.tv_observaciones)
    }

}
