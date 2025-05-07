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

class DetalleOutfitActivity : MenuNavegable() {

    var outfit: ArrayList<Articulo> = ArrayList<Articulo>()

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
        outfit.add(Articulo("Lentes de sol", R.drawable.lentes_sol, "Accesorio", "Rosa", "Sí", "Cute", "20", "5"))
        outfit.add(Articulo("Blusa floreada", R.drawable.top_rosa_flores, "Top", "Rosa", "Sí", "Cute", "20", "5"))
        outfit.add(Articulo("Pantalón de mezclilla", R.drawable.pantalon_mezclilla, "Bottom", "Rosa", "Sí", "Cute", "20", "5"))
        outfit.add(Articulo("Zapatillas rojas", R.drawable.zapatilla_roja, "Zapatos", "Rosa", "Sí", "Cute", "20", "5"))
    }

}

private class AdaptadorOutfit: BaseAdapter {
    var articulos= ArrayList<Articulo>()
    var contexto: Context ?= null

    constructor(contexto: Context, articulos: ArrayList<Articulo>) {
        this.articulos = articulos
        this.contexto = contexto
    }

    override fun getCount(): Int {
        return articulos.size
    }

    override fun getItem(position: Int): Any {
        return articulos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var articulo = articulos[position]
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.articulo_view, null)

        var imagen = vista.findViewById(R.id.ivArticulo) as ImageView
        var nombre = vista.findViewById(R.id.tvNombreArticulo) as TextView
        var categoria = vista.findViewById(R.id.tvCategoriaArticulo) as TextView

        imagen.setImageResource(articulo.imagen)
        nombre.setText(articulo.nombre)
        categoria.setText(articulo.categoria)

        return vista
    }
}