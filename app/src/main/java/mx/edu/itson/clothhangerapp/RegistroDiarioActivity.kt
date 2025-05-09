package mx.edu.itson.clothhangerapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
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

        Log.d("RegistroDiario", "onCreate: Llamando a observarPrendasViewModel y observarOutfitsViewModel")
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
        Log.d("RegistroDiario", "FUNCIÓN observarPrendasViewModel LLAMADA - Configurando observadores.")
        prendasViewModel.prendaSeleccionadaDetalle.observe(this) { prendaRecibida ->
            Log.d("RegistroDiario", ">>>> OBSERVADOR PRENDA DETALLE SE DISPARÓ <<<<")

            if (prendaRecibida != null) {
                Log.d("RegistroDiario", "Observador: Prenda NO es null. Nombre: ${prendaRecibida.nombre}, Slot: $slotActualizando")

                // SOLO actuar si también tenemos un slot que actualizar
                if (slotActualizando != null) {
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
                        val urlParaCargar = prendaRecibida.imagenUrl
                        Log.d("RegistroDiario", "Target ID: ${resources.getResourceEntryName(target.id)}, URL a cargar: $urlParaCargar")

                        target.setBackgroundResource(android.R.color.transparent)
                        target.scaleType = ImageView.ScaleType.FIT_CENTER

                        Glide.with(this)
                            .load(urlParaCargar)
                            .into(target)
                        Log.d("RegistroDiario_Glide", "Llamada a .into(target) realizada para ${resources.getResourceEntryName(target.id)} (SIN LISTENER).")

                    } ?: Log.w("RegistroDiario", "imageViewTarget fue null para slot $slotActualizando")

                    // Resetear slot DESPUÉS de haberlo usado con una prenda válida
                    slotActualizando = null
                } else {
                    // Prenda recibida pero no sabemos para qué slot era (slotActualizando ya era null)
                    // Esto podría pasar si el LiveData se re-emite por algún motivo de ciclo de vida.
                    // Generalmente seguro ignorarlo si ya no hay un slot activo.
                    Log.d("RegistroDiario", "Prenda recibida pero slotActualizando es null, ignorando actualización de UI.")
                }

            } else { // prendaRecibida ES null
                Log.d("RegistroDiario", "Observador: Prenda ES null. Slot: $slotActualizando")
                // Si prendaRecibida es null, no hacemos nada con la UI de imagen,
                // pero NO reseteamos slotActualizando aquí, porque todavía estamos esperando
                // la prenda real para ese slot.
                // El slotActualizando solo se resetea después de una actualización exitosa o si la selección se cancela/falla.
            }
        }

        prendasViewModel.errorDetalle.observe(this) { error ->
            error?.let {
                Log.e("RegistroDiario", "ErrorDetalle observado: $it")
                Toast.makeText(this, "Error detalle: $it", Toast.LENGTH_LONG).show()
                slotActualizando = null // Si hay error al obtener detalle, reseteamos el slot
                // Considera llamar a prendasViewModel.limpiarErrorDetalle()
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
        val hayTop = prendaTopSeleccionada != null
        val hayBottom = prendaBottomSeleccionada != null
        val hayZapatos = prendaZapatosSeleccionada != null
        val hayBodysuit = prendaBodysuitSeleccionada != null

        Log.d("RegistroDiario", "Validando outfit -> Top: $hayTop, Bottom: $hayBottom, Zapatos: $hayZapatos, Bodysuit: $hayBodysuit")

        // Condición 1: Outfit "tradicional" -> Top, Bottom y Zapatos (y NO hay bodysuit seleccionado)
        val condicion1 = hayTop && hayBottom && hayZapatos && !hayBodysuit

        // Condición 2: Outfit con Bodysuit -> Bodysuit y Zapatos (y NO hay top seleccionado, el bottom es opcional aquí)
        // Esta cubre:
        //    - Bodysuit + Zapatos
        //    - Bodysuit + Bottom + Zapatos
        val condicion2 = hayBodysuit && hayZapatos && !hayTop

        // Condición 3: Outfit con Bodysuit Y Top -> Bodysuit, Top, Bottom y Zapatos (tu nueva regla explícita)
        val condicion3 = hayBodysuit && hayTop && hayBottom && hayZapatos

        // Si se cumple CUALQUIERA de estas condiciones, el outfit es válido
        if (condicion1 || condicion2 || condicion3) {
            Log.d("RegistroDiario", "Validación MÍNIMA CUMPLIDA (Cond1: $condicion1, Cond2: $condicion2, Cond3: $condicion3)")
            return true
        } else {
            // Si no se cumple ninguna, damos mensajes de error más específicos
            // Estos mensajes ayudan al usuario a entender qué falta. Puedes personalizarlos.
            if (!hayZapatos) {
                Toast.makeText(this, "El outfit necesita al menos Zapatos.", Toast.LENGTH_LONG).show()
            } else if (hayTop && !hayBottom && !hayBodysuit) { // Tiene Top, pero no Bottom (y no es un caso con Bodysuit)
                Toast.makeText(this, "Un Top necesita un Bottom.", Toast.LENGTH_LONG).show()
            } else if (hayBodysuit && !hayTop && !hayZapatos) { // Tiene Bodysuit, pero no Zapatos (ya cubierto por !hayZapatos arriba pero por claridad)
                // Este caso ya está cubierto por el primer if (!hayZapatos)
            } else if (!hayTop && !hayBodysuit) { // No tiene ni Top ni Bodysuit (y ya sabemos que tiene zapatos)
                Toast.makeText(this, "Necesitas seleccionar un Top o un Bodysuit.", Toast.LENGTH_LONG).show()
            }
            // Puedes añadir más lógica de mensajes de error específicos si lo deseas
            // o un mensaje genérico final
            else {
                Toast.makeText(this, "Combinación de prendas no válida. Revisa las prendas mínimas.", Toast.LENGTH_LONG).show()
            }
            Log.d("RegistroDiario", "Validación MÍNIMA NO CUMPLIDA")
            return false
        }
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