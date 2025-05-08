package mx.edu.itson.clothhangerapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallePrendaActivity : MenuNavegable() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_prenda)

        setupBottomNavigation()

        val nombre = intent.getStringExtra("nombre")
        val imagen = intent.getIntExtra("imagen",0)
        val categoria = intent.getStringExtra("categoria")
        val color = intent.getStringExtra("color")
        val estampado = intent.getStringExtra("estampado")
        val tags = intent.getStringExtra("tags")
        val totalUsos = intent.getStringExtra("totalUsos")
        val usosMes = intent.getStringExtra("usosMes")

        val ivImagen: ImageView = findViewById(R.id.ivImagen)
        val tvNombre: TextView = findViewById(R.id.tvNombre)
        val tvCategoria: TextView = findViewById(R.id.tvCategoria)
        val tvColor: TextView = findViewById(R.id.tvColor)
        val tvEstampado: TextView = findViewById(R.id.tvEstampado)
        val tvTags: TextView = findViewById(R.id.tvTags)
        val tvTotalUsos: TextView = findViewById(R.id.tvTotalUsos)
        val tvUsosMes: TextView = findViewById(R.id.tvUsosMes)

        ivImagen.setImageResource(imagen)
        tvNombre.text = nombre
        tvCategoria.text = categoria
        tvColor.text = color
        tvEstampado.text = estampado
        tvTags.text = tags
        tvTotalUsos.text = totalUsos
        tvUsosMes.text = usosMes
    }
}