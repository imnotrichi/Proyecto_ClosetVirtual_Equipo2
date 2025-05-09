package mx.edu.itson.clothhangerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide
import mx.edu.itson.clothhangerapp.viewmodels.OutfitsViewModel

class CatalogoOutfit : MenuNavegable() {
    private lateinit var viewModel: OutfitsViewModel
    var adapter: OutfitViewAdapter? = null
    var outfits = ArrayList<OutfitView>()

    lateinit var categoria: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_outfits)

        adapter = OutfitViewAdapter(this, outfits)
        val lista: LinearLayout = findViewById(R.id.llOutfits)
        val inflater = LayoutInflater.from(this)
        outfits.forEach { outfit ->
            val view = adapter!!.getView(outfits.indexOf(outfit), null, lista)
            lista.addView(view)
        }

        setupBottomNavigation()
        setSelectedItem(R.id.nav_home)

    }
}

class OutfitViewAdapter : BaseAdapter {

    var outfits = ArrayList<OutfitView>()
    var context: Context? = null

    constructor(context: Context, outfits: ArrayList<OutfitView>) : super() {
        this.outfits = outfits
        this.context = context
    }

    override fun getCount(): Int {
        return outfits.size
    }

    override fun getItem(position: Int): Any {
        return outfits[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val outfit = outfits[position]
        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vista = inflator.inflate(R.layout.outfit_view, null)
        val top: ImageView = vista.findViewById(R.id.ivTop)
        val bodysuit: ImageView = vista.findViewById(R.id.ivBodysuit)
        val bottom: ImageView = vista.findViewById(R.id.ivBottom)
        val zapatos: ImageView = vista.findViewById(R.id.ivZapatos)
        val accesorio1: ImageView = vista.findViewById(R.id.ivAccesorio1)
        val accesorio2: ImageView = vista.findViewById(R.id.ivAccesorio2)
        val accesorio3: ImageView = vista.findViewById(R.id.ivAccesorio3)

        // Cargar imagen del top
        Glide.with(context!!)
            .load(outfit.top)
            .placeholder(R.drawable.placeholder) // Opcional: imagen mientras carga
            .into(top)

        Glide.with(context!!)
            .load(outfit.bodysuit)
            .placeholder(R.drawable.placeholder) // Opcional: imagen mientras carga
            .into(bodysuit)

        Glide.with(context!!)
            .load(outfit.bottom)
            .placeholder(R.drawable.placeholder) // Opcional: imagen mientras carga
            .into(bottom)

        Glide.with(context!!)
            .load(outfit.zapatos)
            .placeholder(R.drawable.placeholder) // Opcional: imagen mientras carga
            .into(zapatos)

        Glide.with(context!!)
            .load(outfit.accesorio1)
            .placeholder(R.drawable.placeholder) // Opcional: imagen mientras carga
            .into(accesorio1)

        Glide.with(context!!)
            .load(outfit.accesorio2)
            .placeholder(R.drawable.placeholder) // Opcional: imagen mientras carga
            .into(accesorio2)

        Glide.with(context!!)
            .load(outfit.accesorio3)
            .placeholder(R.drawable.placeholder) // Opcional: imagen mientras carga
            .into(accesorio3)

        top.setOnClickListener {
            val intento = Intent(context, DetalleOutfitActivity::class.java)
            context!!.startActivity(intento)
        }

        bodysuit.setOnClickListener {
            val intento = Intent(context, DetalleOutfitActivity::class.java)
            context!!.startActivity(intento)
        }

        bottom.setOnClickListener {
            val intento = Intent(context, DetalleOutfitActivity::class.java)
            context!!.startActivity(intento)
        }

        zapatos.setOnClickListener {
            val intento = Intent(context, DetalleOutfitActivity::class.java)
            context!!.startActivity(intento)
        }

        accesorio1.setOnClickListener {
            val intento = Intent(context, DetalleOutfitActivity::class.java)
            context!!.startActivity(intento)
        }

        accesorio2.setOnClickListener {
            val intento = Intent(context, DetalleOutfitActivity::class.java)
            context!!.startActivity(intento)
        }

        accesorio3.setOnClickListener {
            val intento = Intent(context, DetalleOutfitActivity::class.java)
            context!!.startActivity(intento)
        }

        return vista
    }
}
