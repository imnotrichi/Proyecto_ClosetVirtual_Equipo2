package mx.edu.itson.clothhangerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import mx.edu.itson.clothhangerapp.R
import mx.edu.itson.clothhangerapp.dataclases.Prenda

class PrendaAdapter(
    var listaPrendas: List<Prenda>
) : RecyclerView.Adapter<PrendaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnImagen: ImageButton = itemView.findViewById(R.id.ibAgregarImagen)

        val etNombrePrenda: EditText = itemView.findViewById(R.id.etNombrePrenda)

        val ibTop: ImageButton = itemView.findViewById(R.id.ibTop)
        val ibBottom: ImageButton = itemView.findViewById(R.id.ibBottom)
        val ibZapatos: ImageButton = itemView.findViewById(R.id.ibZapatos)
        val ibBodysuit: ImageButton = itemView.findViewById(R.id.ibBodysuit)
        val ibAccesorio: ImageButton = itemView.findViewById(R.id.ibAccesorio)

        val tbSi: ToggleButton = itemView.findViewById(R.id.tbSi)
        val tbNo: ToggleButton = itemView.findViewById(R.id.tbNo)

        val tbCasual: ToggleButton = itemView.findViewById(R.id.tbCasual)
        val tbFormal: ToggleButton = itemView.findViewById(R.id.tbFormal)
        val tbDeportivo: ToggleButton = itemView.findViewById(R.id.tbDeportivo)
        val tbBasico: ToggleButton = itemView.findViewById(R.id.tbBasico)
        val tbFiesta: ToggleButton = itemView.findViewById(R.id.tbFiesta)

        val btnColor: Button = itemView.findViewById(R.id.btnColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_registrar_prenda, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaPrendas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prenda = listaPrendas[position]

        // Enlazar la prenda con las vistas
        holder.etNombrePrenda.setText(prenda.nombre)

        // Aquí puedes enlazar los ToggleButtons y otros botones con los valores de la prenda
        if (prenda.estampado) {
            holder.tbSi.isChecked = true  // Ejemplo de atributo booleano
        } else {
            holder.tbNo.isChecked = true  // Inversa del valor anterior
        }

        holder.ibTop.isSelected = prenda.categoria == "Top"
        holder.ibBottom.isSelected = prenda.categoria == "Bottom"
        holder.ibZapatos.isSelected = prenda.categoria == "Zapatos"
        holder.ibBodysuit.isSelected = prenda.categoria == "Bodysuit"
        holder.ibAccesorio.isSelected = prenda.categoria == "Accesorio"

        // Suponiendo que tienes categorías como "casual", "formal", etc.:
        holder.tbCasual.isChecked = prenda.etiquetas.contains("Casual")
        holder.tbFormal.isChecked = prenda.etiquetas.contains("Formal")
        holder.tbDeportivo.isChecked = prenda.etiquetas.contains("Deportivo")
        holder.tbBasico.isChecked = prenda.etiquetas.contains("Básico")
        holder.tbFiesta.isChecked = prenda.etiquetas.contains("Fiesta")
    }
}