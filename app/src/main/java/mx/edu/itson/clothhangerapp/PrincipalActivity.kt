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
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PrincipalActivity : AppCompatActivity() {

    var articulos:ArrayList<Articulo> = ArrayList<Articulo>()
    val tbTops: ToggleButton = findViewById(R.id.tbTops)
    val tbBottoms: ToggleButton = findViewById(R.id.tbBottoms)
    val tbZapatos: ToggleButton = findViewById(R.id.tbZapatos)
    val tbBodysuits: ToggleButton = findViewById(R.id.tbBodysuits)
    val tbAccesorios: ToggleButton = findViewById(R.id.tbAccesorios)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        var tvCategoria:TextView = findViewById(R.id.tvCategoria)
        var lvArticulos:ListView = findViewById(R.id.lvArticulos)

        tbTops.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvCategoria.text = "Tops"
                agregarTops()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbTops)
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbTops)
            }
        }

        tbBottoms.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvCategoria.text = "Bottoms"
                agregarTops()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbBottoms)
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbBottoms)
            }
        }

        tbZapatos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvCategoria.text = "Zapatos"
                agregarTops()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbZapatos)
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbZapatos)
            }
        }

        tbBodysuits.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvCategoria.text = "Bodysuits"
                agregarTops()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbBodysuits)
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbBodysuits)
            }
        }

        tbAccesorios.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvCategoria.text = "Accesorios"
                agregarTops()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbAccesorios)
            } else {
                tvCategoria.text = "Categoría"
                articulos.clear()

                var adapatador:AdaptadorArticulos = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                checkearToggleButton(tbAccesorios)
            }
        }
    }

    fun checkearToggleButton(button: ToggleButton) {
        tbTops.isChecked = false
        tbTops.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
        tbTops.setTextColor(ContextCompat.getColor(this, R.color.black))

        tbBottoms.isChecked = false
        tbBottoms.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
        tbBottoms.setTextColor(ContextCompat.getColor(this, R.color.black))

        tbZapatos.isChecked = false
        tbZapatos.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
        tbZapatos.setTextColor(ContextCompat.getColor(this, R.color.black))

        tbBodysuits.isChecked = false
        tbBodysuits.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
        tbBodysuits.setTextColor(ContextCompat.getColor(this, R.color.black))

        tbAccesorios.isChecked = false
        tbAccesorios.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
        tbAccesorios.setTextColor(ContextCompat.getColor(this, R.color.black))

        if (button.isChecked) {
            tbTops.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
            tbTops.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            tbTops.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
            tbTops.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    fun agregarTops() {
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "top"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "top"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "top"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "top"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "top"))
        articulos.add(Articulo("Top flores", R.drawable.top_rosa_flores, "top"))
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