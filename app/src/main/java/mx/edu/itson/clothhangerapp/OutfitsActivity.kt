package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class OutfitsActivity : MenuNavegable() {
    var outfits: ArrayList<Outfit> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfits)

        val llOutfits: LinearLayout =
            findViewById(R.id.llOutfits) // Dentro de HorizontalScrollView en XML

        setupBottomNavigation()
        setSelectedItem(R.id.nav_outfits)

        agregarOutfits()

        // Agregar dinámicamente las vistas de los outfits al LinearLayout
        val inflater = LayoutInflater.from(this)
        for (outfit in outfits) {
            val outfitView = inflater.inflate(R.layout.outfit_view, llOutfits, false)

            setFecha(outfitView.findViewById(R.id.tvFechaOutfit), outfit.fecha)
            setImagen(outfitView.findViewById(R.id.ivTop), outfit.top)
            setImagen(outfitView.findViewById(R.id.ivBodysuit), outfit.bodysuit)
            setImagen(outfitView.findViewById(R.id.ivBottom), outfit.bottom)
            setImagen(outfitView.findViewById(R.id.ivZapatos), outfit.zapatos)
            setImagen(outfitView.findViewById(R.id.ivAccesorio1), outfit.accesorio1)
            setImagen(outfitView.findViewById(R.id.ivAccesorio2), outfit.accesorio2)
            setImagen(outfitView.findViewById(R.id.ivAccesorio3), outfit.accesorio3)

            outfitView.findViewById<ImageView>(R.id.ivTop).setOnClickListener {
                abrirDetalleOutfit(outfit, "Top")
            }


            // Agregar evento de clic para abrir la pantalla de detalles
            outfitView.setOnClickListener {
                val intent = Intent(this, DetalleOutfitActivity::class.java).apply {
                    putExtra("fecha", outfit.fecha.timeInMillis)
                    putExtra("top", outfit.top?.imagen ?: -1)
                    putExtra("bodysuit", outfit.bodysuit?.imagen ?: -1)
                    putExtra("bottom", outfit.bottom?.imagen ?: -1)
                    putExtra("zapatos", outfit.zapatos?.imagen ?: -1)
                    putExtra("accesorio1", outfit.accesorio1?.imagen ?: -1)
                    putExtra("accesorio2", outfit.accesorio2?.imagen ?: -1)
                    putExtra("accesorio3", outfit.accesorio3?.imagen ?: -1)
                }
                startActivity(intent)
            }

            llOutfits.addView(outfitView)
        }
    }

    private fun abrirDetalleOutfit(outfit: Outfit, parte: String) {
        val intent = Intent(this, DetalleOutfitActivity::class.java).apply {
            putExtra("fecha", outfit.fecha.timeInMillis)
            putExtra("parteSeleccionada", parte) // Para identificar la imagen clicada
            putExtra("top", outfit.top?.imagen ?: -1)
            putExtra("bodysuit", outfit.bodysuit?.imagen ?: -1)
            putExtra("bottom", outfit.bottom?.imagen ?: -1)
            putExtra("zapatos", outfit.zapatos?.imagen ?: -1)
            putExtra("accesorio1", outfit.accesorio1?.imagen ?: -1)
            putExtra("accesorio2", outfit.accesorio2?.imagen ?: -1)
            putExtra("accesorio3", outfit.accesorio3?.imagen ?: -1)
        }
        startActivity(intent)
    }

    fun agregarOutfits() {
        outfits.add(
            Outfit(
                Calendar.getInstance(),
                Articulo("Top rosa", R.drawable.top_rosa_flores, "Verano"),
                null,
                Articulo("Falda de mezclilla", R.drawable.falda_mezclilla, "Verano"),
                Articulo("Zapatitos rojo", R.drawable.zapatitos_rojos, "Verano"),
                null,
                null,
                null
            )
        )
        outfits.add(
            Outfit(
                Calendar.getInstance(),
                Articulo("Perro El Shirota", R.drawable.perro, "Verano"),
                null,
                Articulo("Pantalones negros", R.drawable.pantalones_negros, "Verano"),
                Articulo("Crocs Rayo McQueen", R.drawable.crocs, "Verano"),
                Articulo("Lentes de broma", R.drawable.broma, "Cute"),
                null,
                null
            )
        )
        outfits.add(
            Outfit(
                Calendar.getInstance(),
                Articulo("Top rosita", R.drawable.top_rosita, "Verano"),
                null,
                Articulo("Pantalones cafes", R.drawable.pantalones_cafes, "Verano"),
                Articulo("Tenis verdes", R.drawable.zapatos_verdes, "Verano"),
                Articulo("Aretes de gato", R.drawable.aretes_gato, "Cute"),
                null,
                null
            )
        )
        outfits.add(
            Outfit(
                Calendar.getInstance(),
                null,
                Articulo("Bodysuit negro", R.drawable.bodysuit_negro, "Verano"),
                Articulo("Botas negras", R.drawable.botas, "Verano"),
                null,
                null,
                null,
                null
            )
        )
    }

    // Función para asignar imagen o esconder ImageView
    private fun setImagen(imageView: ImageView, articulo: Articulo?) {
        if (articulo != null) {
            imageView.setImageResource(articulo.imagen)
            imageView.visibility = View.VISIBLE
        } else {
            imageView.visibility = View.GONE
        }
    }

    private fun setFecha(tvFecha: TextView, fecha: Calendar) {
        val format =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())  // El formato puede ser ajustado
        val formattedDate = format.format(fecha.time)  // Convierte la fecha a String
        tvFecha.text = formattedDate  // Establece el texto en el TextView
    }
}
