package mx.edu.itson.clothhangerapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfiguracionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener las referencias del botón "Guardar Cambios" y el layout del diálogo
        val btnGuardarCambios: Button = findViewById(R.id.btnGuardarCambios)
        val layoutDialogo: LinearLayout = findViewById(R.id.layoutDialogo)

        // Configurar el listener del botón "Guardar Cambios"
        btnGuardarCambios.setOnClickListener {
            // Cambiar la visibilidad del layout del diálogo a visible
            layoutDialogo.visibility = View.VISIBLE
        }

        // Manejo de los botones "Sí" y "No" dentro del diálogo
        val btnSi: Button = findViewById(R.id.btnSi)
        val btnNo: Button = findViewById(R.id.btnNo)

        btnSi.setOnClickListener {
            // Acción al presionar "Sí" (guardar cambios)
            // Aquí puedes agregar el código para guardar los cambios o cualquier otra lógica que necesites
            layoutDialogo.visibility = View.GONE  // Ocultar el diálogo
        }

        btnNo.setOnClickListener {
            layoutDialogo.visibility = View.GONE  // Ocultar el diálogo
        }
    }
}