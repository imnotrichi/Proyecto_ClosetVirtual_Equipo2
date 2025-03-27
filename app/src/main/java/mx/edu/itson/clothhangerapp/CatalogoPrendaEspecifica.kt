package mx.edu.itson.clothhangerapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CatalogoPrendaEspecifica : AppCompatActivity() {

    var adapter: PrendaPreviewAdapter? = null
    var prendas = ArrayList<PrendaPreviewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo_prenda_especifica)

        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Android"))
        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Android"))
        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Android"))
        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Android"))

        adapter = PrendaPreviewAdapter(this, prendas)
        adapter = PrendaPreviewAdapter(this, prendas)
        var cuadricula: GridView = findViewById(R.id.prendasPreviewGridView)
        cuadricula.adapter = adapter

    }
}

class PrendaPreviewAdapter : BaseAdapter {

    var prendas = ArrayList<PrendaPreviewItem>()
    var context: Context? = null

    constructor(context: Context, articulos: ArrayList<PrendaPreviewItem>) : super() {
        this.prendas = articulos
        this.context = context
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
        var articulo = prendas[position]
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var vista = inflator.inflate(R.layout.prenda_preview_item, null)
        var imagen: ImageView = vista.findViewById(R.id.prenda_image_preview)
        var nombre: TextView = vista.findViewById(R.id.prenda_titulo_preview)

        imagen.setImageResource(articulo.imagen)
        nombre.setText(articulo.nombre)

        return vista
    }
}