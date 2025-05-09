package mx.edu.itson.clothhangerapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel

class PrincipalActivity : MenuNavegable() {

    private lateinit var viewModel: PrendasViewModel
    private lateinit var lvArticulos: ListView
    private lateinit var adaptador: AdaptadorPrendas

    private lateinit var tbTops: ImageButton
    private lateinit var tbBottoms: ImageButton
    private lateinit var tbZapatos: ImageButton
    private lateinit var tbBodysuits: ImageButton
    private lateinit var tbAccesorios: ImageButton

    private lateinit var tvCategoria: TextView
    private lateinit var llCategoria: LinearLayout
    private lateinit var etBuscar: EditText
    private lateinit var btnRegistrarPrenda: Button

    private var categoriaSeleccionada: String? = null

    private var listaCompletaPrendas: List<Prenda> = emptyList()

    private lateinit var detallePrendaLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        viewModel = ViewModelProvider(this)[PrendasViewModel::class.java]

        detallePrendaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Este bloque se ejecutará si DetallePrendaActivity (o RegistrarPrendaActivity si fue una edición)
                // llamó a setResult(Activity.RESULT_OK) antes de finish().
                // Esto significa que una prenda fue borrada o editada.
                Log.d("PrincipalActivity", "Resultado OK recibido. Recargando lista de prendas.")
                viewModel.obtenerPrendasDelUsuario() // Vuelve a cargar las prendas
            }
        }


        // Inicialización de vistas
        lvArticulos = findViewById(R.id.lvArticulos)
        tbTops = findViewById(R.id.tbTops)
        tbBottoms = findViewById(R.id.tbBottoms)
        tbZapatos = findViewById(R.id.tbZapatos)
        tbBodysuits = findViewById(R.id.tbBodysuits)
        tbAccesorios = findViewById(R.id.tbAccesorios)
        tvCategoria = findViewById(R.id.tvCategoria)
        llCategoria = findViewById(R.id.llCategoria)
        etBuscar = findViewById(R.id.etBuscar)
        btnRegistrarPrenda = findViewById(R.id.btnRegistrarPrenda)

        setupBottomNavigation()
        setSelectedItem(R.id.nav_home)

        adaptador = AdaptadorPrendas(this, ArrayList())
        lvArticulos.adapter = adaptador

        // ✅ Cargar todas las prendas desde el inicio
        viewModel.cargarTodas()
        llCategoria.visibility = View.GONE

        lvArticulos.setOnItemClickListener { parent, view, position, id ->
            val prendaSeleccionada = adaptador.getItem(position) as? Prenda // Cast seguro
            prendaSeleccionada?.let {
                val intent = Intent(this, DetallePrendaActivity::class.java)
                intent.putExtra("PRENDA_DETALLE", it)
                detallePrendaLauncher.launch(intent) // <<< --- CAMBIA startActivity(intent) POR ESTO
            }
        }

        viewModel.listaPrendasUsuario.observe(this) {
            listaCompletaPrendas = it // guarda todas las prendas cargadas
            aplicarFiltros()
        }

        // Botones de categoría
        tbTops.setOnClickListener { manejarSeleccionCategoria("Top", tbTops, "Tops") }
        tbBottoms.setOnClickListener { manejarSeleccionCategoria("Bottom", tbBottoms, "Bottoms") }
        tbZapatos.setOnClickListener { manejarSeleccionCategoria("Zapatos", tbZapatos, "Zapatos") }
        tbBodysuits.setOnClickListener {
            manejarSeleccionCategoria(
                "Bodysuit",
                tbBodysuits,
                "Bodysuits"
            )
        }
        tbAccesorios.setOnClickListener {
            manejarSeleccionCategoria(
                "Accesorio",
                tbAccesorios,
                "Accesorios"
            )
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aplicarFiltros()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnRegistrarPrenda.setOnClickListener {
            val intent = Intent(this, RegistrarPrendaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        llCategoria.visibility = View.GONE
        viewModel.cargarTodas()
    }

    private fun aplicarFiltros() {
        val textoBusqueda = etBuscar.text.toString().trim().lowercase()

        val filtradas = listaCompletaPrendas.filter { prenda ->
            (categoriaSeleccionada == null || prenda.categoria.equals(
                categoriaSeleccionada,
                ignoreCase = true
            )) &&
                    (textoBusqueda.isEmpty() ||
                            prenda.nombre.lowercase().contains(textoBusqueda) ||
                            prenda.etiquetas.any { it.lowercase().contains(textoBusqueda) })
        }

        adaptador.actualizarLista(filtradas)
    }


    private fun manejarSeleccionCategoria(categoria: String, boton: ImageButton, texto: String) {
        if (categoriaSeleccionada == categoria) {
            categoriaSeleccionada = null
            actualizarEstilosBotones(null)
            llCategoria.visibility = View.GONE
        } else {
            categoriaSeleccionada = categoria
            actualizarEstilosBotones(boton)
            tvCategoria.text = texto
            llCategoria.visibility = View.VISIBLE
        }
        aplicarFiltros()
    }

    private fun actualizarEstilosBotones(botonSeleccionado: ImageButton?) {
        val botones = listOf(
            tbTops to R.drawable.icono_tops_negro,
            tbBottoms to R.drawable.icono_bottoms_negro,
            tbZapatos to R.drawable.icono_zapatos_negro,
            tbBodysuits to R.drawable.icono_bodysuits_negro,
            tbAccesorios to R.drawable.icono_accesorios_negro
        )

        for ((boton, iconoNegro) in botones) {
            if (boton == botonSeleccionado) {
                boton.setImageResource(obtenerIconoBlanco(iconoNegro))
                boton.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                boton.setImageResource(iconoNegro)
                boton.setBackgroundResource(R.drawable.tag_view_off)
            }
        }
    }

    private fun obtenerIconoBlanco(iconoNegro: Int): Int {
        return when (iconoNegro) {
            R.drawable.icono_tops_negro -> R.drawable.icono_tops_blanco
            R.drawable.icono_bottoms_negro -> R.drawable.icono_bottoms_blanco
            R.drawable.icono_zapatos_negro -> R.drawable.icono_zapatos_blanco
            R.drawable.icono_bodysuits_negro -> R.drawable.icono_bodysuit_blanco
            R.drawable.icono_accesorios_negro -> R.drawable.icono_accesorios_blanco
            else -> iconoNegro
        }
    }

    private class AdaptadorPrendas(
        val contexto: Context,
        val prendas: ArrayList<Prenda>
    ) : BaseAdapter() {

        override fun getCount(): Int = prendas.size
        override fun getItem(position: Int): Any = prendas[position]
        override fun getItemId(position: Int): Long = position.toLong()

        fun actualizarLista(nuevaLista: List<Prenda>) {
            prendas.clear()
            prendas.addAll(nuevaLista)
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val prenda = prendas[position]
            val inflador = LayoutInflater.from(contexto)
            val vista = inflador.inflate(R.layout.prenda_view, parent, false)

            val imagen = vista.findViewById<ImageView>(R.id.ivImagenPrenda)
            val nombre = vista.findViewById<TextView>(R.id.tvNombrePrenda)
            val categoria = vista.findViewById<TextView>(R.id.tvCategoriaPrenda)

            Glide.with(contexto)
                .load(prenda.imagenUrl) // URL de la imagen
                .into(imagen)
            nombre.text = prenda.nombre
            categoria.text = prenda.categoria

            return vista
        }
    }
}
