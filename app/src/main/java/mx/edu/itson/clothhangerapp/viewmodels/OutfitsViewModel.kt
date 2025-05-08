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
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _errorMensaje.value = "Debes iniciar sesión para registrar un outfit."
            _registroExitoso.value = false
            return
        }

        outfit.userId = currentUser.uid
        _isLoading.value = true
        _registroExitoso.value = false

        viewModelScope.launch {
            try {
                // Guardar outfit en subcolección del usuario
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(currentUser.uid)
                        .collection("outfits")
                        .add(outfit)
                        .await()
                }

                // Guardar outfit en colección raíz "outfits"
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("outfits")
                        .add(outfit)
                        .await()
                }

                Log.d("OutfitsViewModel", "Outfit registrado con éxito.")
                _registroExitoso.postValue(true)

            } catch (e: Exception) {
                Log.e("OutfitssViewModel", "Error al registrar outfit: ${e.message}", e)
                _errorMensaje.postValue("Error al registrar outfit: ${e.message}")
                _registroExitoso.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun limpiarMensajeError() {
        _errorMensaje.value = null
    }

    fun limpiarEstadoRegistro() {
        _registroExitoso.value = false
    }
}