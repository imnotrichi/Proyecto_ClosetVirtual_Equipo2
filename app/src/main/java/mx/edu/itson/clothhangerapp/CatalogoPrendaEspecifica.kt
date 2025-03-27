package mx.edu.itson.clothhangerapp

import android.content.Context
import android.content.Intent
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
import java.security.Principal

class CatalogoPrendaEspecifica : MenuNavegable() {

    var adapter: PrendaPreviewAdapter? = null
    var prendas = ArrayList<PrendaPreviewItem>()

    lateinit var categoria: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo_prenda_especifica)

        categoria = findViewById(R.id.tvCategoriaEspecifica)

        val opcion = intent.getStringExtra("opcion")

        when ("tops") {
            "tops" -> cargarTops()
            "bottoms" -> cargarBottoms()
            "zapatos" -> cargarZapatos()
            "body_suits" -> cargarBodysuits()
            "accesorios" -> cargarAccesorios()
        }



        adapter = PrendaPreviewAdapter(this, prendas)
        adapter = PrendaPreviewAdapter(this, prendas)
        var cuadricula: GridView = findViewById(R.id.prendasPreviewGridView)
        cuadricula.adapter = adapter

        setupBottomNavigation()
        setSelectedItem(R.id.nav_home)

    }

    fun cargarTops() {
        categoria.text = "Tops"

        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Blusa Floreada"))
        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Blusa Floreada"))
        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Blusa Floreada"))
        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Blusa Floreada"))
    }

    fun cargarBottoms() {
        categoria.text = "Bottoms"

        prendas.add(PrendaPreviewItem(R.drawable.pantalon_mezclilla, "Pantalón"))
        prendas.add(PrendaPreviewItem(R.drawable.pantalon_mezclilla, "Pantalón"))
        prendas.add(PrendaPreviewItem(R.drawable.pantalon_mezclilla, "Pantalón"))
        prendas.add(PrendaPreviewItem(R.drawable.pantalon_mezclilla, "Pantalón"))
    }

    fun cargarZapatos() {
        categoria.text = "Zapatos"

        prendas.add(PrendaPreviewItem(R.drawable.zapatilla_roja, "Zapatilla"))
        prendas.add(PrendaPreviewItem(R.drawable.zapatilla_roja, "Zapatilla"))
        prendas.add(PrendaPreviewItem(R.drawable.zapatilla_roja, "Zapatilla"))
        prendas.add(PrendaPreviewItem(R.drawable.zapatilla_roja, "Zapatilla"))
    }

    fun cargarBodysuits() {
        categoria.text = "Bodysuits"

        prendas.add(PrendaPreviewItem(R.drawable.bodysuit_negro, "Traje negro"))
        prendas.add(PrendaPreviewItem(R.drawable.bodysuit_negro, "Traje negro"))
        prendas.add(PrendaPreviewItem(R.drawable.bodysuit_negro, "Traje negro"))
        prendas.add(PrendaPreviewItem(R.drawable.bodysuit_negro, "Traje negro"))
    }

    fun cargarAccesorios() {
        categoria.text = "Accesorios"

        prendas.add(PrendaPreviewItem(R.drawable.lentes_sol, "Lentes para sol"))
        prendas.add(PrendaPreviewItem(R.drawable.lentes_sol, "Lentes para sol"))
        prendas.add(PrendaPreviewItem(R.drawable.lentes_sol, "Lentes para sol"))
        prendas.add(PrendaPreviewItem(R.drawable.lentes_sol, "Lentes para sol"))
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

        imagen.setOnClickListener() {
            val intento = Intent(context, PrincipalActivity::class.java)
            context!!.startActivity(intento)
        }

        return vista
    }
}