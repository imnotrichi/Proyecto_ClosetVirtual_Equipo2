package mx.edu.itson.clothhangerapp

import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val tbTops:ToggleButton = findViewById(R.id.tbTops)
        val tbBottoms:ToggleButton = findViewById(R.id.tbBottoms)
        val tbZapatos:ToggleButton = findViewById(R.id.tbZapatos)
        val tbBodysuits:ToggleButton = findViewById(R.id.tbBodysuits)
        val tbAccesorios:ToggleButton = findViewById(R.id.tbAccesorios)

        tbTops.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbTops.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbTops.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tbTops.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbTops.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbBottoms.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbBottoms.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbBottoms.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tbBottoms.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBottoms.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbZapatos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbZapatos.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbZapatos.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tbZapatos.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbZapatos.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbBodysuits.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbBodysuits.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbBodysuits.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tbBodysuits.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbBodysuits.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        tbAccesorios.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tbAccesorios.background = ContextCompat.getDrawable(this, R.drawable.tag_view_on)
                tbAccesorios.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                tbAccesorios.background = ContextCompat.getDrawable(this, R.drawable.tag_view_off)
                tbAccesorios.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

    }
}