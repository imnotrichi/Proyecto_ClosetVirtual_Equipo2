package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroDiarioActivity : MenuNavegable() {

    private val selectedClothingItems = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_diario)

        val categoriesMap = mapOf(
            R.id.btnSeleccionarZapatos to "shoes",
            R.id.btnSeleccionarTop to "top",
            R.id.btnSeleccionarBottom to "pants"
        )

        val clothingSelectionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val category = data?.getStringExtra("CATEGORY")
                val selectedItemResource = data?.getIntExtra("SELECTED_ITEM", -1)

                if (category != null && selectedItemResource != null && selectedItemResource != -1) {
                    // Guardar el recurso de imagen seleccionado
                    selectedClothingItems[category] = selectedItemResource

                    // Actualizar el background del botón correspondiente
                    val buttonId = when(category) {
                        "shoes" -> R.id.btnSeleccionarZapatos
                        "top" -> R.id.btnSeleccionarTop
                        "pants" -> R.id.btnSeleccionarBottom
                        else -> return@registerForActivityResult
                    }

                    val button = findViewById<Button>(buttonId)
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                        ContextCompat.getDrawable(this, selectedItemResource))
                }
            }
        }

        // Configurar OnClickListener para cada botón de categoría
        categoriesMap.forEach { (buttonId, category) ->
            findViewById<Button>(buttonId).setOnClickListener {
                val intent = Intent(this, CatalogoPrendaEspecifica::class.java).apply {
                    putExtra("CATEGORY", category)
                }
                clothingSelectionLauncher.launch(intent)
            }
        }

        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)

        btnConfirmar.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}