package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import mx.edu.itson.clothhangerapp.viewmodels.UsuariosViewModel
import java.security.MessageDigest

class ConfiguracionActivity : MenuNavegable() {

    private lateinit var viewModel: UsuariosViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        setupBottomNavigation()
        setSelectedItem(R.id.nav_perfil)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val etNombre: EditText = findViewById(R.id.etNombre)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etContrasenia: EditText = findViewById(R.id.etContrasenia)
        val etConfirmarContrasenia: EditText = findViewById(R.id.etConfirmarContrasenia)
        val btnCerrarSesion : Button = findViewById(R.id.btnCerrarSesion)

        val btnGuardarCambios: Button = findViewById(R.id.btnGuardarCambios)
        val layoutDialogo: LinearLayout = findViewById(R.id.layoutDialogo)
        val btnSi: Button = findViewById(R.id.btnSi)
        val btnNo: Button = findViewById(R.id.btnNo)

        var emailOriginal: String? = null

        viewModel = ViewModelProvider(this)[UsuariosViewModel::class.java]

        viewModel.usuario.observe(this) { usuario ->
            if (usuario != null) {
                Log.d("ConfiguracionActivity", "Usuario cargado: $usuario")
                etNombre.setText(usuario.nombre)
                etEmail.setText(usuario.email)
                emailOriginal = usuario.email
            }
        }

        viewModel.cargarDatosUsuario()

        btnGuardarCambios.setOnClickListener {
            layoutDialogo.visibility = View.VISIBLE
        }

        btnSi.setOnClickListener {
            layoutDialogo.visibility = View.GONE

            val nuevoNombre = etNombre.text.toString().trim()
            val nuevoEmail = etEmail.text.toString().trim()
            val nuevaContrasenia = etContrasenia.text.toString()
            val nuevaContraseniaConfirmar = etConfirmarContrasenia.text.toString()

            // Validar campos
            if (nuevoNombre.isBlank() || nuevoEmail.isBlank() || nuevaContrasenia.isBlank()) {
                Toast.makeText(this, "Todos los campos deben estar completos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!nuevaContrasenia.equals(nuevaContraseniaConfirmar)) {
                Toast.makeText(this, "Las contraseñas deben coincidir.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nuevaContrasenia.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Generar hash de la nueva contraseña (opcional, si quieres guardarlo)
            val passwordHash = hashPassword(nuevaContrasenia)

            // Actualizar en Firestore solamente
            viewModel.actualizarDatosUsuario(nuevoNombre, nuevoEmail, passwordHash)
            Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
        }

        btnNo.setOnClickListener {
            layoutDialogo.visibility = View.GONE
        }

        btnCerrarSesion.setOnClickListener {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

