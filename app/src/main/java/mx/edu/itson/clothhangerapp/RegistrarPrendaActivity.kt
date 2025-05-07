package mx.edu.itson.clothhangerapp

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mrudultora.colorpicker.ColorPickerPopUp
import com.mrudultora.colorpicker.ColorPickerPopUp.OnPickColorListener
import mx.edu.itson.clothhangerapp.adapters.PrendaAdapter
import mx.edu.itson.clothhangerapp.viewmodels.PrendasViewModel
import mx.edu.itson.clothhangerapp.databinding.ActivityRegistrarPrendaBinding
import mx.edu.itson.clothhangerapp.dataclases.Categoria
import mx.edu.itson.clothhangerapp.dataclases.Prenda

class RegistrarPrendaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarPrendaBinding
    private lateinit var adapter: PrendaAdapter
    private lateinit var viewModel: PrendasViewModel
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    var prendaEdit = Prenda()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_prenda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgregarImagen: ImageButton = findViewById(R.id.ibAgregar)

        val etNombrePrenda: EditText = findViewById(R.id.etNombrePrenda)

        val ibTop: ImageButton = findViewById(R.id.ibTop)
        val ibBottom: ImageButton = findViewById(R.id.ibBottom)
        val ibZapatos: ImageButton = findViewById(R.id.ibZapatos)
        val ibBodysuit: ImageButton = findViewById(R.id.ibBodysuit)
        val ibAccesorio: ImageButton = findViewById(R.id.ibAccesorio)

        val tbSi: ToggleButton = findViewById(R.id.tbSi)
        val tbNo: ToggleButton = findViewById(R.id.tbNo)

        val btnColor: Button = findViewById(R.id.btnColor)

        val tvError: TextView = findViewById(R.id.tvErrorRegistrarPrenda)

        tvError.visibility = View.INVISIBLE

        val btnRegistrarPrenda: Button = findViewById(R.id.btnRegistrarPrenda)

        val defaultColor = Color.BLACK

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageView.setImageURI(it)
                // Aquí puedes guardar el URI o procesar la imagen
            }
        }

        btnColor.setOnClickListener {
            val colorPickerPopUp = ColorPickerPopUp(it.context) // Usamos el contexto de la vista.
            colorPickerPopUp.setShowAlpha(true) // Por defecto muestra el alpha (transparencia).
                .setDefaultColor(defaultColor) // Color rojo por defecto.
                .setDialogTitle("Pick a Color")
                .setOnPickColorListener(object : OnPickColorListener {
                    override fun onColorPicked(color: Int) {
                        // Cambiar el color del botón al color seleccionado
                        btnColor.setBackgroundColor(color)
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

        btnRegistrarPrenda.setOnClickListener {
            if ((!isTopsChecked || !isBottomsChecked || !isZapatosChecked || !isBodysuitsChecked
                        || !isAccesoriosChecked) && (!isSiChecked || !isNoChecked) && etNombrePrenda.text.isEmpty()
            ) {
                tvError.text = "Asegúrese de ingresar todos los datos."
                tvError.visibility = View.VISIBLE
            } else {
                prendaEdit.nombre = etNombrePrenda.text.toString()

                if (isTopsChecked) {
                    prendaEdit.categoria = Categoria("Top")
                } else if (isBottomsChecked) {
                    prendaEdit.categoria = Categoria("Bottom")
                } else if (isZapatosChecked) {
                    prendaEdit.categoria = Categoria("Zapatos")
                } else if (isBodysuitsChecked) {
                    prendaEdit.categoria = Categoria("Bodysuit")
                } else if (isAccesoriosChecked) {
                    prendaEdit.categoria = Categoria("Accesorio")
                }

                if (isSiChecked) {
                    prendaEdit.estampado = true
                } else if (isNoChecked) {
                    prendaEdit.estampado = false
                }


            }
        }

    }
}