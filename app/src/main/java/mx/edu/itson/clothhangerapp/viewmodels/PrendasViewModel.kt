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
import mx.edu.itson.clothhangerapp.dataclases.Prenda
import java.util.UUID

class PrendasViewModel: ViewModel() {

    private val firestoreDb = Firebase.firestore
    private val storageRef = Firebase.storage.reference
    private val auth = Firebase.auth

    private val _listaPrendasUsuario = MutableLiveData<List<Prenda>>()
    val listaPrendasUsuario: LiveData<List<Prenda>> = _listaPrendasUsuario

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMensaje = MutableLiveData<String?>()
    val errorMensaje: LiveData<String?> = _errorMensaje

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    /**
     * Obtiene las prendas del usuario actualmente autenticado desde Firestore.
     * Útil para mostrar el clóset en otra pantalla.
     */
    fun obtenerPrendasDelUsuario() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _errorMensaje.postValue("Usuario no autenticado para cargar prendas.")
            _listaPrendasUsuario.postValue(emptyList())
            return
        }

        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val resultado = withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(userId)
                        .collection("prendas")
                        .orderBy("nombre")
                        .get()
                        .await()
                }

                val prendas = resultado.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(Prenda::class.java)
                }
                _listaPrendasUsuario.postValue(prendas)
            } catch (e: Exception) {
                Log.e("PrendasViewModel", "Error al obtener prendas: ${e.message}", e)
                _errorMensaje.postValue("Error al cargar prendas: ${e.message}")
                _listaPrendasUsuario.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Registra una nueva prenda: sube la imagen a Firebase Storage (si se proporciona)
     * y guarda los datos de la prenda en Cloud Firestore.
     * Este es el método que llamará RegistrarPrendaActivity.
     *
     * @param prenda Objeto Prenda con los datos recolectados de la UI.
     * @param imagenUri Uri de la imagen seleccionada por el usuario (puede ser null).
     */
    fun registrarNuevaPrenda(prenda: Prenda, imagenUri: Uri?) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _errorMensaje.value = "Debes iniciar sesión para registrar una prenda."
            _registroExitoso.value = false
            return
        }
        prenda.userId = currentUser.uid

        _isLoading.value = true
        _registroExitoso.value = false

        viewModelScope.launch {
            try {
                if (imagenUri != null) {
                    val nombreArchivoImagen = "prendas_imagenes/${currentUser.uid}/${UUID.randomUUID()}.jpg"
                    val imagenEnStorageRef = storageRef.child(nombreArchivoImagen)

                    withContext(Dispatchers.IO) {
                        imagenEnStorageRef.putFile(imagenUri).await()
                    }

                    val downloadUrl = withContext(Dispatchers.IO) {
                        imagenEnStorageRef.downloadUrl.await()
                    }
                    prenda.imagenUrl = downloadUrl.toString()
                } else {
                    prenda.imagenUrl = ""
                }

                val nuevoDocumentoPrendaRef = withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(currentUser.uid)
                        .collection("prendas")
                        .add(prenda)
                        .await()
                }

                Log.d("PrendasViewModel", "Prenda registrada en Firestore con ID: ${nuevoDocumentoPrendaRef.id}")

                _registroExitoso.postValue(true)

            } catch (e: Exception) {
                Log.e("PrendasViewModel", "Error al registrar prenda: ${e.message}", e)
                _errorMensaje.postValue("Error al registrar prenda: ${e.message}")
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
        _registroExitoso.value = false // O null si prefieres un estado de tres vías
    }
}