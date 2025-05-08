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
            val contraseniaActual = etContrasenia.text.toString()
            val nuevaContrasenia = etConfirmarContrasenia.text.toString()

            val cambiarEmail = nuevoEmail != emailOriginal
            val cambiarContrasenia = nuevaContrasenia.isNotEmpty()

            // Validación de contraseña nueva
            if (cambiarContrasenia && nuevaContrasenia.length < 6) {
                Toast.makeText(this, "La nueva contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cambiarEmail || cambiarContrasenia) {
                if (contraseniaActual.isBlank()) {
                    Toast.makeText(this, "Debes ingresar tu contraseña actual para confirmar", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val user = auth.currentUser
                val credential = EmailAuthProvider.getCredential(emailOriginal!!, contraseniaActual) // Use non-null emailOriginal

                if (user != null) { // Check if user is not null
                    user.reauthenticate(credential)
                        .addOnCompleteListener { reauthResult ->
                            if (reauthResult.isSuccessful) {
                                viewModel.actualizarDatosUsuario(nuevoNombre, nuevoEmail) // Update Firestore data

                                if (cambiarEmail) {
                                    user.updateEmail(nuevoEmail)
                                        .addOnCompleteListener { emailUpdateTask ->
                                            if (emailUpdateTask.isSuccessful) {
                                                Log.d("ConfiguracionActivity", "Email updated successfully")
                                            } else {
                                                Log.e("ConfiguracionActivity", "Failed to update email: ${emailUpdateTask.exception?.message}")
                                                Toast.makeText(this, "Failed to update email: ${emailUpdateTask.exception?.message}", Toast.LENGTH_LONG).show()
                                                return@addOnCompleteListener // IMPORTANT: Return on failure
                                            }
                                        }
                                }

                                if (cambiarContrasenia) {
                                    user.updatePassword(nuevaContrasenia)
                                        .addOnCompleteListener { passwordUpdateTask ->
                                            if (passwordUpdateTask.isSuccessful) {
                                                Log.d("ConfiguracionActivity", "Password updated successfully")
                                            } else {
                                                Log.e("ConfiguracionActivity", "Failed to update password: ${passwordUpdateTask.exception?.message}")
                                                Toast.makeText(this, "Failed to update password: ${passwordUpdateTask.exception?.message}", Toast.LENGTH_LONG).show()
                                                return@addOnCompleteListener // IMPORTANT: Return on failure
                                            }
                                        }
                                }
                                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()

                            } else {
                                Log.e("ConfiguracionActivity", "Reauthentication failed: ${reauthResult.exception?.message}")
                                if (reauthResult.exception is FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show()
                                } else if (reauthResult.exception is FirebaseAuthUserCollisionException){
                                    Toast.makeText(this, "El correo electrónico ya está en uso.", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(this, "Reautenticación fallida: ${reauthResult.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                }
                else{
                    Toast.makeText(this, "No hay usuario autenticado.", Toast.LENGTH_LONG).show()
                }
            } else {
                viewModel.actualizarDatosUsuario(nuevoNombre, nuevoEmail)
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            }
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
}

