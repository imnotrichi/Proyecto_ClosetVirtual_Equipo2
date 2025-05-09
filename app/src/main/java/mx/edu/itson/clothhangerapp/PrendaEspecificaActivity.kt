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
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import mx.edu.itson.clothhangerapp.dataclases.PrendaPreviewItem
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel

class PrendaEspecificaActivity : AppCompatActivity() {
    private lateinit var viewModel: PrendasViewModel
    private lateinit var adapter: PrendaPreviewAdapter // Usará la clase anidada
    private var prendasMostradas = ArrayList<PrendaPreviewItem>() // Para el adaptador

    // Para guardar la lista original de Prenda y poder acceder a todos sus datos
    private var prendasOriginales = listOf<Prenda>()

    // Variables para recibir los extras del Intent
    private var categoriaRecibida: String? = null
    private var slotOrigenRecibido: String? = null // Necesitamos recibir y devolver esto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prenda_especifica) // Asigna el layout

        // Inicializar ViewModel
        viewModel = ViewModelProvider(this)[PrendasViewModel::class.java]

        // --- 1. Recibir datos del Intent ---
        categoriaRecibida = intent.getStringExtra("CATEGORIA")
        slotOrigenRecibido = intent.getStringExtra("SLOT_ORIGEN") // ¡Recibir el slot!

        Log.d("PrendaEspecifica", "Categoría recibida: $categoriaRecibida, Slot Origen: $slotOrigenRecibido")

        // Validar que recibimos la categoría Y EL SLOT
        if (categoriaRecibida.isNullOrEmpty() || slotOrigenRecibido.isNullOrEmpty()) {
            finish() // Cerrar si no hay datos suficientes
            return
        }

        // Configurar el título (labelCategoria es tu TextView)
        val labelCategoria: TextView = findViewById(R.id.tvCategoriaEspecifica)
        labelCategoria.text = categoriaRecibida // Mostrar la categoría

        // --- 2. Configurar GridView y Adaptador ---
        val gridView: GridView = findViewById(R.id.gvPrviewPrendas)
        adapter = PrendaPreviewAdapter(this, prendasMostradas)
        gridView.adapter = adapter

        // --- 3. Observar Cambios en las Prendas Filtradas del ViewModel ---
        viewModel.listaPrendasUsuario.observe(this) { prendasFirestore ->
            Log.d("PrendaEspecifica", "Prendas recibidas del ViewModel: ${prendasFirestore.size}")
            prendasOriginales = prendasFirestore // Guardar la lista original
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

        // Observar errores del ViewModel
        viewModel.errorMensaje.observe(this) { error ->
            error?.let {
                viewModel.limpiarMensajeError()
            }
        }

        // --- 4. Solicitar al ViewModel que cargue las prendas ---
        // (Asegúrate que cargarPorCategoria en tu ViewModel filtre por 'categoriaRecibida')
        viewModel.cargarPorCategoria(categoriaRecibida)

        // --- 5. Configurar el Listener para Devolver la Prenda Seleccionada (EN EL GRIDVIEW) ---
        gridView.setOnItemClickListener { _, _, position, _ ->
            if (position >= 0 && position < prendasOriginales.size) {
                // Obtener la PRENDA ORIGINAL completa para tener todos sus datos
                val prendaSeleccionadaOriginal = prendasOriginales[position]

                Log.d("PrendaEspecifica", "Prenda seleccionada: ${prendaSeleccionadaOriginal.nombre}, ID: ${prendaSeleccionadaOriginal.id}")

                val resultadoIntent = Intent()
                resultadoIntent.putExtra("PRENDA_SELECCIONADA_ID", prendaSeleccionadaOriginal.id)
                resultadoIntent.putExtra("PRENDA_SELECCIONADA_URL", prendaSeleccionadaOriginal.imagenUrl)
                // Devolver el slotOrigen que recibimos al iniciar esta Activity
                resultadoIntent.putExtra("SLOT_ORIGEN_DEVUELTO", slotOrigenRecibido)

                setResult(Activity.RESULT_OK, resultadoIntent)
                finish() // Cerrar y volver a RegistroDiarioActivity
            } else {
                Log.e("PrendaEspecifica", "Error: Posición de clic inválida ($position)")
            }
        }

        // Aquí iría tu setupBottomNavigation() si esta Activity también lo usa
        // setupBottomNavigation()
        // setSelectedItem(R.id.nav_home)
    } // Fin de onCreate


    // --- CLASE ADAPTADORA ANIDADA (CORREGIDA) ---
    private class PrendaPreviewAdapter(
        private val context: Context, // Cambiado a val y private para buenas prácticas
        private var prendas: ArrayList<PrendaPreviewItem> // Puede ser List si no la modificas aquí
    ) : BaseAdapter() {

        private val inflator: LayoutInflater = LayoutInflater.from(context)

        // ViewHolder para optimizar
        private class ViewHolder {
            lateinit var imagen: ImageView
            lateinit var nombre: TextView
        }

        override fun getCount(): Int {
            return prendas.size
        }

        override fun getItem(position: Int): Any {
            return prendas[position]
        }

        override fun getItemId(position: Int): Long {
            // Usar el hashcode del ID es más estable si los IDs son únicos y no nulos
            return prendas[position].id?.hashCode()?.toLong() ?: position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val vista: View
            val holder: ViewHolder

            if (convertView == null) {
                vista = inflator.inflate(R.layout.prenda_preview_item, parent, false) // Usa tu layout de item
                holder = ViewHolder()
                holder.imagen = vista.findViewById(R.id.prenda_image_preview) // ID de tu XML
                holder.nombre = vista.findViewById(R.id.prenda_titulo_preview) // ID de tu XML
                vista.tag = holder
            } else {
                vista = convertView
                holder = vista.tag as ViewHolder
            }

            val prendaItem = prendas[position] // Es un PrendaPreviewItem
            holder.nombre.text = prendaItem.nombre

            // Cargar imagen desde URL con Glide
            Glide.with(context) // No necesitas !! si el constructor asegura que no es nulo
                .load(prendaItem.imagenUrl) // Cargar desde imagenUrl
                .placeholder(R.drawable.ic_launcher_background) // ¡REEMPLAZA con tu placeholder!
                .error(R.drawable.ic_launcher_foreground)       // ¡REEMPLAZA con tu imagen de error!
                .into(holder.imagen)

            // --- NO HAY OnClickListener AQUÍ ---
            // El clic se maneja en el setOnItemClickListener del GridView en la Activity

            return vista
        }
    }
}