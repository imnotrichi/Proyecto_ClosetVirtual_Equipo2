package mx.edu.itson.clothhangerapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import java.util.UUID

class PrendasViewModel: ViewModel() {

    private val db = Firebase.firestore

    private var _listaPrendas = MutableLiveData<List<Prenda>>(emptyList())
    val listaPrendas: LiveData<List<Prenda>> = _listaPrendas

    init {
        obtenerPrendas()
    }

    fun obtenerPrendas() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultado = db.collection("prendas").get().await()

                val prendas = resultado.documents.mapNotNull { it.toObject(Prenda::class.java) }
                _listaPrendas.postValue(prendas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarPrenda(prenda: Prenda) {
        prenda.id = UUID.randomUUID().toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("prendas").document(prenda.id).set(prenda).await()
                Log.d("Firebase", "Prenda registrada correctamente: ${prenda.id}")

                // Asegúrate de actualizar la lista correctamente
                val nuevaLista = _listaPrendas.value?.toMutableList() ?: mutableListOf()
                nuevaLista.add(prenda)
                _listaPrendas.postValue(nuevaLista)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}