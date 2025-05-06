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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class IniciarSesionActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

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
                auth.signInWithEmailAndPassword(etEmail.text.toString(), etContrasenia.text.toString()).addOnCompleteListener(this) {
                        task ->
                    if  (task.isSuccessful) {
                        val intent = Intent(this, PrincipalActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        tvError.text = "Alguno o ambos datos ingresados son erróneos."
                        tvError.visibility = View.VISIBLE
                    }
                }
            }
        }

        btnRegistrarme.setOnClickListener {
            val intent = Intent(this, RegistrarseActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}