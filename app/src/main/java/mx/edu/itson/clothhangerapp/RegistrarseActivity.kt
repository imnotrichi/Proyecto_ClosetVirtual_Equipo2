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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.itson.clothhangerapp.dataclases.Usuario
import java.security.MessageDigest

class RegistrarseActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

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

                auth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let {
                            val uid = user.uid
                            val hashedPassword = hashPassword(contrasenia)

                            val usuario = Usuario()
                            usuario.id = uid
                            usuario.nombre = nombre
                            usuario.email = email
                            usuario.contraseniaHash = hashedPassword

                            val db = FirebaseFirestore.getInstance()
                            db.collection("usuarios").document(uid).set(usuario)
                                .addOnSuccessListener {
                                    val intent = Intent(this, PrincipalActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Hubo un error al realizar el registro. Vuelva a intentarlo.", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, IniciarSesionActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }


}