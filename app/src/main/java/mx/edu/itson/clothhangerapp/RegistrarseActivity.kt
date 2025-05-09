package mx.edu.itson.clothhangerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import mx.edu.itson.clothhangerapp.viewmodels.UsuariosViewModel

class RegistrarseActivity : AppCompatActivity() {

    private lateinit var viewModel: UsuariosViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val btnIniciarSesion: Button = findViewById(R.id.btnIniciarSesion)
        val textBoton = "iniciar sesión"
        val textBotonSpannableString = SpannableString(textBoton)
        textBotonSpannableString.setSpan(UnderlineSpan(), 0, textBotonSpannableString.length, 0)
        btnIniciarSesion.text = textBotonSpannableString

        viewModel = ViewModelProvider(this)[UsuariosViewModel::class.java]

        viewModel.errorMensaje.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.usuario.observe(this, Observer { usuario ->
            if (usuario != null) {
                // Si el registro es exitoso, redirigir a la pantalla principal
                val intent = Intent(this, PrincipalActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()  // Esto cierra la actividad actual para que no pueda regresar al registro
            }
        })

        val etNombre: EditText = findViewById(R.id.etNombre)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etContrasenia: EditText = findViewById(R.id.etContrasenia)
        val etConfirmarContrasenia: EditText = findViewById(R.id.etConfirmarContrasenia)
        val btnRegistrar: Button = findViewById(R.id.btnRegisrar)

        btnRegistrar.setOnClickListener {
            if (etNombre.text.isBlank() || etEmail.text.isBlank() || etContrasenia.text.isBlank() || etConfirmarContrasenia.text.isBlank()) {
                Toast.makeText(this, "Asegúrese de llenar todos los campos.", Toast.LENGTH_SHORT).show()
            } else if (etContrasenia.text.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show()
            } else if (etContrasenia.text.toString() != etConfirmarContrasenia.text.toString()) {
                Toast.makeText(this, "Asegúrese de que ambas contraseñas coincidan.", Toast.LENGTH_SHORT).show()
            } else {
                val nombre = etNombre.text.toString()
                val email = etEmail.text.toString()
                val contrasenia = etContrasenia.text.toString()

                viewModel.registrarUsuario(nombre, email, contrasenia)

            }
        }

        btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, IniciarSesionActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


}