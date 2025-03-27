package mx.edu.itson.clothhangerapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrendaEspecificaActivity : AppCompatActivity() {

    lateinit var adapter: PrendaPreviewAdapter
    var prendas = ArrayList<PrendaPreviewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prenda_especifica)

        Toast.makeText(this, "HOLA DESDE EL GRID VIEW LISTENER", Toast.LENGTH_SHORT).show()
        val gridView:GridView = findViewById(R.id.gvPrviewPrendas)
        val categoria = intent.getStringExtra("categoria")

        when (categoria) {
            "Zapatos" -> {
                cargarZapatos()
                Toast.makeText(this, "HOLA DESDE EL WHEN CATEGORIA", Toast.LENGTH_SHORT).show()
            }
            "Tops" -> cargarTops()
            "Bottoms" -> cargarBottoms()
            "Bodysuits" -> cargarTops()
            "Accesorios" -> cargarTops()
        }

        adapter = PrendaPreviewAdapter(this, prendas)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "HOLA DESDE EL GRID VIEW LISTENER", Toast.LENGTH_SHORT).show()
            val selectedItem = prendas[position]
            val resultIntent = Intent().apply {
                putExtra("categoria", categoria)
                putExtra("articulo", selectedItem.imagen)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
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
