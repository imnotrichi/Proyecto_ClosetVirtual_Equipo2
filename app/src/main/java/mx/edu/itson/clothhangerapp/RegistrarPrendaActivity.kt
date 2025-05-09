package mx.edu.itson.clothhangerapp

import android.app.Activity // --- NUEVO --- (Para setResult)
import android.content.Intent // --- NUEVO --- (Para setResult)
import android.graphics.Color // --- NUEVO --- (Asegúrate de importar esta)
import android.net.Uri
import android.os.Build // --- NUEVO --- (Para la comprobación de versión de SDK)
import android.os.Bundle
import android.util.Log // --- NUEVO --- (Para logging)
import android.widget.ImageButton
import android.widget.Toast
// import androidx.activity.enableEdgeToEdge // Comentado si no lo usas activamente
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity // O tu clase base MenuNavegable
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
// import androidx.core.view.ViewCompat // Comentado si no lo usas activamente
// import androidx.core.view.WindowInsetsCompat // Comentado si no lo usas activamente
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide // --- NUEVO --- (Para cargar la imagen en edición)
import com.mrudultora.colorpicker.ColorPickerPopUp
import mx.edu.itson.clothhangerapp.databinding.ActivityRegistrarPrendaBinding
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel

class RegistrarPrendaActivity : MenuNavegable() {

    private lateinit var binding: ActivityRegistrarPrendaBinding
    private lateinit var viewModel: PrendasViewModel
    private lateinit var seleccionarImagenLauncher: ActivityResultLauncher<String>
    private var imagenUriParaSubir: Uri? = null

    private var colorSeleccionadoHex: String = "#FFFFFFFF"
    private var categoriaSeleccionada: String = ""
    private var tieneEstampado: Boolean = false
    private var etiquetasSeleccionadas = mutableListOf<String>()

    private lateinit var mapIconosCategoriaOriginal: Map<ImageButton, Int>
    private lateinit var mapIconosCategoriaSeleccionado: Map<ImageButton, Int>

    private var prendaAEditar: Prenda? = null
    private var esModoEdicion: Boolean = false
    private var imagenUrlOriginal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarPrendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PrendasViewModel::class.java]

        inicializarMapasDeIconos()
        configurarLauncherSeleccionImagen()
        configurarListenersUI()
        configurarEstadoInicialUI()
        observarViewModel()

        setupBottomNavigation()

        if (intent.hasExtra("PRENDA_A_EDITAR")) {
            prendaAEditar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("PRENDA_A_EDITAR", Prenda::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Prenda>("PRENDA_A_EDITAR")
            }

            if (prendaAEditar != null) {
                esModoEdicion = true
                imagenUrlOriginal = prendaAEditar?.imagenUrl // Guardar URL original
                cargarDatosParaEdicion(prendaAEditar!!)
                binding.btnRegistrarPrenda.text = getString(R.string.actualizar) // Ej: "Actualizar Prenda" (Define en strings.xml)
                // Opcional: Cambiar título de la Activity/Toolbar
                // supportActionBar?.title = getString(R.string.editar_prenda_titulo) // Define en strings.xml
            } else {
                Log.e("RegistrarPrenda", "MODO EDICIÓN: Prenda extra es null.")
                // Proceder como si fuera nueva prenda si la carga falla
                esModoEdicion = false
                configurarEstadoInicialUI()
            }
        } else {
            // Modo creación de nueva prenda
            esModoEdicion = false
            configurarEstadoInicialUI()
        }
        configurarListenersUI()
    }


    private fun inicializarMapasDeIconos() {
        mapIconosCategoriaOriginal = mapOf(
            binding.ibTop to R.drawable.icono_tops_negro,
            binding.ibBottom to R.drawable.icono_bottoms_negro,
            binding.ibZapatos to R.drawable.icono_zapatos_negro,
            binding.ibBodysuit to R.drawable.icono_bodysuits_negro,
            binding.ibAccesorio to R.drawable.icono_accesorios_negro
        )
        mapIconosCategoriaSeleccionado = mapOf(
            binding.ibTop to R.drawable.icono_tops_blanco,
            binding.ibBottom to R.drawable.icono_bottoms_blanco,
            binding.ibZapatos to R.drawable.icono_zapatos_blanco,
            binding.ibBodysuit to R.drawable.icono_bodysuit_blanco,
            binding.ibAccesorio to R.drawable.icono_accesorios_blanco
        )
    }

    private fun configurarLauncherSeleccionImagen() {
        seleccionarImagenLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imagenUriParaSubir = it
                binding.ibAgregarImagen.setImageURI(it)
            }
        }
    }

    private fun cargarDatosParaEdicion(prenda: Prenda) {
        Log.d("RegistrarPrenda", "Iniciando cargarDatosParaEdicion. ID de prenda recibida: ${prenda.id}, Nombre: ${prenda.nombre}")
        Log.d("RegistrarPrenda", "Cargando datos para editar: ${prenda.nombre}, ImagenURL: ${prenda.imagenUrl}")

        binding.etNombrePrenda.setText(prenda.nombre)

        // Imagen: Mostrar la imagen existente
        imagenUriParaSubir = null // Importante: resetear, solo se llena si el usuario elige OTRA imagen
        if (prenda.imagenUrl.isNotEmpty()) {
            Glide.with(this)
                .load(prenda.imagenUrl)
                .into(binding.ibAgregarImagen)
        }

        // Categoría
        categoriaSeleccionada = prenda.categoria
        val nombresCategorias = listOf("Top", "Bottom", "Zapatos", "Bodysuit", "Accesorio")
        val imageButtonsCategorias = listOf(binding.ibTop, binding.ibBottom, binding.ibZapatos, binding.ibBodysuit, binding.ibAccesorio)
        val indiceCategoria = nombresCategorias.indexOf(prenda.categoria)
        if (indiceCategoria != -1) {
            actualizarAparienciaCategorias(imageButtonsCategorias[indiceCategoria], imageButtonsCategorias)
        } else {
            actualizarAparienciaCategorias(null, imageButtonsCategorias) // Deseleccionar todas
        }

        // Estampado
        tieneEstampado = prenda.estampado
        actualizarAparienciaEstampado(esSiSeleccionado = prenda.estampado)

        // Color
        colorSeleccionadoHex = prenda.colorHex
        if (prenda.colorHex.isNotEmpty()) {
            try {
                val colorString = if (prenda.colorHex.startsWith("#")) prenda.colorHex else "#" + prenda.colorHex
                val colorInt = Color.parseColor(colorString)
                binding.btnColor.setBackgroundColor(colorInt)
            } catch (e: IllegalArgumentException) {
                Log.e("RegistrarPrenda", "Error al parsear color para edición: '${prenda.colorHex}'", e)
                binding.btnColor.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray)) // Color de error
            }
        } else {
            binding.btnColor.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white)) // Color por defecto
        }

        // Etiquetas
        etiquetasSeleccionadas.clear()
        etiquetasSeleccionadas.addAll(prenda.etiquetas)
        val toggleButtonsEtiquetasMap = mapOf(
            binding.tbCasual to "Casual", binding.tbFormal to "Formal",
            binding.tbDeportivo to "Deportivo", binding.tbBasico to "Basico",
            binding.tbFiesta to "Fiesta"
        )
        toggleButtonsEtiquetasMap.forEach { (toggleButton, etiquetaNombre) ->
            val isSelected = etiquetasSeleccionadas.contains(etiquetaNombre)
            toggleButton.isChecked = isSelected // Establecer estado antes de que el listener se active
            if (isSelected) {
                toggleButton.setBackgroundResource(R.drawable.tag_view_on)
                toggleButton.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                toggleButton.setBackgroundResource(R.drawable.tag_view_off)
                toggleButton.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }
    }

    private fun configurarEstadoInicialUI() {
        Log.d("RegistrarPrenda", "Configurando UI para NUEVA prenda.")
        binding.etNombrePrenda.setText("")
        binding.ibAgregarImagen.setImageResource(R.drawable.agregar) // Ícono para añadir foto

        val imageButtonsCategorias = listOf(binding.ibTop, binding.ibBottom, binding.ibZapatos, binding.ibBodysuit, binding.ibAccesorio)
        actualizarAparienciaCategorias(null, imageButtonsCategorias) // Ninguna categoría seleccionada
        categoriaSeleccionada = ""

        actualizarAparienciaEstampado(esSiSeleccionado = false) // O tu valor por defecto para estampado
        tieneEstampado = false

        binding.btnColor.setBackgroundResource(R.drawable.rainbow_bar) // <--- ESTABLECER EL DRAWABLE ARCOÍRIS
        // Inicializar colorSeleccionadoHex a un valor por defecto (ej. blanco) o incluso a vacío/nulo
        // si quieres indicar que ningún color sólido ha sido explícitamente seleccionado aún.
        // Si el picker necesita un color por defecto al abrirse la primera vez, blanco está bien.
        val defaultColorIntForPicker = ContextCompat.getColor(this, android.R.color.white)
        colorSeleccionadoHex = String.format("#%08X", defaultColorIntForPicker) // Blanco opaco por defecto para el picker

        etiquetasSeleccionadas.clear()
        val toggleButtonsEtiquetasMap = mapOf(
            binding.tbCasual to "Casual", binding.tbFormal to "Formal",
            binding.tbDeportivo to "Deportivo", binding.tbBasico to "Basico",
            binding.tbFiesta to "Fiesta"
        )
        toggleButtonsEtiquetasMap.forEach { (toggleButton, _) ->
            toggleButton.isChecked = false
            toggleButton.setBackgroundResource(R.drawable.tag_view_off)
            toggleButton.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        imagenUriParaSubir = null
        imagenUrlOriginal = null // No hay imagen original para nueva prenda
        prendaAEditar = null // No hay prenda a editar
        esModoEdicion = false // Confirmar modo creación
        binding.btnRegistrarPrenda.text = getString(R.string.registrar_prenda) // Ej: "Registrar Prenda" (Define en strings.xml)
    }

    private fun configurarListenersUI() {
        binding.ibAgregarImagen.setOnClickListener {
            seleccionarImagenLauncher.launch("image/*")
        }

        binding.btnColor.setOnClickListener {
            val colorPickerPopUp = ColorPickerPopUp(this)
            colorPickerPopUp.setShowAlpha(true)
                .setDefaultColor(ContextCompat.getColor(this, android.R.color.white))
                .setDialogTitle("Selecciona un Color")
                .setOnPickColorListener(object : ColorPickerPopUp.OnPickColorListener {
                    override fun onColorPicked(color: Int) {
                        binding.btnColor.setBackgroundColor(color)
                        colorSeleccionadoHex = String.format("#%08X", color)
                    }
                    override fun onCancel() {
                        colorPickerPopUp.dismissDialog()
                    }
                })
                .show()
        }

        val imageButtonsCategorias = listOf(binding.ibTop, binding.ibBottom, binding.ibZapatos, binding.ibBodysuit, binding.ibAccesorio)
        val nombresCategorias = listOf("Top", "Bottom", "Zapatos", "Bodysuit", "Accesorio")

        imageButtonsCategorias.forEachIndexed { index, imageButton ->
            imageButton.setOnClickListener {
                categoriaSeleccionada = nombresCategorias[index]
                actualizarAparienciaCategorias(imageButton, imageButtonsCategorias)
            }
        }

        binding.tbSi.setOnClickListener {
            tieneEstampado = true
            actualizarAparienciaEstampado(esSiSeleccionado = true)
        }
        binding.tbNo.setOnClickListener {
            tieneEstampado = false
            actualizarAparienciaEstampado(esSiSeleccionado = false)
        }

        val toggleButtonsEtiquetas = mapOf(
            binding.tbCasual to "Casual", binding.tbFormal to "Formal",
            binding.tbDeportivo to "Deportivo", binding.tbBasico to "Basico",
            binding.tbFiesta to "Fiesta"
        )
        toggleButtonsEtiquetas.forEach { (toggleButton, etiquetaNombre) ->
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!etiquetasSeleccionadas.contains(etiquetaNombre)) etiquetasSeleccionadas.add(etiquetaNombre)
                    toggleButton.setBackgroundResource(R.drawable.tag_view_on)
                    toggleButton.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else {
                    etiquetasSeleccionadas.remove(etiquetaNombre)
                    toggleButton.setBackgroundResource(R.drawable.tag_view_off)
                    toggleButton.setTextColor(ContextCompat.getColor(this, R.color.black))
                }
            }
        }

        binding.btnRegistrarPrenda.setOnClickListener {
            val nombrePrenda = binding.etNombrePrenda.text.toString().trim()

            if (nombrePrenda.isEmpty()) {
                Toast.makeText(this, "El nombre de la prenda es obligatorio.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (categoriaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Debes seleccionar una categoría.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Si NO es modo edición Y no se ha seleccionado imagen nueva, es un error.
            // Si ES modo edición, la imagen puede ser la original (imagenUriParaSubir es null pero imagenUrlOriginal existe).
            if (!esModoEdicion && imagenUriParaSubir == null) {
                Toast.makeText(this, "Debes seleccionar una imagen para la nueva prenda.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Si es modo edición y no hay imagen original NI se seleccionó una nueva (esto no debería pasar si la imagen es obligatoria)
            if (esModoEdicion && imagenUrlOriginal.isNullOrEmpty() && imagenUriParaSubir == null) {
                Toast.makeText(this, "La prenda debe tener una imagen.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prendaParaGuardar: Prenda // Declara el tipo

            if (esModoEdicion && prendaAEditar != null) {
                // MODO EDICIÓN: Copia la prenda original y actualiza los campos modificados
                prendaParaGuardar = prendaAEditar!!.copy( // Usamos prendaAEditar!! porque ya comprobamos que no es null
                    nombre = nombrePrenda,
                    categoria = categoriaSeleccionada,
                    estampado = tieneEstampado,
                    colorHex = colorSeleccionadoHex,
                    etiquetas = ArrayList(etiquetasSeleccionadas)
                    // imagenUrl se manejará en el ViewModel basado en si imagenUriParaSubir es nueva o no.
                    // El ViewModel usará imagenUrlOriginal si imagenUriParaSubir es null.
                    // Los campos como id, userId, usosMensuales, usosTotales se preservan de prendaAEditar.
                )
                Log.d("RegistrarPrenda", "Modo Edición. ID de prendaParaGuardar (debería ser de prendaAEditar): ${prendaParaGuardar.id}, Nombre: ${prendaParaGuardar.nombre}")
            } else {
                // MODO CREACIÓN: Crear una nueva instancia
                prendaParaGuardar = Prenda(
                    // id se generará en Firestore (manejado por el ViewModel)
                    userId = viewModel.obtenerUsuarioActualId(), // Asumo que este método existe en tu ViewModel
                    nombre = nombrePrenda,
                    categoria = categoriaSeleccionada,
                    estampado = tieneEstampado,
                    colorHex = colorSeleccionadoHex,
                    etiquetas = ArrayList(etiquetasSeleccionadas),
                    imagenUrl = "", // El ViewModel la llenará si se sube una imagen
                    usosMensuales = 0, // Valor inicial para nueva prenda
                    usosTotales = 0    // Valor inicial para nueva prenda
                )
            }

            if (esModoEdicion) {
                Log.d("RegistrarPrenda", "MODO EDICIÓN: Llamar a viewModel.actualizarPrenda(...)")
                // viewModel.actualizarPrenda(prendaParaGuardar, imagenUriParaSubir, imagenUrlOriginal)
                // Ejemplo conceptual de cómo podría ser la llamada al ViewModel (necesitarás crear este método):
                viewModel.actualizarPrenda(prendaParaGuardar, imagenUriParaSubir, imagenUrlOriginal)

            } else {
                Log.d("RegistrarPrenda", "MODO CREACIÓN: Llamar a viewModel.guardarNuevaPrenda(...)")
                // viewModel.guardarNuevaPrenda(prendaParaGuardar, imagenUriParaSubir)
                // Ejemplo conceptual:
                viewModel.registrarNuevaPrenda(prendaParaGuardar, imagenUriParaSubir)
            }

        }
    }

    private fun actualizarAparienciaCategorias(seleccionado: ImageButton?, todosLosBotones: List<ImageButton>) {
        todosLosBotones.forEach { boton ->
            if (boton == seleccionado) {
                boton.setBackgroundResource(R.drawable.tag_view_on)
                mapIconosCategoriaSeleccionado[boton]?.let { boton.setImageResource(it) }
            } else {
                boton.setBackgroundResource(R.drawable.tag_view_off)
                mapIconosCategoriaOriginal[boton]?.let { boton.setImageResource(it) }
            }
        }
    }

    private fun actualizarAparienciaEstampado(esSiSeleccionado: Boolean) {
        if (esSiSeleccionado) {
            binding.tbSi.isChecked = true
            binding.tbNo.isChecked = false
            binding.tbSi.setBackgroundResource(R.drawable.tag_view_on)
            binding.tbSi.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.tbNo.setBackgroundResource(R.drawable.tag_view_off)
            binding.tbNo.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            binding.tbNo.isChecked = true
            binding.tbSi.isChecked = false
            binding.tbNo.setBackgroundResource(R.drawable.tag_view_on)
            binding.tbNo.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.tbSi.setBackgroundResource(R.drawable.tag_view_off)
            binding.tbSi.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun observarViewModel() {
        viewModel.operacionExitosa.observe(this) { exitoso ->
            if (exitoso == true) { // Comprobar explícitamente por true si el LiveData puede ser nullable Boolean
                Toast.makeText(
                    this,
                    if (esModoEdicion) getString(R.string.actualizacion_exito) // Necesitas este string
                    else getString(R.string.registro_exito), // Necesitas este string
                    Toast.LENGTH_SHORT
                ).show()

                val resultIntent = Intent()
                // Obtener la prenda directamente del LiveData que debería tener la versión más actualizada
                val prendaResultadoFinal = viewModel.ultimaPrendaModificada.value // Este LiveData lo definimos en el ViewModel

                if (prendaResultadoFinal != null) {
                    // Tanto para edición como para creación, si queremos devolver la prenda completa.
                    // El ViewModel ya se encargó de que 'prendaResultadoFinal' tenga el ID y la imagenUrl correctos.
                    resultIntent.putExtra("PRENDA_EDITADA", prendaResultadoFinal) // DetallePrendaActivity espera esta clave
                    Log.d(
                        "RegistrarPrenda",
                        "Devolviendo ${if (esModoEdicion) "PRENDA_EDITADA" else "NUEVA_PRENDA_OK"}: " +
                                "Nombre: ${prendaResultadoFinal.nombre}, ID: ${prendaResultadoFinal.id}, URL: ${prendaResultadoFinal.imagenUrl}"
                    )
                }
                // Si no es modo edición, PrincipalActivity podría simplemente recargar su lista.
                // Si es modo edición, DetallePrendaActivity espera PRENDA_EDITADA.
                setResult(Activity.RESULT_OK, resultIntent)
                finish()

                // Limpiar el estado en el ViewModel para la próxima vez
                viewModel.limpiarEstadoRegistro() // Asumiendo que tienes este método en el ViewModel
                // para resetear operacionExitosa, ultimaPrendaModificada, etc.
            }
            // No es necesario el 'else' aquí si solo reaccionas al éxito para cerrar la actividad.
            // El manejo de errores (mostrar Toast de error) se hace en el observador de viewModel.mensajeError.
        }

        viewModel.errorMensaje.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                viewModel.limpiarMensajeError() // Limpiar para que no se muestre de nuevo al rotar, etc.
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Aquí puedes mostrar/ocultar un ProgressBar si lo tienes en tu layout
            // binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
            // binding.btnRegistrarPrenda.isEnabled = !(isLoading == true) // Deshabilitar botón mientras carga
        }

        // Si necesitas la URL de la imagen específicamente (aunque ya debería estar en ultimaPrendaModificada):
        // viewModel.nuevaUrlImagen.observe(this) { url ->
        //     if (url != null && esModoEdicion) {
        //         Log.d("RegistrarPrenda", "Observada nueva URL de imagen para prenda editada: $url (pero ya debería estar en ultimaPrendaModificada)")
        //     }
        // }
    }
}