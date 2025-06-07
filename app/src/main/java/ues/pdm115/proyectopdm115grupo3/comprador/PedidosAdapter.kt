package ues.pdm115.proyectopdm115grupo3.comprador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import ues.pdm115.proyectopdm115grupo3.R
import ues.pdm115.proyectopdm115grupo3.models.DetalleEnvio


// Clase modelo para los datos
class PedidosAdapter(
    private val pedidos: List<DetalleEnvio>,
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
        holder.tvNumeroTelefonico.text = "Numero telefónico: " + pedido.numeroTelefonoDestinatario.toString()
        holder.tvNombreDestinatario.text = pedido.nombreDestinatario
        holder.tvObservaciones.text = pedido.detalleEnvio

        holder.itemView.setOnClickListener {
            val action = LocatePackageUserDirections
                .actionLocatePackageUserFragmentToLocatePackageUserFragmentMap(
                    pedido.idEnvio.toString(),
                    "Numero telefónico: " + pedido.numeroTelefonoDestinatario.toString(),
                    pedido.nombreDestinatario,
                    pedido.detalleEnvio,
                    pedido.longitudRepartidor.toString(),
                    pedido.latitudRepartidor.toString(),
                    pedido.numeroSeguimiento,
                    pedido.codigoSeguimiento,
                    pedido.nombreRepartidor
                )
            navController.navigate(action)
        }
    }

    override fun getItemCount(): Int = pedidos.size

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumeroTelefonico: TextView = itemView.findViewById(R.id.tv_numero_telefonico)
        val tvNombreDestinatario: TextView = itemView.findViewById(R.id.tv_nombre_destinatario)
        val tvObservaciones: TextView = itemView.findViewById(R.id.tv_observaciones)
    }

}
