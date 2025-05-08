package mx.edu.itson.clothhangerapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.databinding.ActivityRegistroDiarioBinding
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import mx.edu.itson.clothhangerapp.viewmodels.OutfitsViewModel
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel


class RegistroDiarioActivity : MenuNavegable() {

    private lateinit var binding: ActivityRegistroDiarioBinding
    private lateinit var prendasViewModel: PrendasViewModel
    private lateinit var outfitsViewModel: OutfitsViewModel
    private lateinit var seleccionarPrendaLauncher: ActivityResultLauncher<Intent>
    private var prendaTopSeleccionada: Prenda? = null
    private var prendaBottomSeleccionada: Prenda? = null
    private var prendaBodysuitSeleccionada: Prenda? = null
    private var prendaZapatosSeleccionada: Prenda? = null
    private var prendaAccesorio1Seleccionada: Prenda? = null
    private var prendaAccesorio2Seleccionada: Prenda? = null
    private var prendaAccesorio3Seleccionada: Prenda? = null
    private var slotActualizando: String? = null
    private lateinit var mapIconosCategoriaOriginal: Map<ImageButton, Int>
    private lateinit var mapIconosCategoriaSeleccionado: Map<ImageButton, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroDiarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prendasViewModel = ViewModelProvider(this)[PrendasViewModel::class.java]
        outfitsViewModel = ViewModelProvider(this)[OutfitsViewModel::class.java]

        registrarActivityLauncher()

        inicializarMapasDeIconos()
        configurarListenersBotonesSeleccion()

        setupBottomNavigation()
        setSelectedItem(R.id.nav_hoy)

        observarPrendasViewModel()
        observarOutfitsViewModel()

        // Listener del botón Confirmar
        binding.btnConfirmar.setOnClickListener {
            if (validarOutfitMinimo()) {
                guardarOutfit()
            } else {
                Toast.makeText(this, "Completa las prendas mínimas requeridas.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inicializarMapasDeIconos() {
        mapIconosCategoriaOriginal = mapOf(
            binding.btnSeleccionarTop to R.drawable.icono_tops_blanco,
            binding.btnSeleccionarBottom to R.drawable.icono_bottoms_blanco,
            binding.btnSeleccionarBodysuit to R.drawable.icono_bodysuit_blanco,
            binding.btnSeleccionarZapatos to R.drawable.icono_zapatos_blanco,
            binding.btnSeleccionarAccesorios1 to R.drawable.icono_accesorios_blanco,
            binding.btnSeleccionarAccesorios2 to R.drawable.icono_accesorios_blanco,
            binding.btnSeleccionarAccesorios3 to R.drawable.icono_accesorios_blanco
        )
    }

    private fun registrarActivityLauncher() {
        seleccionarPrendaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("RegistroDiario", "Resultado recibido. Code: ${result.resultCode}")
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val prendaId = data?.getStringExtra("PRENDA_SELECCIONADA_ID")
                val slotOrigenDevuelto = data?.getStringExtra("SLOT_ORIGEN_DEVUELTO")

                Log.d("RegistroDiario", "Resultado: slot=$slotOrigenDevuelto, id=$prendaId")

                if (prendaId != null && slotOrigenDevuelto != null) {
                    slotActualizando = slotOrigenDevuelto
                    prendasViewModel.obtenerDetallePrenda(prendaId)
                } else {
                    Log.w("RegistroDiario", "Faltaron datos ID o Slot en el resultado de la selección.")
                    Toast.makeText(this, "No se pudo obtener la información de la prenda.", Toast.LENGTH_SHORT).show()
                    slotActualizando = null
                }
            } else {
                Log.d("RegistroDiario", "Selección cancelada o fallida.")
                slotActualizando = null
            }
        }
    }

    // Función para configurar los listeners
    private fun configurarListenersBotonesSeleccion() {
        binding.btnSeleccionarTop.setOnClickListener { iniciarSeleccionPrenda("Top", "TOP") }
        binding.btnSeleccionarBottom.setOnClickListener { iniciarSeleccionPrenda("Bottom", "BOTTOM") }
        binding.btnSeleccionarBodysuit.setOnClickListener { iniciarSeleccionPrenda("Bodysuit", "BODYSUIT") }
        binding.btnSeleccionarZapatos.setOnClickListener { iniciarSeleccionPrenda("Zapatos", "ZAPATOS") }
        binding.btnSeleccionarAccesorios1.setOnClickListener { iniciarSeleccionPrenda("Accesorio", "ACC1") }
        binding.btnSeleccionarAccesorios2.setOnClickListener { iniciarSeleccionPrenda("Accesorio", "ACC2") }
        binding.btnSeleccionarAccesorios3.setOnClickListener { iniciarSeleccionPrenda("Accesorio", "ACC3") }
    }

    private fun iniciarSeleccionPrenda(categoria: String, slotOrigen: String) {
        val intent = Intent(this, PrendaEspecificaActivity::class.java)
        intent.putExtra("CATEGORIA", categoria)
        intent.putExtra("SLOT_ORIGEN", slotOrigen)
        seleccionarPrendaLauncher.launch(intent)
    }

    private fun observarPrendasViewModel() {
        prendasViewModel.prendaSeleccionadaDetalle.observe(this) { prendaRecibida ->
            if (prendaRecibida != null && slotActualizando != null) {
                Log.d("RegistroDiario", "Detalle recibido para slot $slotActualizando: ${prendaRecibida.nombre}")
                val imageViewTarget: ImageButton? = when (slotActualizando) {
                    "TOP" -> { prendaTopSeleccionada = prendaRecibida; binding.btnSeleccionarTop }
                    "BOTTOM" -> { prendaBottomSeleccionada = prendaRecibida; binding.btnSeleccionarBottom }
                    "BODYSUIT" -> { prendaBodysuitSeleccionada = prendaRecibida; binding.btnSeleccionarBodysuit }
                    "ZAPATOS" -> { prendaZapatosSeleccionada = prendaRecibida; binding.btnSeleccionarZapatos }
                    "ACC1" -> { prendaAccesorio1Seleccionada = prendaRecibida; binding.btnSeleccionarAccesorios1 }
                    "ACC2" -> { prendaAccesorio2Seleccionada = prendaRecibida; binding.btnSeleccionarAccesorios2 }
                    "ACC3" -> { prendaAccesorio3Seleccionada = prendaRecibida; binding.btnSeleccionarAccesorios3 }
                    else -> null
                }

                imageViewTarget?.let { target ->
                    val iconoOriginal = target.drawable
                    Glide.with(this)
                        .load(prendaRecibida.imagenUrl)
                        .placeholder(iconoOriginal)
                        .into(target)
                }

                slotActualizando = null

            } else if (slotActualizando != null) {
                slotActualizando = null
            }
        }

        prendasViewModel.errorDetalle.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error detalle: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observarOutfitsViewModel() {
        outfitsViewModel.isLoading.observe(this) { isLoading ->
            binding.btnConfirmar.isEnabled = !isLoading
        }

        outfitsViewModel.errorMensaje.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error guardando outfit: $it", Toast.LENGTH_LONG).show()
                outfitsViewModel.limpiarMensajeError()
            }
        }

        outfitsViewModel.registroExitoso.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "¡Outfit guardado con éxito!", Toast.LENGTH_SHORT).show()
                outfitsViewModel.limpiarEstadoRegistro()
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validarOutfitMinimo(): Boolean {
        Log.d("RegistroDiario", "Validando outfit: T:${prendaTopSeleccionada!=null} B:${prendaBottomSeleccionada!=null} Z:${prendaZapatosSeleccionada!=null} BS:${prendaBodysuitSeleccionada!=null}")
        val topOk = prendaTopSeleccionada != null
        val bottomOk = prendaBottomSeleccionada != null
        val zapatosOk = prendaZapatosSeleccionada != null
        val bodysuitOk = prendaBodysuitSeleccionada != null

        val condicion1 = topOk && bottomOk && zapatosOk
        val condicion2 = bodysuitOk && zapatosOk
        val condicion3 = bodysuitOk && bottomOk && zapatosOk

        return condicion1 || condicion2 || condicion3
    }

    private fun guardarOutfit() {
        val outfitParaGuardar = mx.edu.itson.clothhangerapp.dataclases.Outfit(
            fecha = com.google.firebase.Timestamp.now(),
            top = prendaTopSeleccionada,
            bottom = prendaBottomSeleccionada,
            bodysuit = prendaBodysuitSeleccionada,
            zapatos = prendaZapatosSeleccionada,
            accesorio1 = prendaAccesorio1Seleccionada,
            accesorio2 = prendaAccesorio2Seleccionada,
            accesorio3 = prendaAccesorio3Seleccionada
        )
        Log.d("RegistroDiario", "Llamando a outfitsViewModel.registrarNuevoOutfit...")
        outfitsViewModel.registrarNuevoOutfit(outfitParaGuardar)
    }

}