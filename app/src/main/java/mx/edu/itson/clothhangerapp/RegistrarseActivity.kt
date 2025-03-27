package mx.edu.itson.clothhangerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistrarseActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnIniciarSesion: Button = findViewById(R.id.btnIniciarSesion)
        val textBoton = "iniciar sesión"
        val textBotonSpannableString = SpannableString(textBoton)
        textBotonSpannableString.setSpan(UnderlineSpan(), 0, textBotonSpannableString.length, 0)
        btnIniciarSesion.text = textBotonSpannableString

        val etNombre: EditText = findViewById(R.id.etNombre)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etContrasenia: EditText = findViewById(R.id.etContrasenia)
        val etConfirmarContrasenia: EditText = findViewById(R.id.etConfirmarContrasenia)
        val tvError: TextView = findViewById(R.id.tvError)
        val btnRegistrar: Button = findViewById(R.id.btnRegisrar)

        tvError.visibility = View.INVISIBLE

        btnRegistrar.setOnClickListener {
            if (etNombre.text.isBlank() || etEmail.text.isBlank() || etContrasenia.text.isBlank() || etConfirmarContrasenia.text.isBlank()) {
                tvError.text = "Asegúrese de llenar todos los campos."
                tvError.visibility = View.VISIBLE
            } else if (etContrasenia.text.length < 8){
                tvError.text = "La contraseña debe tener al menos 8 caracteres."
                tvError.visibility = View.VISIBLE
            } else if (etContrasenia.text.toString() != etConfirmarContrasenia.text.toString()) {
                tvError.text = "Las contraseñas no son iguales."
                tvError.visibility = View.VISIBLE
            } else {
                val intent = Intent(this, PrincipalActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, IniciarSesionActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}