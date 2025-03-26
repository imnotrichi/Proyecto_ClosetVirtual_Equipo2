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
import androidx.appcompat.app.AppCompatActivity

class IniciarSesionActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)

        val btnRegistrarme:Button = findViewById(R.id.btnRegistrarme)
        val textBoton = "registrarme"
        val textBotonSpannableString = SpannableString(textBoton)
        textBotonSpannableString.setSpan(UnderlineSpan(), 0, textBotonSpannableString.length, 0)
        btnRegistrarme.text = textBotonSpannableString

        val etEmail:EditText = findViewById(R.id.etEmail)
        val etContrasenia:EditText = findViewById(R.id.etContrasenia)
        val tvError: TextView = findViewById(R.id.tvError)
        val btnEntrar:Button = findViewById(R.id.btnEntrar)

        tvError.visibility = View.INVISIBLE

        btnEntrar.setOnClickListener {
            if (etEmail.text.isBlank() || etContrasenia.text.isBlank()) {
                tvError.text = "Asegúrese de llenar todos los campos."
                tvError.visibility = View.VISIBLE
            } else {
                val intent = Intent(this, Principal::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}