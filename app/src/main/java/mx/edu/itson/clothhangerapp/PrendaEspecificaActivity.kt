package mx.edu.itson.clothhangerapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrendaEspecificaActivity : AppCompatActivity() {

    lateinit var gridView: GridView
    lateinit var adapter: PrendaPreviewAdapter
    val prendas = ArrayList<PrendaPreviewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prenda_especifica)

        val category = intent.getStringExtra("CATEGORY") ?: return
        gridView = findViewById(R.id.prendasPreviewGridView)

        when (category) {
            "shoes" -> cargarZapatos()
            "top" -> cargarTops()
            "pants" -> cargarBottoms()
        }

        adapter = PrendaPreviewAdapter(this, prendas)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = prendas[position]

            val resultIntent = Intent().apply {
                putExtra("CATEGORY", category)
                putExtra("SELECTED_ITEM", selectedItem.imagen)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarZapatos() {
        prendas.clear()
        prendas.add(PrendaPreviewItem(R.drawable.zapatilla_roja, "Zapatilla Roja"))
        prendas.add(PrendaPreviewItem(R.drawable.zapatos_verdes, "Zapatilla Negra"))
    }

    private fun cargarTops() {
        prendas.clear()
        prendas.add(PrendaPreviewItem(R.drawable.prenda_floreada, "Blusa Floreada"))
        prendas.add(PrendaPreviewItem(R.drawable.top_rosita, "Top Negro"))
    }

    private fun cargarBottoms() {
        prendas.clear()
        prendas.add(PrendaPreviewItem(R.drawable.pantalon_mezclilla, "Pantalón Mezclilla"))
        prendas.add(PrendaPreviewItem(R.drawable.pantalones_cafes, "Pantalón Negro"))
    }
}