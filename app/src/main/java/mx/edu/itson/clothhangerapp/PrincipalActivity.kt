package mx.edu.itson.clothhangerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PrincipalActivity : MenuNavegable() {

    var articulos:ArrayList<Articulo> = ArrayList<Articulo>()
    var tbTops: ToggleButton? = null
    var tbBottoms: ToggleButton? = null
    var tbZapatos: ToggleButton? = null
    var tbBodysuits: ToggleButton? = null
    var tbAccesorios: ToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)


        tbTops = findViewById(R.id.tbTops)
        tbBottoms = findViewById(R.id.tbBottoms)
        tbZapatos = findViewById(R.id.tbZapatos)
        tbBodysuits = findViewById(R.id.tbBodysuits)
        tbAccesorios = findViewById(R.id.tbAccesorios)

        var tvCategoria:TextView = findViewById(R.id.tvCategoria)
        var lvArticulos:ListView = findViewById(R.id.lvArticulos)

        setupBottomNavigation()
        setSelectedItem(R.id.nav_home)

        val btnRegistrarPrenda:Button = findViewById(R.id.btnRegisrarPrenda)

        btnRegistrarPrenda.setOnClickListener {
            val intent = Intent(this, RegistrarPrendaActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        tbTops!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbBottoms!!.isChecked = false
                tbBottoms!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBottoms!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbZapatos!!.isChecked = false
                tbZapatos!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbZapatos!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbBodysuits!!.isChecked = false
                tbBodysuits!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBodysuits!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbAccesorios!!.isChecked = false
                tbAccesorios!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbAccesorios!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tvCategoria.text = "Tops"
                articulos.clear()
                agregarTops()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbTops!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbTops!!.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbTops!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbTops!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbBottoms!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbTops!!.isChecked = false
                tbTops!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbTops!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbZapatos!!.isChecked = false
                tbZapatos!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbZapatos!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbBodysuits!!.isChecked = false
                tbBodysuits!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBodysuits!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbAccesorios!!.isChecked = false
                tbAccesorios!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbAccesorios!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tvCategoria.text = "Bottoms"
                articulos.clear()
                agregarBottoms()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbBottoms!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbBottoms!!.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbBottoms!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBottoms!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbZapatos!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbTops!!.isChecked = false
                tbTops!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbTops!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbBottoms!!.isChecked = false
                tbBottoms!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBottoms!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbBodysuits!!.isChecked = false
                tbBodysuits!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBodysuits!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbAccesorios!!.isChecked = false
                tbAccesorios!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbAccesorios!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tvCategoria.text = "Zapatos"
                articulos.clear()
                agregarZapatos()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbZapatos!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbZapatos!!.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbZapatos!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbZapatos!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbBodysuits!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbTops!!.isChecked = false
                tbTops!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbTops!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbBottoms!!.isChecked = false
                tbBottoms!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBottoms!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbZapatos!!.isChecked = false
                tbZapatos!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbZapatos!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbAccesorios!!.isChecked = false
                tbAccesorios!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbAccesorios!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tvCategoria.text = "Bodysuits"
                articulos.clear()
                agregarBodysuits()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbBodysuits!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbBodysuits!!.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbBodysuits!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBodysuits!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbAccesorios!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbTops!!.isChecked = false
                tbTops!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbTops!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbBottoms!!.isChecked = false
                tbBottoms!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBottoms!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbZapatos!!.isChecked = false
                tbZapatos!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbZapatos!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tbBodysuits!!.isChecked = false
                tbBodysuits!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBodysuits!!.setTextColor(ContextCompat.getColor(this, R.color.black))

                tvCategoria.text = "Accesorios"
                articulos.clear()
                agregarAccesorios()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbAccesorios!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbAccesorios!!.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbAccesorios!!.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbAccesorios!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        lvArticulos.setOnItemClickListener { _, _, position, _ ->
            val articuloSeleccionado = articulos[position]
            val intent = Intent(this, DetallePrendaActivity::class.java).apply {
                putExtra("nombre", articuloSeleccionado.nombre)
                putExtra("imagen", articuloSeleccionado.imagen)
                putExtra("categoria", articuloSeleccionado.categoria)
                putExtra("color", articuloSeleccionado.color)
                putExtra("estampado", articuloSeleccionado.estampado)
                putExtra("tags", articuloSeleccionado.tags)
                putExtra("totalUsos", articuloSeleccionado.totalUsos)
                putExtra("usosMes", articuloSeleccionado.usosMes)
            }
            startActivity(intent)
        }
    }

    fun agregarTops() {
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Top", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Top", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Top", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Top", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Top", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Top", "Rosa", "Sí", "Cute", "20", "5"))
    }

    fun agregarBottoms() {
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bottom", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bottom", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bottom", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bottom", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bottom", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bottom", "Rosa", "Sí", "Cute", "20", "5"))
    }

    fun agregarZapatos() {
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Zapatos", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Zapatos", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Zapatos", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Zapatos", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Zapatos", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Zapatos", "Rosa", "Sí", "Cute", "20", "5"))
    }

    fun agregarBodysuits() {
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bodysuit", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bodysuit", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bodysuit", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bodysuit", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bodysuit", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Bodysuit", "Rosa", "Sí", "Cute", "20", "5"))
    }

    fun agregarAccesorios() {
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Accesorio", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Accesorio", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Accesorio", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Accesorio", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Accesorio", "Rosa", "Sí", "Cute", "20", "5"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "Accesorio", "Rosa", "Sí", "Cute", "20", "5"))
    }

    private class AdaptadorArticulos:BaseAdapter {
        var articulos = ArrayList<Articulo>()
        var contexto:Context ?= null

        constructor(contexto:Context, productos:ArrayList<Articulo>) {
            this.articulos = productos
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
            var art = articulos[position]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.articulo_view, null)

            var imagen = vista.findViewById(R.id.ivArticulo) as ImageView
            var nombre = vista.findViewById(R.id.tvNombreArticulo) as TextView
            var categoria = vista.findViewById(R.id.tvCategoriaArticulo) as TextView

            imagen.setImageResource(art.imagen)
            nombre.setText(art.nombre)
            categoria.setText(art.categoria)
            return vista
        }
    }

}