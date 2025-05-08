package mx.edu.itson.clothhangerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
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
        val tvError: TextView = findViewById(R.id.tvError)
        val btnRegistrar: Button = findViewById(R.id.btnRegisrar)

        tvError.visibility = View.INVISIBLE

        btnRegistrar.setOnClickListener {
            if (etNombre.text.isBlank() || etEmail.text.isBlank() || etContrasenia.text.isBlank() || etConfirmarContrasenia.text.isBlank()) {
                tvError.text = "Asegúrese de llenar todos los campos."
                tvError.visibility = View.VISIBLE
            } else if (etContrasenia.text.length < 8) {
                tvError.text = "La contraseña debe tener al menos 8 caracteres."
                tvError.visibility = View.VISIBLE
            } else if (etContrasenia.text.toString() != etConfirmarContrasenia.text.toString()) {
                tvError.text = "Asegúrese de que las contraseñas coincidan."
                tvError.visibility = View.VISIBLE
            } else {
                val nombre = etNombre.text.toString()
                val email = etEmail.text.toString()
                val contrasenia = etContrasenia.text.toString()

                auth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registro exitoso, obtener el usuario actual
                        val user = auth.currentUser
                        user?.let {
                            val uid = user.uid
                            // Crear el objeto Usuario
                            val usuario = Usuario(id = uid, nombre = nombre, email = email)

                            // Usar Firestore en lugar de Realtime Database
                            val db = FirebaseFirestore.getInstance()
                            db.collection("usuarios").document(uid).set(usuario)
                                .addOnSuccessListener {
                                    // Si el guardado es exitoso, ir a la actividad principal
                                    val intent = Intent(this, PrincipalActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    tvError.text = "Error al guardar los datos"
                                    tvError.visibility = View.VISIBLE
                                }
                        }
                    } else {
                        tvError.text = "Hubo un error al realizar el registro. Vuelva a intentarlo."
                        tvError.visibility = View.VISIBLE
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

}