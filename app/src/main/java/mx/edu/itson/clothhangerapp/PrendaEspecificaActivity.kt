package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel

class PrendaEspecificaActivity : AppCompatActivity() {
    private lateinit var viewModel: PrendasViewModel
    lateinit var adapter: PrendaPreviewAdapter
    var prendas = ArrayList<PrendaPreviewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[PrendasViewModel::class.java]
        viewModel.listaPrendasUsuario.observe(this) { prendasFirestore ->
            prendas.clear()
            prendasFirestore.forEach {
                // Aquí debes adaptar tu objeto Prenda a PrendaPreviewItem
                prendas.add(PrendaPreviewItem(it.imagenUrl, it.nombre)) // Usa un drawable placeholder o adapta según datos
            }
            adapter.notifyDataSetChanged()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prenda_especifica)

        val gridView:GridView = findViewById(R.id.gvPrviewPrendas)
        val categoria = intent.getStringExtra("categoria")
        println(categoria)

        when (categoria) {
            "Zapatos", "Top", "Bottoms", "Bodysuits", "Accesorios" -> viewModel.cargarPorCategoria(categoria)
            else -> viewModel.cargarPorCategoria(null) // O podrías mostrar un mensaje de error
        }

        adapter = PrendaPreviewAdapter(this, prendas)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, RegistroDiario2Activity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
