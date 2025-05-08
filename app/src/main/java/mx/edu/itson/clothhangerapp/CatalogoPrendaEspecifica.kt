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
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel
import java.security.Principal

class CatalogoPrendaEspecifica : MenuNavegable() {
    private lateinit var viewModel: PrendasViewModel
    var adapter: PrendaPreviewAdapter? = null
    var prendas = ArrayList<PrendaPreviewItem>()

    lateinit var categoria: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo_prenda_especifica)

        categoria = findViewById(R.id.tvCategoriaEspecifica)

        when (categoria.text.toString()) {
            "Zapatos", "Top", "Bottoms", "Bodysuits", "Accesorios" -> viewModel.cargarPorCategoria(categoria.text.toString())
            else -> viewModel.cargarPorCategoria(null) // O podrías mostrar un mensaje de error
        }

        adapter = PrendaPreviewAdapter(this, prendas)
        adapter = PrendaPreviewAdapter(this, prendas)
        var cuadricula: GridView = findViewById(R.id.prendasPreviewGridView)
        cuadricula.adapter = adapter

        setupBottomNavigation()
        setSelectedItem(R.id.nav_home)

    }
}

class PrendaPreviewAdapter : BaseAdapter {

    var prendas = ArrayList<PrendaPreviewItem>()
    var context: Context? = null

    constructor(context: Context, prendas: ArrayList<PrendaPreviewItem>) : super() {
        this.prendas = prendas
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
        val prenda = prendas[position]
        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vista = inflator.inflate(R.layout.prenda_preview_item, null)
        val imagen: ImageView = vista.findViewById(R.id.prenda_image_preview)
        val nombre: TextView = vista.findViewById(R.id.prenda_titulo_preview)

        // Cargar imagen desde URL con Glide
        Glide.with(context!!)
            .load(prenda.imagen)
            .into(imagen)

        nombre.text = prenda.nombre

        imagen.setOnClickListener {
            val intento = Intent(context, PrincipalActivity::class.java)
            context!!.startActivity(intento)
        }

        return vista
    }
}
