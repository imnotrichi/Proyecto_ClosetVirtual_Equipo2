package mx.edu.itson.clothhangerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.dataclases.PrendaPreviewItem
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel

class PrendaEspecificaActivity : AppCompatActivity() {
    private lateinit var viewModel: PrendasViewModel
    private lateinit var adapter: PrendaPreviewAdapter
    lateinit var labelCategoria: TextView
    var prendas = ArrayList<PrendaPreviewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[PrendasViewModel::class.java]
        viewModel.listaPrendasUsuario.observe(this) { prendasFirestore ->
            prendas.clear()
            prendasFirestore.forEach {
                // Aquí debes adaptar tu objeto Prenda a PrendaPreviewItem
                prendas.add(PrendaPreviewItem(it.id, it.imagenUrl, it.nombre)) // Usa un drawable placeholder o adapta según datos
            }
            adapter.notifyDataSetChanged()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prenda_especifica)

        val gridView:GridView = findViewById(R.id.gvPrviewPrendas)
        val categoria = intent.getStringExtra("categoria")
        Log.d("PrendaEspecificaActivity", "Categoría: ${categoria}")


        labelCategoria = findViewById(R.id.tvCategoriaEspecifica)
        if (categoria != null) {
            labelCategoria.text = categoria
        }

        when (categoria) {
            "Zapatos", "Top", "Bottoms", "Bodysuits", "Accesorios" -> viewModel.cargarPorCategoria(categoria)
            else -> viewModel.cargarPorCategoria(null) // O podrías mostrar un mensaje de error
        }

        adapter = PrendaPreviewAdapter(this, prendas)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, RegistroDiario2Activity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}

private class PrendaPreviewAdapter : BaseAdapter {

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
            .load(prenda.imagenUrl)
            .into(imagen)

        nombre.text = prenda.nombre

        imagen.setOnClickListener {
            val intento = Intent(context, PrincipalActivity::class.java)
            context!!.startActivity(intento)
        }

        return vista
    }
}