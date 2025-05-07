package mx.edu.itson.clothhangerapp

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.mrudultora.colorpicker.ColorPickerPopUp
import mx.edu.itson.clothhangerapp.databinding.ActivityRegistrarPrendaBinding
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel

class RegistrarPrendaActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrarPrendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[PrendasViewModel::class.java]

        inicializarMapasDeIconos()
        configurarLauncherSeleccionImagen()
        configurarListenersUI()
        configurarEstadoInicialUI()
        observarViewModel()
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
            binding.tbDeportivo to "Deportivo", binding.tbBasico to "Básico",
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
            if (imagenUriParaSubir == null) {
                Toast.makeText(this, "Debes seleccionar una imagen para la prenda.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prendaParaGuardar = Prenda(
                nombre = nombrePrenda,
                categoria = categoriaSeleccionada,
                estampado = tieneEstampado,
                colorHex = colorSeleccionadoHex,
                etiquetas = ArrayList(etiquetasSeleccionadas)
            )
            viewModel.registrarNuevaPrenda(prendaParaGuardar, imagenUriParaSubir)
        }
    }

    private fun configurarEstadoInicialUI() {
        val imageButtonsCategorias = listOf(binding.ibTop, binding.ibBottom, binding.ibZapatos, binding.ibBodysuit, binding.ibAccesorio)
        actualizarAparienciaCategorias(null, imageButtonsCategorias)

        tieneEstampado = false
        actualizarAparienciaEstampado(esSiSeleccionado = false)

        listOf(binding.tbCasual, binding.tbFormal, binding.tbDeportivo, binding.tbBasico, binding.tbFiesta).forEach {
            it.isChecked = false
            it.setBackgroundResource(R.drawable.tag_view_off)
            it.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        etiquetasSeleccionadas.clear()
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
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnRegistrarPrenda.isEnabled = !isLoading // Deshabilitar botón mientras carga
        }

        viewModel.errorMensaje.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.limpiarMensajeError()
            }
        }

        viewModel.registroExitoso.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "¡Prenda registrada con éxito!", Toast.LENGTH_SHORT).show()
                viewModel.limpiarEstadoRegistro()
                finish()
            }
        }
    }
}