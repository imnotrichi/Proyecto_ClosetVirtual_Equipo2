package mx.edu.itson.clothhangerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import mx.edu.itson.clothhangerapp.dataclases.Articulo

class PrincipalActivity : MenuNavegable() {

    private var articulos:ArrayList<Articulo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        var tbTops = findViewById<ImageButton>(R.id.tbTops)
        var tbBottoms = findViewById<ImageButton>(R.id.tbBottoms)
        var tbZapatos = findViewById<ImageButton>(R.id.tbZapatos)
        var tbBodysuits = findViewById<ImageButton>(R.id.tbBodysuits)
        var tbAccesorios = findViewById<ImageButton>(R.id.tbAccesorios)

        var lvArticulos:ListView = findViewById(R.id.lvArticulos)
        var llCategoria = findViewById<LinearLayout>(R.id.llCategoria)
        var tvCategoria = findViewById<TextView>(R.id.tvCategoria)

        setupBottomNavigation()
        setSelectedItem(R.id.nav_home)

        val btnRegistrarPrenda:Button = findViewById(R.id.btnRegisrarPrenda)

        btnRegistrarPrenda.setOnClickListener {
            val intent = Intent(this, RegistrarPrendaActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        var isTopsChecked = false
        var isBottomsChecked = false
        var isZapatosChecked = false
        var isBodysuitsChecked = false
        var isAccesoriosChecked = false

        tbTops.setOnClickListener {
            isTopsChecked = !isTopsChecked
            if (isTopsChecked) {
                llCategoria.visibility = View.VISIBLE
                tvCategoria.text = "Tops"

                tbBottoms.setImageResource(R.drawable.icono_bottoms_negro)
                tbBottoms.setBackgroundResource(R.drawable.tag_view_off)

                tbZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                tbZapatos.setBackgroundResource(R.drawable.tag_view_off)

                tbBodysuits.setImageResource(R.drawable.icono_bodysuits_negro)
                tbBodysuits.setBackgroundResource(R.drawable.tag_view_off)

                tbAccesorios.setImageResource(R.drawable.icono_accesorios_negro)
                tbAccesorios.setBackgroundResource(R.drawable.tag_view_off)

                isAccesoriosChecked = false
                isBottomsChecked = false
                isZapatosChecked = false
                isBodysuitsChecked = false

                articulos.clear()
                agregarTops()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbTops.setImageResource(R.drawable.icono_tops_blanco)
                tbTops.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                llCategoria.visibility = View.INVISIBLE

                articulos.clear()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbTops.setImageResource(R.drawable.icono_tops_negro)
                tbTops.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbBottoms.setOnClickListener {
            isBottomsChecked = !isBottomsChecked
            if (isBottomsChecked) {
                llCategoria.visibility = View.VISIBLE
                tvCategoria.text = "Bottoms"

                tbTops.setImageResource(R.drawable.icono_tops_negro)
                tbTops.setBackgroundResource(R.drawable.tag_view_off)

                tbZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                tbZapatos.setBackgroundResource(R.drawable.tag_view_off)

                tbBodysuits.setImageResource(R.drawable.icono_bodysuits_negro)
                tbBodysuits.setBackgroundResource(R.drawable.tag_view_off)

                tbAccesorios.setImageResource(R.drawable.icono_accesorios_negro)
                tbAccesorios.setBackgroundResource(R.drawable.tag_view_off)

                isTopsChecked = false
                isAccesoriosChecked = false
                isZapatosChecked = false
                isBodysuitsChecked = false

                articulos.clear()
                agregarBottoms()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbBottoms.setImageResource(R.drawable.icono_bottoms_blanco)
                tbBottoms.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                llCategoria.visibility = View.INVISIBLE

                articulos.clear()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbBottoms.setImageResource(R.drawable.icono_bottoms_negro)
                tbBottoms.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbZapatos.setOnClickListener {
            isZapatosChecked = !isZapatosChecked
            if (isZapatosChecked) {
                llCategoria.visibility = View.VISIBLE
                tvCategoria.text = "Zapatos"

                tbBottoms.setImageResource(R.drawable.icono_bottoms_negro)
                tbBottoms.setBackgroundResource(R.drawable.tag_view_off)

                tbTops.setImageResource(R.drawable.icono_tops_negro)
                tbTops.setBackgroundResource(R.drawable.tag_view_off)

                tbBodysuits.setImageResource(R.drawable.icono_bodysuits_negro)
                tbBodysuits.setBackgroundResource(R.drawable.tag_view_off)

                tbAccesorios.setImageResource(R.drawable.icono_accesorios_negro)
                tbAccesorios.setBackgroundResource(R.drawable.tag_view_off)

                isTopsChecked = false
                isBottomsChecked = false
                isAccesoriosChecked = false
                isBodysuitsChecked = false

                articulos.clear()
                agregarZapatos()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbZapatos.setImageResource(R.drawable.icono_zapatos_blanco)
                tbZapatos.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                llCategoria.visibility = View.INVISIBLE

                articulos.clear()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                tbZapatos.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbBodysuits.setOnClickListener {
            isBodysuitsChecked = !isBodysuitsChecked
            if (isBodysuitsChecked) {
                llCategoria.visibility = View.VISIBLE
                tvCategoria.text = "Bodysuits"

                tbBottoms.setImageResource(R.drawable.icono_bottoms_negro)
                tbBottoms.setBackgroundResource(R.drawable.tag_view_off)

                tbZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                tbZapatos.setBackgroundResource(R.drawable.tag_view_off)

                tbTops.setImageResource(R.drawable.icono_tops_negro)
                tbTops.setBackgroundResource(R.drawable.tag_view_off)

                tbAccesorios.setImageResource(R.drawable.icono_accesorios_negro)
                tbAccesorios.setBackgroundResource(R.drawable.tag_view_off)

                isTopsChecked = false
                isBottomsChecked = false
                isZapatosChecked = false
                isAccesoriosChecked = false

                articulos.clear()
                agregarBodysuits()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbBodysuits.setImageResource(R.drawable.icono_bodysuit_blanco)
                tbBodysuits.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                llCategoria.visibility = View.INVISIBLE

                articulos.clear()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbBodysuits.setImageResource(R.drawable.icono_bodysuits_negro)
                tbBodysuits.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbAccesorios.setOnClickListener {
            isAccesoriosChecked = !isAccesoriosChecked
            if (isAccesoriosChecked) {
                llCategoria.visibility = View.VISIBLE
                tvCategoria.text = "Accesorios"

                tbBottoms.setImageResource(R.drawable.icono_bottoms_negro)
                tbBottoms.setBackgroundResource(R.drawable.tag_view_off)

                tbZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                tbZapatos.setBackgroundResource(R.drawable.tag_view_off)

                tbBodysuits.setImageResource(R.drawable.icono_bodysuits_negro)
                tbBodysuits.setBackgroundResource(R.drawable.tag_view_off)

                tbTops.setImageResource(R.drawable.icono_tops_negro)
                tbTops.setBackgroundResource(R.drawable.tag_view_off)

                isTopsChecked = false
                isBottomsChecked = false
                isZapatosChecked = false
                isBodysuitsChecked = false

                articulos.clear()
                agregarAccesorios()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbAccesorios.setImageResource(R.drawable.icono_accesorios_blanco)
                tbAccesorios.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                llCategoria.visibility = View.INVISIBLE

                articulos.clear()

                var adapatador = AdaptadorArticulos(this, articulos)
                lvArticulos.adapter = adapatador

                tbAccesorios.setImageResource(R.drawable.icono_accesorios_negro)
                tbAccesorios.setBackgroundResource(R.drawable.tag_view_off)
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