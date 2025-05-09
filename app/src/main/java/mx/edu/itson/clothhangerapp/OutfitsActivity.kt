package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import mx.edu.itson.clothhangerapp.dataclases.Outfit
import mx.edu.itson.clothhangerapp.viewmodels.OutfitsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class OutfitsActivity : MenuNavegable() {

    private lateinit var viewModel: OutfitsViewModel
    private lateinit var llOutfits: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfits)

        llOutfits = findViewById(R.id.llOutfits)
        setupBottomNavigation()
        setSelectedItem(R.id.nav_outfits)

        viewModel = ViewModelProvider(this)[OutfitsViewModel::class.java]
        viewModel.listaOutfitsUsuario.observe(this) { outfits ->
            if (!outfits.isNullOrEmpty()) {
                actualizarUIConOutfits(outfits)
            }
        }

        viewModel.obtenerOutfitsDelUsuario()

        findViewById<View>(R.id.btnCrearOutfit).setOnClickListener {
            val intent = Intent(this, RegistroDiarioActivity::class.java)
            startActivity(intent)
        }
    }

    private fun actualizarUIConOutfits(outfits: List<Outfit>) {
        llOutfits.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for (outfit in outfits) {
            val outfitView = inflater.inflate(R.layout.outfit_view, llOutfits, false)

            // Asignar imágenes
            setImagen(outfitView.findViewById(R.id.ivTop), outfit.top?.imagenUrl)
            setImagen(outfitView.findViewById(R.id.ivBodysuit), outfit.bodysuit?.imagenUrl)
            setImagen(outfitView.findViewById(R.id.ivBottom), outfit.bottom?.imagenUrl)
            setImagen(outfitView.findViewById(R.id.ivZapatos), outfit.zapatos?.imagenUrl)
            setImagen(outfitView.findViewById(R.id.ivAccesorio1), outfit.accesorio1?.imagenUrl)
            setImagen(outfitView.findViewById(R.id.ivAccesorio2), outfit.accesorio2?.imagenUrl)
            setImagen(outfitView.findViewById(R.id.ivAccesorio3), outfit.accesorio3?.imagenUrl)

            // Agregar listener para TODO el layout del outfit
            outfitView.findViewById<LinearLayout>(R.id.outfit).setOnClickListener {
                abrirDetalleOutfit(outfit)
            }

            llOutfits.addView(outfitView)
        }
    }

    private fun abrirDetalleOutfit(outfit: Outfit) {
        val intent = Intent(this, DetalleOutfitActivity::class.java).apply {


            val timestamp: Timestamp = outfit.fecha
            val date = timestamp.toDate()

            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = formatter.format(date)

            Log.d("DETALLE - OUTFIT", "Fecha: ${formattedDate}")
            putExtra("fecha", formattedDate)



            putExtra("top_name", outfit.top?.nombre ?: "")
            putExtra("top_image", outfit.top?.imagenUrl ?: "")
            putExtra("top_category", outfit.top?.categoria ?: "")

            putExtra("bodysuit_name", outfit.bodysuit?.nombre ?: "")
            putExtra("bodysuit_image", outfit.bodysuit?.imagenUrl ?: "")
            putExtra("bodysuit_category", outfit.bodysuit?.categoria ?: "")

            putExtra("bottom_name", outfit.bottom?.nombre ?: "")
            putExtra("bottom_image", outfit.bottom?.imagenUrl ?: "")
            putExtra("bottom_category", outfit.bottom?.categoria ?: "")

            putExtra("zapatos_name", outfit.zapatos?.nombre ?: "")
            putExtra("zapatos_image", outfit.zapatos?.imagenUrl ?: "")
            putExtra("zapatos_category", outfit.zapatos?.categoria ?: "")

            putExtra("accesorio1_name", outfit.accesorio1?.nombre ?: "")
            putExtra("accesorio1_image", outfit.accesorio1?.imagenUrl ?: "")
            putExtra("accesorio1_category", outfit.accesorio1?.categoria ?: "")

            putExtra("accesorio2_name", outfit.accesorio1?.nombre ?: "")
            putExtra("accesorio2_image", outfit.accesorio1?.imagenUrl ?: "")
            putExtra("accesorio2_category", outfit.accesorio1?.categoria ?: "")

            putExtra("accesorio3_name", outfit.accesorio1?.nombre ?: "")
            putExtra("accesorio3_image", outfit.accesorio1?.imagenUrl ?: "")
            putExtra("accesorio3_category", outfit.accesorio1?.categoria ?: "")
        }
        startActivity(intent)
    }

    private fun setImagen(imageView: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            imageView.visibility = View.VISIBLE
            Glide.with(this).load(url).into(imageView)
        } else {
            imageView.visibility = View.GONE
        }
    }
}
