package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ToggleButton
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

        val btnAgregarImagen: ImageButton = findViewById(R.id.ibAgregar)

        val etNombrePrenda:EditText = findViewById(R.id.etNombrePrenda)

        val ibTop:ImageButton = findViewById(R.id.ibTop)
        val ibBottom:ImageButton = findViewById(R.id.ibBottom)
        val ibZapatos:ImageButton = findViewById(R.id.ibZapatos)
        val ibBodysuit:ImageButton = findViewById(R.id.ibBodysuit)
        val ibAccesorio:ImageButton = findViewById(R.id.ibAccesorio)

        val tbSi:ToggleButton = findViewById(R.id.tbSi)
        val tbNo:ToggleButton = findViewById(R.id.tbNo)

        val btnColor: Button = findViewById(R.id.btnColor)

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