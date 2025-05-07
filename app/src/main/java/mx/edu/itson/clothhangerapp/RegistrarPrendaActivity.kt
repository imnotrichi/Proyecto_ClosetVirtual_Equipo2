package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.mrudultora.colorpicker.ColorPickerPopUp
import com.mrudultora.colorpicker.ColorPickerPopUp.OnPickColorListener
import mx.edu.itson.clothhangerapp.adapters.PrendaAdapter
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel
import mx.edu.itson.clothhangerapp.databinding.ActivityRegistrarPrendaBinding
import mx.edu.itson.clothhangerapp.dataclases.Prenda

class RegistrarPrendaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarPrendaBinding
    private lateinit var prendaAdapter: PrendaAdapter
    private lateinit var viewModel: PrendasViewModel
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    var prendaEdit = Prenda()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_prenda)

        val btnAgregarImagen: ImageButton = findViewById(R.id.ibAgregarImagen)

        val etNombrePrenda: EditText = findViewById(R.id.etNombrePrenda)

        val ibTop: ImageButton = findViewById(R.id.ibTop)
        val ibBottom: ImageButton = findViewById(R.id.ibBottom)
        val ibZapatos: ImageButton = findViewById(R.id.ibZapatos)
        val ibBodysuit: ImageButton = findViewById(R.id.ibBodysuit)
        val ibAccesorio: ImageButton = findViewById(R.id.ibAccesorio)

        val tbSi: ToggleButton = findViewById(R.id.tbSi)
        val tbNo: ToggleButton = findViewById(R.id.tbNo)

        val tbCasual: ToggleButton = findViewById(R.id.tbCasual)
        val tbFormal: ToggleButton = findViewById(R.id.tbFormal)
        val tbDeportivo: ToggleButton = findViewById(R.id.tbDeportivo)
        val tbBasico: ToggleButton = findViewById(R.id.tbBasico)
        val tbFiesta: ToggleButton = findViewById(R.id.tbFiesta)

        val btnColor: Button = findViewById(R.id.btnColor)

        val btnRegistrarPrenda: Button = findViewById(R.id.btnRegistrarPrenda)

        val defaultColor = Color.WHITE

        var colorSeleccionado = ""

        viewModel = ViewModelProvider(this)[PrendasViewModel::class.java]

        btnColor.setOnClickListener {
            val colorPickerPopUp = ColorPickerPopUp(it.context) // Usamos el contexto de la vista.
            colorPickerPopUp.setShowAlpha(true) // Por defecto muestra el alpha (transparencia).
                .setDefaultColor(defaultColor) // Color rojo por defecto.
                .setDialogTitle("Pick a Color")
                .setOnPickColorListener(object : OnPickColorListener {
                    override fun onColorPicked(color: Int) {
                        // Cambiar el color del botón al color seleccionado
                        btnColor.setBackgroundColor(color)
                        colorSeleccionado = String.format("#%08X", color)
                    }

                    override fun onCancel() {
                        colorPickerPopUp.dismissDialog() // Cerrar el cuadro de diálogo.
                    }
                })
                .show()
        }

        btnAgregarImagen.setOnClickListener {
            try {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        var isTopsChecked = false
        var isBottomsChecked = false
        var isZapatosChecked = false
        var isBodysuitsChecked = false
        var isAccesoriosChecked = false

        var isCasualChecked = false
        var isFormalChecked = false
        var isDeportivoChecked = false
        var isBasicoChecked = false
        var isFiestaChecked = false

        var isSiChecked = false
        var isNoChecked = false

        tbSi.setOnClickListener {
            isSiChecked = !isSiChecked
            if (isSiChecked) {
                tbNo.setBackgroundResource(R.drawable.tag_view_off)
                tbNo.setTextColor(ContextCompat.getColor(this, R.color.black))

                isNoChecked = false

                tbSi.setBackgroundResource(R.drawable.tag_view_on)
                tbSi.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tbSi.setBackgroundResource(R.drawable.tag_view_off)
                tbSi.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbNo.setOnClickListener {
            isNoChecked = !isNoChecked
            if (isNoChecked) {
                tbSi.setBackgroundResource(R.drawable.tag_view_off)
                tbSi.setTextColor(ContextCompat.getColor(this, R.color.black))

                isSiChecked = false

                tbNo.setBackgroundResource(R.drawable.tag_view_on)
                tbNo.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tbNo.setBackgroundResource(R.drawable.tag_view_off)
                tbNo.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        ibTop.setOnClickListener {
            isTopsChecked = !isTopsChecked
            if (isTopsChecked) {
                ibBottom.setImageResource(R.drawable.icono_bottoms_negro)
                ibBottom.setBackgroundResource(R.drawable.tag_view_off)

                ibZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                ibZapatos.setBackgroundResource(R.drawable.tag_view_off)

                ibBodysuit.setImageResource(R.drawable.icono_bodysuits_negro)
                ibBodysuit.setBackgroundResource(R.drawable.tag_view_off)

                ibAccesorio.setImageResource(R.drawable.icono_accesorios_negro)
                ibAccesorio.setBackgroundResource(R.drawable.tag_view_off)

                isAccesoriosChecked = false
                isBottomsChecked = false
                isZapatosChecked = false
                isBodysuitsChecked = false

                ibTop.setImageResource(R.drawable.icono_tops_blanco)
                ibTop.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                ibTop.setImageResource(R.drawable.icono_tops_negro)
                ibTop.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        ibBottom.setOnClickListener {
            isBottomsChecked = !isBottomsChecked
            if (isBottomsChecked) {
                ibTop.setImageResource(R.drawable.icono_tops_negro)
                ibTop.setBackgroundResource(R.drawable.tag_view_off)

                ibZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                ibZapatos.setBackgroundResource(R.drawable.tag_view_off)

                ibBodysuit.setImageResource(R.drawable.icono_bodysuits_negro)
                ibBodysuit.setBackgroundResource(R.drawable.tag_view_off)

                ibAccesorio.setImageResource(R.drawable.icono_accesorios_negro)
                ibAccesorio.setBackgroundResource(R.drawable.tag_view_off)

                isTopsChecked = false
                isAccesoriosChecked = false
                isZapatosChecked = false
                isBodysuitsChecked = false

                ibBottom.setImageResource(R.drawable.icono_bottoms_blanco)
                ibBottom.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                ibBottom.setImageResource(R.drawable.icono_bottoms_negro)
                ibBottom.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        ibZapatos.setOnClickListener {
            isZapatosChecked = !isZapatosChecked
            if (isZapatosChecked) {
                ibBottom.setImageResource(R.drawable.icono_bottoms_negro)
                ibBottom.setBackgroundResource(R.drawable.tag_view_off)

                ibTop.setImageResource(R.drawable.icono_tops_negro)
                ibTop.setBackgroundResource(R.drawable.tag_view_off)

                ibBodysuit.setImageResource(R.drawable.icono_bodysuits_negro)
                ibBodysuit.setBackgroundResource(R.drawable.tag_view_off)

                ibAccesorio.setImageResource(R.drawable.icono_accesorios_negro)
                ibAccesorio.setBackgroundResource(R.drawable.tag_view_off)

                isTopsChecked = false
                isBottomsChecked = false
                isAccesoriosChecked = false
                isBodysuitsChecked = false

                ibZapatos.setImageResource(R.drawable.icono_zapatos_blanco)
                ibZapatos.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                ibZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                ibZapatos.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        ibBodysuit.setOnClickListener {
            isBodysuitsChecked = !isBodysuitsChecked
            if (isBodysuitsChecked) {
                ibBottom.setImageResource(R.drawable.icono_bottoms_negro)
                ibBottom.setBackgroundResource(R.drawable.tag_view_off)

                ibZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                ibZapatos.setBackgroundResource(R.drawable.tag_view_off)

                ibTop.setImageResource(R.drawable.icono_tops_negro)
                ibTop.setBackgroundResource(R.drawable.tag_view_off)

                ibAccesorio.setImageResource(R.drawable.icono_accesorios_negro)
                ibAccesorio.setBackgroundResource(R.drawable.tag_view_off)

                isTopsChecked = false
                isBottomsChecked = false
                isZapatosChecked = false
                isAccesoriosChecked = false

                ibBodysuit.setImageResource(R.drawable.icono_bodysuit_blanco)
                ibBodysuit.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                ibBodysuit.setImageResource(R.drawable.icono_bodysuits_negro)
                ibBodysuit.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        ibAccesorio.setOnClickListener {
            isAccesoriosChecked = !isAccesoriosChecked
            if (isAccesoriosChecked) {
                ibBottom.setImageResource(R.drawable.icono_bottoms_negro)
                ibBottom.setBackgroundResource(R.drawable.tag_view_off)

                ibZapatos.setImageResource(R.drawable.icono_zapatos_negro)
                ibZapatos.setBackgroundResource(R.drawable.tag_view_off)

                ibBodysuit.setImageResource(R.drawable.icono_bodysuits_negro)
                ibBodysuit.setBackgroundResource(R.drawable.tag_view_off)

                ibTop.setImageResource(R.drawable.icono_tops_negro)
                ibTop.setBackgroundResource(R.drawable.tag_view_off)

                isTopsChecked = false
                isBottomsChecked = false
                isZapatosChecked = false
                isBodysuitsChecked = false

                ibAccesorio.setImageResource(R.drawable.icono_accesorios_blanco)
                ibAccesorio.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                ibAccesorio.setImageResource(R.drawable.icono_accesorios_negro)
                ibAccesorio.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbCasual.setOnClickListener {
            isCasualChecked = !isCasualChecked
            if (isCasualChecked) {
                tbFormal.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFormal.setBackgroundResource(R.drawable.tag_view_off)

                tbDeportivo.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbDeportivo.setBackgroundResource(R.drawable.tag_view_off)

                tbBasico.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbBasico.setBackgroundResource(R.drawable.tag_view_off)

                tbFiesta.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFiesta.setBackgroundResource(R.drawable.tag_view_off)

                isFormalChecked = false
                isDeportivoChecked = false
                isBasicoChecked = false
                isFiestaChecked = false

                tbCasual.setTextColor(ContextCompat.getColor(this, R.color.white))
                tbCasual.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                tbCasual.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbCasual.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbFormal.setOnClickListener {
            isFormalChecked = !isFormalChecked
            if (isFormalChecked) {
                tbCasual.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbCasual.setBackgroundResource(R.drawable.tag_view_off)

                tbDeportivo.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbDeportivo.setBackgroundResource(R.drawable.tag_view_off)

                tbBasico.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbBasico.setBackgroundResource(R.drawable.tag_view_off)

                tbFiesta.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFiesta.setBackgroundResource(R.drawable.tag_view_off)

                isCasualChecked = false
                isDeportivoChecked = false
                isBasicoChecked = false
                isFiestaChecked = false

                tbFormal.setTextColor(ContextCompat.getColor(this, R.color.white))
                tbFormal.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                tbFormal.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFormal.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbDeportivo.setOnClickListener {
            isDeportivoChecked = !isDeportivoChecked
            if (isDeportivoChecked) {
                tbFormal.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFormal.setBackgroundResource(R.drawable.tag_view_off)

                tbCasual.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbCasual.setBackgroundResource(R.drawable.tag_view_off)

                tbBasico.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbBasico.setBackgroundResource(R.drawable.tag_view_off)

                tbFiesta.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFiesta.setBackgroundResource(R.drawable.tag_view_off)

                isFormalChecked = false
                isCasualChecked = false
                isBasicoChecked = false
                isFiestaChecked = false

                tbDeportivo.setTextColor(ContextCompat.getColor(this, R.color.white))
                tbDeportivo.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                tbDeportivo.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbDeportivo.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbBasico.setOnClickListener {
            isBasicoChecked = !isBasicoChecked
            if (isBasicoChecked) {
                tbFormal.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFormal.setBackgroundResource(R.drawable.tag_view_off)

                tbDeportivo.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbDeportivo.setBackgroundResource(R.drawable.tag_view_off)

                tbCasual.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbCasual.setBackgroundResource(R.drawable.tag_view_off)

                tbFiesta.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFiesta.setBackgroundResource(R.drawable.tag_view_off)

                isFormalChecked = false
                isDeportivoChecked = false
                isCasualChecked = false
                isFiestaChecked = false

                tbBasico.setTextColor(ContextCompat.getColor(this, R.color.white))
                tbBasico.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                tbBasico.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbBasico.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        tbFiesta.setOnClickListener {
            isFiestaChecked = !isFiestaChecked
            if (isFiestaChecked) {
                tbFormal.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFormal.setBackgroundResource(R.drawable.tag_view_off)

                tbDeportivo.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbDeportivo.setBackgroundResource(R.drawable.tag_view_off)

                tbBasico.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbBasico.setBackgroundResource(R.drawable.tag_view_off)

                tbCasual.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbCasual.setBackgroundResource(R.drawable.tag_view_off)

                isFormalChecked = false
                isDeportivoChecked = false
                isBasicoChecked = false
                isCasualChecked = false

                tbFiesta.setTextColor(ContextCompat.getColor(this, R.color.white))
                tbFiesta.setBackgroundResource(R.drawable.tag_view_on)
            } else {
                tbFiesta.setTextColor(ContextCompat.getColor(this, R.color.black))
                tbFiesta.setBackgroundResource(R.drawable.tag_view_off)
            }
        }

        btnRegistrarPrenda.setOnClickListener {
            if ((!isTopsChecked || !isBottomsChecked || !isZapatosChecked || !isBodysuitsChecked
                        || !isAccesoriosChecked) && (!isSiChecked || !isNoChecked) && etNombrePrenda.text.isEmpty()
            ) {
                Toast.makeText(this, "Asegúrese de ingresar toda la información solicitada.", Toast.LENGTH_SHORT).show()
            } else {
                val prendaNueva = Prenda()

                prendaNueva.nombre = etNombrePrenda.text.toString()

                if (isTopsChecked) {
                    prendaNueva.categoria = "Top"
                } else if (isBottomsChecked) {
                    prendaNueva.categoria = "Bottom"
                } else if (isZapatosChecked) {
                    prendaNueva.categoria = "Zapatos"
                } else if (isBodysuitsChecked) {
                    prendaNueva.categoria = "Bodysuit"
                } else if (isAccesoriosChecked) {
                    prendaNueva.categoria = "Accesorio"
                }

                if (isSiChecked) {
                    prendaNueva.estampado = true
                } else if (isNoChecked) {
                    prendaNueva.estampado = false
                }

                if (isCasualChecked) {
                    prendaNueva.etiquetas.add("Casual")
                } else if (isFormalChecked) {
                    prendaNueva.etiquetas.add("Formal")
                } else if (isDeportivoChecked) {
                    prendaNueva.etiquetas.add("Deportivo")
                } else if (isBasicoChecked) {
                    prendaNueva.etiquetas.add("Básico")
                } else if (isFiestaChecked) {
                    prendaNueva.etiquetas.add("Fiesta")
                }

                prendaNueva.colorHex = colorSeleccionado
                prendaNueva.usosMensuales = 0
                prendaNueva.usosTotales = 0

                viewModel.agregarPrenda(prendaNueva)

                Toast.makeText(this, "Prenda registrada con éxito.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}