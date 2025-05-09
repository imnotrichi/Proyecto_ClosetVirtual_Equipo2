package mx.edu.itson.clothhangerapp.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import mx.edu.itson.clothhangerapp.dataclases.Outfit
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import java.util.UUID
import com.google.firebase.firestore.FieldValue // <-- ¡IMPORTANTE PARA INCREMENT!
import com.google.firebase.firestore.WriteBatch

class OutfitsViewModel : ViewModel() {

    private val firestoreDb = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var prendasViewModel: PrendasViewModel

    private val _listaOutfitsUsuario = MutableLiveData<List<Outfit>>()
    val listaOutfitsUsuario: LiveData<List<Outfit>> = _listaOutfitsUsuario

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMensaje = MutableLiveData<String?>()
    val errorMensaje: LiveData<String?> = _errorMensaje

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    /**
     * Carga los outfits del usuario autenticado desde su colección en Firestore.
     */
    fun obtenerOutfitsDelUsuario() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _errorMensaje.postValue("Usuario no autenticado para cargar outifts.")
            _listaOutfitsUsuario.postValue(emptyList())
            return
        }

        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val resultado = withContext(Dispatchers.IO) {
                    firestoreDb.collection("outfits")
                        .whereEqualTo("userId", userId)
                        .get()
                        .await()
                }

                val outfits = resultado.documents.mapNotNull { it.toObject(Outfit::class.java) }
                _listaOutfitsUsuario.postValue(outfits)
            } catch (e: Exception) {
                Log.e("OutfitsViewModel", "Error al obtener outfits: ${e.message}", e)
                _errorMensaje.postValue("Error al cargar outfits: ${e.message}")
                _listaOutfitsUsuario.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun vaciarLista() {
        _listaOutfitsUsuario.value = emptyList()
    }

    fun registrarNuevoOutfit(outfit: Outfit) {
        val currentUser = auth.currentUser // Obtiene el usuario DENTRO del ViewModel
        if (currentUser == null) {
            _errorMensaje.value = "Debes iniciar sesión para registrar un outfit."
            _registroExitoso.value = false
            return
        }

        outfit.userId = currentUser.uid // <-- ASIGNA EL userId AQUÍ

        _isLoading.value = true
        _registroExitoso.value = false

        viewModelScope.launch {
            try {
                // Guardar outfit en la colección raíz "outfits" (o donde decidas)
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("outfits") // Guardando en la colección raíz como pide el usuario
                        .add(outfit) // Guarda el outfit que AHORA SÍ tiene el userId
                        .await()
                }

                Log.d("OutfitsViewModel", "Outfit registrado con éxito.")
                _registroExitoso.postValue(true)

                // --- AQUÍ IMPLEMENTAREMOS LUEGO EL INCREMENTO DE CONTADORES ---
                // Necesitarás los IDs de las prendas que están DENTRO del objeto 'outfit'
//                incrementarContadoresPrendas(outfit) // Llamar a función auxiliar

            } catch (e: Exception) {
                Log.e("OutfitsViewModel", "Error al registrar outfit: ${e.message}", e) // Corregido Tag
                _errorMensaje.postValue("Error al registrar outfit: ${e.message}")
                _registroExitoso.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Incrementa los contadores de uso (usosTotales, usosMensuales) para cada
     * prenda válida incluida en el outfit recién guardado.
     * Utiliza un Batch Write para eficiencia y atomicidad parcial.
     *
     * @param outfit El objeto Outfit que se acaba de guardar (ya debe tener el userId asignado).
     */
    private suspend fun incrementarContadoresPrendas(outfit: Outfit) {
        val userId = outfit.userId // El userId del dueño del outfit (y de las prendas originales)
        if (userId == null) {
            Log.e("OutfitsViewModel", "No se puede incrementar contadores: userId es nulo en el outfit.")
            return
        }

        // Crear una lista con todas las prendas (no nulas) del outfit
        // que también tengan un ID (el ID original de la prenda)
        val prendasAActualizar = listOfNotNull(
            outfit.top,
            outfit.bottom,
            outfit.bodysuit,
            outfit.zapatos,
            outfit.accesorio1,
            outfit.accesorio2,
            outfit.accesorio3
        ).filter { !it.id.isNullOrBlank() } // Filtrar solo las que tengan un ID válido

        if (prendasAActualizar.isEmpty()) {
            Log.d("OutfitsViewModel", "No hay prendas con ID en el outfit para incrementar contadores.")
            return // No hay nada que hacer
        }

        Log.d("OutfitsViewModel", "Preparando batch para incrementar contadores de ${prendasAActualizar.size} prendas.")

        try {
            val batch: WriteBatch = firestoreDb.batch()

            prendasAActualizar.forEach { prendaEnOutfit ->
                // El ID de la prenda original está en prendaEnOutfit.id
                val prendaOriginalId = prendaEnOutfit.id!! // Usamos !! porque ya filtramos por no nulo/blanco

                // Ruta al documento original de la prenda en la subcolección del usuario
                val prendaOriginalRef = firestoreDb.collection("usuarios")
                    .document(userId) // Usa el userId del dueño del outfit
                    .collection("prendas")
                    .document(prendaOriginalId)

                batch.update(prendaOriginalRef, mapOf(
                    "usosTotales" to FieldValue.increment(1),
                    "usosMensuales" to FieldValue.increment(1)
                ))
                Log.d("OutfitsViewModel", "Batch: Incrementar usos para prenda ID: $prendaOriginalId")
            }

            // Ejecutar el Batch Write
            withContext(Dispatchers.IO) {
                batch.commit().await()
            }
            Log.d("OutfitsViewModel", "Contadores de uso de prendas actualizados con éxito.")

        } catch (e: Exception) {
            Log.e("OutfitsViewModel", "Error al actualizar contadores de uso de prendas: ${e.message}", e)
            // Considera cómo quieres manejar este error secundario. Por ahora, solo se loguea.
        }
    }

    fun limpiarMensajeError() {
        _errorMensaje.value = null
    }

    fun limpiarEstadoRegistro() {
        _registroExitoso.value = false
    }
}