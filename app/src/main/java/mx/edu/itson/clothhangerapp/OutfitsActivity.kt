package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.dataclases.Outfit
import mx.edu.itson.clothhangerapp.viewmodels.OutfitsViewModel

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
            putExtra("fecha", outfit.fecha)
            putExtra("top", outfit.top?.imagenUrl ?: "")
            putExtra("bodysuit", outfit.bodysuit?.imagenUrl ?: "")
            putExtra("bottom", outfit.bottom?.imagenUrl ?: "")
            putExtra("zapatos", outfit.zapatos?.imagenUrl ?: "")
            putExtra("accesorio1", outfit.accesorio1?.imagenUrl ?: "")
            putExtra("accesorio2", outfit.accesorio2?.imagenUrl ?: "")
            putExtra("accesorio3", outfit.accesorio3?.imagenUrl ?: "")
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
