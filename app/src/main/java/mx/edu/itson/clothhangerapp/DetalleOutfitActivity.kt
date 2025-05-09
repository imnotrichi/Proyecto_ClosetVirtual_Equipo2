package mx.edu.itson.clothhangerapp

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.dataclases.Articulo
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import java.util.Locale

class DetalleOutfitActivity : MenuNavegable() {

    var outfit: ArrayList<Prenda> = ArrayList()
    lateinit var fechaOutfit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_outfit)

        cargarOutfit()

        val listView: ListView = findViewById(R.id.lvPrendasDelDia)
        val adaptador = AdaptadorOutfit(this, outfit)
        listView.adapter = adaptador

        setupBottomNavigation()
    }

    fun cargarOutfit() {
        // Obtener el Timestamp de Firebase
        val timestamp = intent.getParcelableExtra<com.google.firebase.Timestamp>("fecha")

        // Verificar si el timestamp no es nulo
        timestamp?.let {
            // Convertir el Timestamp a Date
            val date = it.toDate()

            // Formatear la fecha según tus necesidades
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaFormateada = formato.format(date)

            // Mostrar en el TextView
            fechaOutfit = findViewById(R.id.tvTitulo)
            fechaOutfit.text = "Outfit del $fechaFormateada"
        } ?: run {
            // Si el timestamp es nulo, mostrar un mensaje predeterminado
            fechaOutfit = findViewById(R.id.tvTitulo)
            fechaOutfit.text = "Outfit del día"
        }

        fun agregarPrenda(nombre: String?, imagen: String?, categoria: String?) {
            if (!nombre.isNullOrBlank()) {
                val prenda = Prenda(
                    "",
                    imagen ?: "",
                    "",
                    nombre,
                    categoria ?: "",
                    false,
                    "",
                    mutableListOf()
                )
                outfit.add(prenda)
            }
        }

        agregarPrenda(
            intent.getStringExtra("top_name"),
            intent.getStringExtra("top_image"),
            intent.getStringExtra("top_category")
        )
        agregarPrenda(
            intent.getStringExtra("bottom_name"),
            intent.getStringExtra("bottom_image"),
            intent.getStringExtra("bottom_category")
        )
        agregarPrenda(
            intent.getStringExtra("bodysuit_name"),
            intent.getStringExtra("bodysuit_image"),
            intent.getStringExtra("bodysuit_category")
        )
        agregarPrenda(
            intent.getStringExtra("zapatos_name"),
            intent.getStringExtra("zapatos_image"),
            intent.getStringExtra("zapatos_category")
        )
        agregarPrenda(
            intent.getStringExtra("accesorio1_name"),
            intent.getStringExtra("accesorio1_image"),
            intent.getStringExtra("accesorio1_category")
        )
        agregarPrenda(
            intent.getStringExtra("accesorio2_name"),
            intent.getStringExtra("accesorio2_image"),
            intent.getStringExtra("accesorio2_category")
        )
        agregarPrenda(
            intent.getStringExtra("accesorio3_name"),
            intent.getStringExtra("accesorio3_image"),
            intent.getStringExtra("accesorio3_category")
        )
    }
}

private class AdaptadorOutfit: BaseAdapter {
    var prendas= ArrayList<Prenda>()
    var contexto: Context

    override fun getCount(): Int = prendas.size

    override fun getItem(position: Int): Any = prendas[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val prenda = prendas[position]
        val inflador = LayoutInflater.from(contexto)
        val vista = inflador.inflate(R.layout.prenda_view, null)

        val imagen = vista.findViewById<ImageView>(R.id.ivImagenPrenda)
        val nombre = vista.findViewById<TextView>(R.id.tvNombrePrenda)
        val categoria = vista.findViewById<TextView>(R.id.tvCategoriaPrenda)

        Glide.with(contexto)
            .load(prenda.imagenUrl)
            .placeholder(R.drawable.placeholder)
            .into(imagen)

        nombre.text = prenda.nombre
        categoria.text = prenda.categoria

        return vista
    }
}