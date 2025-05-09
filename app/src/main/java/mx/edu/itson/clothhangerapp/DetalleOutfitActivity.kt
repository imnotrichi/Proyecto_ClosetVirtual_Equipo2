package mx.edu.itson.clothhangerapp

import android.content.Context
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

class DetalleOutfitActivity : MenuNavegable() {

    var outfit: ArrayList<Prenda> = ArrayList<Prenda>()
    lateinit var fechaOutfit: TextView

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

        val fecha = intent.getStringExtra("fecha") ?: ""
        fechaOutfit = findViewById(R.id.tvFechaOutfit)
        fechaOutfit.text = "Outfit del ${fecha}"


        // CARGAR TOP
        val top_nombre = intent.getStringExtra("top_name") ?: ""
        val top_imagen = intent.getStringExtra("top_image") ?: ""
        val top_categoria = intent.getStringExtra("top_category") ?: ""

        Log.d("DETALLE - OUTFIT", "IMAGEN: ${top_imagen}")

        if (top_nombre != "") {
            outfit.add(Prenda("", top_imagen, "", top_nombre, top_categoria, false, "", mutableListOf()))
        }

        // CARGAR BOTTOM
        val bottom_nombre = intent.getStringExtra("bottom_name") ?: ""
        val bottom_imagen = intent.getStringExtra("bottom_image") ?: ""
        val bottom_categoria = intent.getStringExtra("bottom_category") ?: ""

        if (bottom_nombre != "") {
            outfit.add(Prenda("", bottom_imagen, "", bottom_nombre, bottom_categoria, false, "", mutableListOf()))
        }

        // CARGAR BODYSUIT
        val bodysuit_nombre = intent.getStringExtra("bodysuit_name") ?: ""
        val bodysuit_imagen = intent.getStringExtra("bodysuit_image") ?: ""
        val bodysuit_categoria = intent.getStringExtra("bodysuit_category") ?: ""

        if (bodysuit_nombre != "") {
            outfit.add(Prenda("", bodysuit_imagen, "", bodysuit_nombre, bodysuit_categoria, false, "", mutableListOf()))
        }

        // CARGAR ZAPATOS
        val zapatos_nombre = intent.getStringExtra("zapatos_name") ?: ""
        val zapatos_imagen = intent.getStringExtra("zapatos_image") ?: ""
        val zapatos_categoria = intent.getStringExtra("zapatos_category") ?: ""

        if (zapatos_nombre != "") {
            outfit.add(Prenda("", zapatos_imagen, "", zapatos_nombre, zapatos_categoria, false, "", mutableListOf()))
        }

        // CARGAR ACCESORIO 1
        val accesorio1_nombre = intent.getStringExtra("accesorio1_name") ?: ""
        val accesorio1_imagen = intent.getStringExtra("accesorio1_image") ?: ""
        val accesorio1_categoria = intent.getStringExtra("accesorio1_category") ?: ""

        if (accesorio1_nombre != "") {
            outfit.add(Prenda("", accesorio1_imagen, "", accesorio1_nombre, accesorio1_categoria, false, "", mutableListOf()))
        }

        // CARGAR ACCESORIO 2
        val accesorio2_nombre = intent.getStringExtra("accesorio2_name") ?: ""
        val accesorio2_imagen = intent.getStringExtra("accesorio2_image") ?: ""
        val accesorio2_categoria = intent.getStringExtra("accesorio2_category") ?: ""

        if (accesorio2_nombre != "") {
            outfit.add(Prenda("", accesorio2_imagen, "", accesorio2_nombre, accesorio2_categoria, false, "", mutableListOf()))
        }

        // CARGAR ACCESORIO 3
        val accesorio3_nombre = intent.getStringExtra("accesorio3_name") ?: ""
        val accesorio3_imagen = intent.getStringExtra("accesorio3_image") ?: ""
        val accesorio3_categoria = intent.getStringExtra("accesorio3_category") ?: ""

        if (accesorio3_nombre != "") {
            outfit.add(Prenda("", accesorio3_imagen, "", accesorio3_nombre, accesorio3_categoria, false, "", mutableListOf()))
        }

    }


}

private class AdaptadorOutfit: BaseAdapter {
    var prendas= ArrayList<Prenda>()
    var contexto: Context

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

        Glide.with(contexto)
            .load(prnd.imagenUrl)
            .into(imagen)
        nombre.setText(prnd.nombre)
        categoria.setText(prnd.categoria)

        return vista
    }
}