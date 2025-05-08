package mx.edu.itson.clothhangerapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import mx.edu.itson.clothhangerapp.dataclases.Articulo
import mx.edu.itson.clothhangerapp.dataclases.Prenda

class DetalleOutfitActivity : MenuNavegable() {

    var outfit: ArrayList<Prenda> = ArrayList<Prenda>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_outfit)

        cargarOutfit()

        var listView: ListView = findViewById(R.id.lvPrendasDelDia) as ListView

        var adaptador: AdaptadorOutfit = AdaptadorOutfit(this, outfit)

        listView.adapter = adaptador

        setupBottomNavigation()
    }

    fun cargarOutfit() {
        //outfit.add(Articulo("Lentes de sol", R.drawable.lentes_sol, "Accesorio", "Rosa", "Sí", "Cute", "20", "5"))
        //outfit.add(Articulo("Blusa floreada", R.drawable.top_rosa_flores, "Top", "Rosa", "Sí", "Cute", "20", "5"))
        //outfit.add(Articulo("Pantalón de mezclilla", R.drawable.pantalon_mezclilla, "Bottom", "Rosa", "Sí", "Cute", "20", "5"))
        //outfit.add(Articulo("Zapatillas rojas", R.drawable.zapatilla_roja, "Zapatos", "Rosa", "Sí", "Cute", "20", "5"))
    }

}

private class AdaptadorOutfit: BaseAdapter {
    var prendas= ArrayList<Prenda>()
    var contexto: Context ?= null

    constructor(contexto: Context, prendas: ArrayList<Prenda>) {
        this.prendas = prendas
        this.contexto = contexto
    }

    override fun getCount(): Int {
        return prendas.size
    }

    override fun getItem(position: Int): Any {
        return prendas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var prnd = prendas[position]
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.prenda_view, null)

        var imagen = vista.findViewById(R.id.ivImagenPrenda) as ImageView
        var nombre = vista.findViewById(R.id.tvNombrePrenda) as TextView
        var categoria = vista.findViewById(R.id.tvCategoriaPrenda) as TextView

       // imagen.setImageResource(prnd.imagen.toInt())
        nombre.setText(prnd.nombre)
        categoria.setText(prnd.categoria)

        return vista
    }
}