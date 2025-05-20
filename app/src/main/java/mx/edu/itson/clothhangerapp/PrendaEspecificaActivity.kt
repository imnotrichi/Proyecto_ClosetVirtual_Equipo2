package mx.edu.itson.clothhangerapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import mx.edu.itson.clothhangerapp.dataclases.PrendaPreviewItem
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel

class PrendaEspecificaActivity : MenuNavegable() {
    private lateinit var viewModel: PrendasViewModel
    private lateinit var adapter: PrendaPreviewAdapter
    private lateinit var btnAgregarPrenda: Button
    private var prendasMostradas = ArrayList<PrendaPreviewItem>()
    private var prendasOriginales = listOf<Prenda>()
    private var categoriaRecibida: String? = null
    private var slotOrigenRecibido: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prenda_especifica)

        setupBottomNavigation()

        viewModel = ViewModelProvider(this)[PrendasViewModel::class.java]
        btnAgregarPrenda = findViewById(R.id.btnAgregarPrenda)

        categoriaRecibida = intent.getStringExtra("CATEGORIA")
        slotOrigenRecibido = intent.getStringExtra("SLOT_ORIGEN")

        Log.d("PrendaEspecifica", "Categoría recibida: $categoriaRecibida, Slot Origen: $slotOrigenRecibido")

        if (categoriaRecibida.isNullOrEmpty() || slotOrigenRecibido.isNullOrEmpty()) {
            // Es buena idea mostrar un Toast aquí también antes de cerrar
            finish()
            return
        }

        val labelCategoria: TextView = findViewById(R.id.tvCategoriaEspecifica)
        labelCategoria.text = categoriaRecibida

        val gridView: GridView = findViewById(R.id.gvPrviewPrendas)
        // Se inicializa el adapter con la lista 'prendasMostradas' que se llenará con el observer
        adapter = PrendaPreviewAdapter(this, prendasMostradas)
        gridView.adapter = adapter // Se asigna el adapter al GridView

        // Configurar observadores
        viewModel.listaPrendasUsuario.observe(this) { prendasFirestore ->
            Log.d("PrendaEspecifica", "Prendas recibidas del ViewModel: ${prendasFirestore.size}")
            prendasOriginales = prendasFirestore
            prendasMostradas.clear()
            prendasFirestore.forEach { prendaOriginal ->
                prendasMostradas.add(
                    PrendaPreviewItem(
                        id = prendaOriginal.id,
                        imagenUrl = prendaOriginal.imagenUrl,
                        nombre = prendaOriginal.nombre
                    )
                )
            }
            adapter.notifyDataSetChanged()
        }

        viewModel.errorMensaje.observe(this) { error ->
            error?.let {
                viewModel.limpiarMensajeError() // Asume que tienes este método
            }
        }

        // Solicitar datos al ViewModel
        viewModel.cargarPorCategoria(categoriaRecibida)

        // Configurar listener del GridView para devolver resultado
        gridView.setOnItemClickListener { _, _, position, _ ->
            if (position >= 0 && position < prendasOriginales.size) {
                val prendaSeleccionadaOriginal = prendasOriginales[position]
                Log.d("PrendaEspecifica", "Prenda seleccionada: ${prendaSeleccionadaOriginal.nombre}, ID: ${prendaSeleccionadaOriginal.id}")
                val resultadoIntent = Intent()
                resultadoIntent.putExtra("PRENDA_SELECCIONADA_ID", prendaSeleccionadaOriginal.id)
                resultadoIntent.putExtra("PRENDA_SELECCIONADA_URL", prendaSeleccionadaOriginal.imagenUrl)
                resultadoIntent.putExtra("SLOT_ORIGEN_DEVUELTO", slotOrigenRecibido)
                setResult(Activity.RESULT_OK, resultadoIntent)
                finish()
            } else {
                Log.e("PrendaEspecifica", "Error: Posición de clic inválida ($position)")
            }
        }

        btnAgregarPrenda.setOnClickListener {
            val intent = Intent(this, RegistrarPrendaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    } // --- FIN DE onCreate ---


    // --- CLASE ADAPTADORA ANIDADA ---
    // Esta clase está DENTRO de PrendaEspecificaActivity, por eso es 'private class'
    // Si la quisieras usar en otras Activities, deberías moverla a su propio archivo .kt en el paquete 'adapters'
    private class PrendaPreviewAdapter(
        private val context: Context,
        private var prendas: ArrayList<PrendaPreviewItem>
    ) : BaseAdapter() {

        private val inflator: LayoutInflater = LayoutInflater.from(context)

        private class ViewHolder {
            lateinit var imagen: ImageView
            lateinit var nombre: TextView
        }

        override fun getCount(): Int { return prendas.size }
        override fun getItem(position: Int): Any { return prendas[position] }
        override fun getItemId(position: Int): Long {
            return prendas[position].id?.hashCode()?.toLong() ?: position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val vista: View
            val holder: ViewHolder

            if (convertView == null) {
                vista = inflator.inflate(R.layout.prenda_preview_item, parent, false)
                holder = ViewHolder()
                holder.imagen = vista.findViewById(R.id.prenda_image_preview)
                holder.nombre = vista.findViewById(R.id.prenda_titulo_preview)
                vista.tag = holder
            } else {
                vista = convertView
                holder = vista.tag as ViewHolder
            }

            val prendaItem = prendas[position]
            holder.nombre.text = prendaItem.nombre

            Glide.with(context)
                .load(prendaItem.imagenUrl)
                .placeholder(R.drawable.ic_launcher_background) // CAMBIA ESTO
                .error(R.drawable.ic_launcher_foreground)       // CAMBIA ESTO
                .into(holder.imagen)

            return vista
        }
    } // --- FIN DE PrendaPreviewAdapter ---

}