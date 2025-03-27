package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mrudultora.colorpicker.ColorPickerPopUp
import com.mrudultora.colorpicker.ColorPickerPopUp.OnPickColorListener

class RegistrarPrendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_prenda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnColor: Button = findViewById(R.id.btnColor)
        val btnAgregarImagen: ImageButton = findViewById(R.id.ibAgregar)

        val defaultColor = Color.BLACK

        btnColor.setOnClickListener {
            val colorPickerPopUp = ColorPickerPopUp(it.context) // Usamos el contexto de la vista.
            colorPickerPopUp.setShowAlpha(true) // Por defecto muestra el alpha (transparencia).
                .setDefaultColor(defaultColor) // Color rojo por defecto.
                .setDialogTitle("Pick a Color")
                .setOnPickColorListener(object : OnPickColorListener {
                    override fun onColorPicked(color: Int) {
                        // Cambiar el color del botón al color seleccionado
                        btnColor.setBackgroundColor(color)
                    }

                    override fun onCancel() {
                        colorPickerPopUp.dismissDialog() // Cerrar el cuadro de diálogo.
                    }
                })
                .show()
        }

        btnAgregarImagen.setOnClickListener {
            try {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}