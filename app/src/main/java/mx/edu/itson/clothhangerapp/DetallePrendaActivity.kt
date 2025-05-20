package mx.edu.itson.clothhangerapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import mx.edu.itson.clothhangerapp.dataclases.Prenda

class DetallePrendaActivity : MenuNavegable() {

    private lateinit var ivImagen: ImageView
    private lateinit var tvNombre: TextView
    private lateinit var tvCategoria: TextView
    private lateinit var tvColor: TextView
    private lateinit var tvEstampado: TextView
    private lateinit var tvTags: TextView
    private lateinit var tvTotalUsos: TextView
    private lateinit var tvUsosMes: TextView
    private lateinit var btnEditar: ImageButton
    private lateinit var btnBorrar: ImageButton

    private var prendaActual: Prenda? = null

    // Constante para el request code de edición
    companion object {
        const val REQUEST_CODE_EDIT_PRENDA = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_prenda)

        // Llama a setupBottomNavigation si es parte de tu clase base y relevante
        setupBottomNavigation()

        // Inicialización de vistas
        ivImagen = findViewById(R.id.ivImagen)
        tvNombre = findViewById(R.id.tvNombre)
        tvCategoria = findViewById(R.id.tvCategoria)
        tvColor = findViewById(R.id.tvColor) // Tu TextView para el swatch de color
        tvEstampado = findViewById(R.id.tvEstampado)
        tvTags = findViewById(R.id.tvTags)
        tvTotalUsos = findViewById(R.id.tvTotalUsos)
        tvUsosMes = findViewById(R.id.tvUsosMes)
        btnEditar = findViewById(R.id.btnEditar)
        btnBorrar = findViewById(R.id.btnBorrar)

        // Recibir la prenda del Intent
        val prendaRecibida: Prenda? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("PRENDA_DETALLE", Prenda::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Prenda>("PRENDA_DETALLE")
        }

        prendaActual = prendaRecibida // Guardar la referencia

        // Poblar las vistas con los datos de la prenda
        poblarVistasConPrenda(prendaActual)

        // Configurar listeners si la prenda se cargó correctamente
        prendaActual?.let { prenda ->
            btnEditar.setOnClickListener {
                val intentEditar = Intent(this, RegistrarPrendaActivity::class.java)
                intentEditar.putExtra("PRENDA_A_EDITAR", prenda) // Enviar la prenda actual para editar
                startActivityForResult(intentEditar, REQUEST_CODE_EDIT_PRENDA)
            }

            btnBorrar.setOnClickListener {
                mostrarDialogoConfirmacionBorrado(prenda)
            }
        } ?: run {
            tvNombre.text = getString(R.string.error)
            btnEditar.isEnabled = false
            btnBorrar.isEnabled = false
        }
    }

    private fun poblarVistasConPrenda(prenda: Prenda?) {
        prenda?.let { p ->
            tvNombre.text = p.nombre
            tvCategoria.text = p.categoria
            tvEstampado.text = if (p.estampado) getString(R.string.si) else getString(R.string.no)
            tvTags.text = p.etiquetas.joinToString(", ")
            tvTotalUsos.text = p.usosTotales.toString()
            tvUsosMes.text = p.usosMensuales.toString()

            tvColor.text = "" // Vaciar texto del swatch de color
            // Asegurar que el tvColor tenga un tamaño fijo en el XML (ej. 24dp x 24dp)
            if (p.colorHex.isNotEmpty()) {
                try {
                    val colorString = if (p.colorHex.startsWith("#")) p.colorHex else "#" + p.colorHex
                    tvColor.setBackgroundColor(Color.parseColor(colorString))
                } catch (e: IllegalArgumentException) {
                    Log.e("DetallePrenda", "Error al parsear color en poblarVistas: '${p.colorHex}'", e)
                    tvColor.setBackgroundColor(Color.TRANSPARENT) // O un color de error/default
                }
            } else {
                tvColor.setBackgroundColor(Color.TRANSPARENT) // O un color de default si está vacío
            }

            if (p.imagenUrl.isNotEmpty()) {
                Glide.with(this).load(p.imagenUrl).into(ivImagen)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT_PRENDA && resultCode == Activity.RESULT_OK) {
            // La prenda fue editada exitosamente.
            val prendaEditada = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data?.getParcelableExtra("PRENDA_EDITADA", Prenda::class.java)
            } else {
                @Suppress("DEPRECATION")
                data?.getParcelableExtra<Prenda>("PRENDA_EDITADA")
            }

            if (prendaEditada != null) {
                prendaActual = prendaEditada
                poblarVistasConPrenda(prendaActual) // Actualiza la UI con los nuevos datos
            } else {
                // Si no se devuelve la prenda, pero sabemos que se guardó (RESULT_OK),
                // podríamos necesitar recargar desde la fuente o simplemente confiar en que
                // PrincipalActivity se actualizará. Por ahora, si no hay prenda, no hacemos más.
                // O, simplemente, podrías finalizar esta actividad:
                // finish()
            }
            // Notificar a PrincipalActivity (si está escuchando y esta actividad fue iniciada por ella con startActivityForResult)
            // que algo pudo haber cambiado.
            setResult(Activity.RESULT_OK)
            // Si no es necesario actualizar PrincipalActivity inmediatamente o si el ViewModel lo maneja:
            // podrías no necesitar setResult() aquí o incluso no usar startActivityForResult.
        }
    }

    private fun mostrarDialogoConfirmacionBorrado(prenda: Prenda) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmacion))
            .setMessage(getString(R.string.confirmar_pregunta, prenda.nombre))
            .setPositiveButton(getString(R.string.borrar)) { dialog, which ->
                borrarPrenda(prenda)
            }
            .setNegativeButton(getString(R.string.cancelar), null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun borrarPrenda(prenda: Prenda) {
        val prendaIdParaBorrar = prenda.id
        val userIdDePrenda = prenda.userId // Este es el UserID de la prenda, no necesariamente el del usuario actual logueado
        // pero para la ruta de la prenda debe ser este.
        val nombrePrendaParaLog = prenda.nombre

        Log.d("DetallePrenda", "Iniciando borrado para Prenda ID: $prendaIdParaBorrar, Nombre: $nombrePrendaParaLog, UserID en prenda: $userIdDePrenda")

        if (prendaIdParaBorrar == null) {
            Log.e("DetallePrenda", "ID de prenda es null. No se puede borrar.")
            return
        }
        if (userIdDePrenda == null) {
            Log.e("DetallePrenda", "UserID en la prenda es null. No se puede construir la ruta completa para borrar de la subcolección.")
            Toast.makeText(this, "Error: Falta UserID en la prenda.", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val userPrendaDocumentRef = db.collection("usuarios")
            .document(userIdDePrenda) // Usar el userId de la prenda para la ruta
            .collection("prendas")
            .document(prendaIdParaBorrar)

        val rootPrendaDocumentRef = db.collection("prendas").document(prendaIdParaBorrar)

        // Paso 1: Borrar de la subcolección del usuario
        Log.d("DetallePrenda", "Intentando borrar de: ${userPrendaDocumentRef.path}")
        userPrendaDocumentRef.delete()
            .addOnSuccessListener {
                Log.i("DetallePrenda", "ÉXITO al borrar Prenda '${nombrePrendaParaLog}' de la subcolección del usuario (${userPrendaDocumentRef.path}).")

                // Paso 2: Si el primero fue exitoso, intentar borrar de la colección raíz "prendas"
                Log.d("DetallePrenda", "Intentando borrar de la colección raíz: ${rootPrendaDocumentRef.path}")
                rootPrendaDocumentRef.delete()
                    .addOnSuccessListener {
                        Log.i("DetallePrenda", "ÉXITO al borrar Prenda '${nombrePrendaParaLog}' también de la colección raíz (${rootPrendaDocumentRef.path}).")

                        // SOLO si AMBOS borrados son exitosos (o el principal es exitoso y el segundo es opcional)

                        if (prenda.imagenUrl.isNotEmpty()) {
                            borrarImagenDeStorage(prenda.imagenUrl)
                        }

                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener { e_root ->
                        Log.e("DetallePrenda", "Prenda borrada de subcolección, pero FALLÓ borrado de raíz '${rootPrendaDocumentRef.path}': ${e_root.message}", e_root)
                        // Aquí decides: ¿Es un error total? ¿O el borrado principal fue suficiente?
                        // Por ahora, consideraremos que si el de la subcolección del usuario se borró, es un "éxito parcial" suficiente para el usuario.
                        // Pero idealmente, deberías tener una estrategia para manejar esta inconsistencia.
                        Toast.makeText(this, "Prenda borrada (parcialmente, error en col. raíz): ${e_root.localizedMessage}", Toast.LENGTH_LONG).show() // Mensaje diferente

                        // Aún así podrías borrar la imagen y cerrar, pero sabiendo que hay una inconsistencia.
                        if (prenda.imagenUrl.isNotEmpty()) {
                            borrarImagenDeStorage(prenda.imagenUrl)
                        }
                        setResult(Activity.RESULT_OK) // Indicar que algo cambió, aunque sea parcialmente.
                        finish()
                    }
            }
            .addOnFailureListener { e_user_subcollection ->
                Log.e("DetallePrenda", "FALLÓ borrado de Prenda '${nombrePrendaParaLog}' de la subcolección del usuario (${userPrendaDocumentRef.path}): ${e_user_subcollection.message}", e_user_subcollection)
                // Si el borrado principal falla, no continuamos al borrado de la raíz ni de la imagen.
            }
    }

    private fun borrarImagenDeStorage(imageUrl: String) {
        // Verificar que la URL sea una URL de Firebase Storage antes de intentar borrar
        if (!imageUrl.startsWith("gs://") && !imageUrl.startsWith("https://firebasestorage.googleapis.com/")) {
            Log.w("DetallePrenda", "URL de imagen no parece ser de Firebase Storage, no se intentará borrar: $imageUrl")
            return
        }

        val storage = FirebaseStorage.getInstance()
        try {
            val storageRef = storage.getReferenceFromUrl(imageUrl)
            storageRef.delete()
                .addOnSuccessListener {
                    Log.d("DetallePrenda", "Imagen $imageUrl borrada de Storage.")
                }
                .addOnFailureListener { e ->
                    // El fallo puede ser porque el archivo no existe, lo cual está bien en un borrado.
                    // O podría ser un problema de permisos.
                    Log.w("DetallePrenda", "Fallo o no se pudo borrar imagen de Storage (podría no existir ya): $imageUrl", e)
                }
        } catch (e: IllegalArgumentException) {
            // Esto puede pasar si la URL no es una URL válida de Firebase Storage formateada como espera getReferenceFromUrl
            Log.e("DetallePrenda", "URL de imagen inválida para Firebase Storage: $imageUrl", e)
        }
    }

}