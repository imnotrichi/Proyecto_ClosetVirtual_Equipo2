package mx.edu.itson.clothhangerapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallePrendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_prenda)

        val nombre = intent.getStringExtra("nombre")
        val imagen = intent.getIntExtra("imagen",0)
        val categoria = intent.getStringExtra("categoria")

        val ivImagen: ImageView = findViewById(R.id.ivImagen)
        val tvNombre: TextView = findViewById(R.id.tvNombre)
        val tvCategoria: TextView = findViewById(R.id.tvCategoria)

        ivImagen.setImageResource(imagen)
        tvNombre.text = nombre
        tvCategoria.text = categoria

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}