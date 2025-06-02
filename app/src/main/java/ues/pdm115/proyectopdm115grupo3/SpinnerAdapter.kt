package ues.pdm115.proyectopdm115grupo3

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class SpinnerAdapter (context: Context, resource: Int, objects: Array<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun isEnabled(position: Int): Boolean {
        // Deshabilita la primera posición (nuestra leyenda)
        return position != 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val tv = view as TextView
        if (position == 0){
            tv.setTextColor(ContextCompat.getColor(context, R.color.textHint))
        }else{
            tv.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv = view as TextView
        if (position == 0) {
            // Establece el color del texto para la leyenda (hint)
            tv.setTextColor(ContextCompat.getColor(context, R.color.textHint)) // Tu color de hint
        } else {
            tv.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.black
                )
            ) // Color normal para los demás ítems
        }
        return view
    }
}