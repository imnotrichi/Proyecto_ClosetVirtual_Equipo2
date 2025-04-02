package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroDiarioActivity : MenuNavegable() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_diario)

        val btnRegistrarPrenda:Button = findViewById(R.id.btnRegisrarPrenda)
        val btnTop:Button = findViewById(R.id.btnSeleccionarTop)
        val btnBottom:Button = findViewById(R.id.btnSeleccionarBottom)
        val btnBodysuit:Button = findViewById(R.id.btnSeleccionarBodysuit)
        val btnZapatos:Button = findViewById(R.id.btnSeleccionarZapatos)
        val btnAccesorio1:Button = findViewById(R.id.btnSeleccionarAccesorios1)
        val btnAccesorio2:Button = findViewById(R.id.btnSeleccionarAccesorios2)
        val btnAccesorio3:Button = findViewById(R.id.btnSeleccionarAccesorios3)

        setupBottomNavigation()
        setSelectedItem(R.id.nav_hoy)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val categoria = data?.getStringExtra("categoria")
                val articulo = data?.getIntExtra("articulo", 0)

                when (categoria) {
                    "Tops" ->
                        btnTop.setBackgroundResource(articulo!!)
                }
            }
        }

        btnRegistrarPrenda.setOnClickListener {
            val intent = Intent(this, RegistrarPrendaActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnTop.setOnClickListener{
            val intent = Intent(this, PrendaEspecificaActivity::class.java)
            intent.putExtra("categoria", "Tops")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            launcher.launch(intent)
        }

        btnBottom.setOnClickListener{
            val intent = Intent(this, PrendaEspecificaActivity::class.java)
            intent.putExtra("categoria", "Bottoms")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnBodysuit.setOnClickListener{
            val intent = Intent(this, PrendaEspecificaActivity::class.java)
            intent.putExtra("categoria", "Bodysuits")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnZapatos.setOnClickListener{
            val intent = Intent(this, PrendaEspecificaActivity::class.java)
            intent.putExtra("categoria", "Zapatos")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnAccesorio1.setOnClickListener{
            val intent = Intent(this, PrendaEspecificaActivity::class.java)
            intent.putExtra("categoria", "Accesorios")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnAccesorio2.setOnClickListener{
            val intent = Intent(this, PrendaEspecificaActivity::class.java)
            intent.putExtra("categoria", "Accesorios")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnAccesorio3.setOnClickListener{
            val intent = Intent(this, PrendaEspecificaActivity::class.java)
            intent.putExtra("categoria", "Accesorios")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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