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

class PrendasViewModel : ViewModel() {

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

    private val _prendaSeleccionadaDetalle = MutableLiveData<Prenda?>()
    val prendaSeleccionadaDetalle: LiveData<Prenda?> = _prendaSeleccionadaDetalle

    private val _errorDetalle = MutableLiveData<String?>()
    val errorDetalle: LiveData<String?> = _errorDetalle

    /**
     * Carga las prendas del usuario autenticado desde su colección en Firestore.
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

                val prendas = resultado.documents.mapNotNull { it.toObject(Prenda::class.java) }
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

    fun vaciarLista() {
        _listaPrendasUsuario.value = emptyList()
    }

    fun cargarTodas() {
        val userId = auth.currentUser?.uid
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val resultado = withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(userId!!)
                        .collection("prendas") // orden alfabético por nombre
                        .get()
                        .await()
                }

                val prendas = resultado.documents.mapNotNull { it.toObject(Prenda::class.java) }
                _listaPrendasUsuario.postValue(prendas)

            } catch (e: Exception) {
                Log.e("PrendasViewModel", "Error al cargar todas las prendas: ${e.message}", e)
                _errorMensaje.postValue("Error al cargar prendas.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun cargarPorCategoria(categoria: String?) {
        val userId = auth.currentUser?.uid
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val resultado = withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(userId!!)
                        .collection("prendas")
                        .get()
                        .await()
                }

                val prendas = resultado.documents.mapNotNull { it.toObject(Prenda::class.java) }

                val filtradas = categoria?.let {
                    prendas.filter { it.categoria.equals(categoria, ignoreCase = true) }
                } ?: prendas

                _listaPrendasUsuario.postValue(filtradas)

                Log.d("PrendasViewModel", "Prendas totales: ${prendas.size}")
                Log.d("PrendasViewModel", "Filtradas por '$categoria': ${filtradas.size}")
                filtradas.forEach {
                    Log.d("PrendasViewModel", "Prenda: ${it.nombre}, Categoría: ${it.categoria}")
                }


            } catch (e: Exception) {
                Log.e("PrendasViewModel", "Error al filtrar por categoría: ${e.message}", e)
                _errorMensaje.postValue("Error al cargar prendas.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

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
                // Subir imagen si existe
                if (imagenUri != null) {
                    val nombreArchivoImagen =
                        "prendas_imagenes/${currentUser.uid}/${UUID.randomUUID()}.jpg"
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

                // Guardar prenda en subcolección del usuario
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(currentUser.uid)
                        .collection("prendas")
                        .add(prenda)
                        .await()
                }

                // Guardar prenda en colección raíz "prendas"
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("prendas")
                        .add(prenda)
                        .await()
                }

                Log.d("PrendasViewModel", "Prenda registrada con éxito.")
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
        _errorDetalle.value = null
    }

    fun limpiarEstadoRegistro() {
        _registroExitoso.value = false
    }

    /**
     * Obtiene los detalles completos de una prenda específica por su ID.
     * El resultado se publica en el LiveData prendaSeleccionadaDetalle.
     *
     * @param prendaId El ID del documento de la prenda a buscar en Firestore.
     */
    fun obtenerDetallePrenda(prendaId: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _errorDetalle.postValue("Usuario no autenticado para obtener detalle.")
            _prendaSeleccionadaDetalle.postValue(null)
            return
        }
        if (prendaId.isBlank()) {
            _errorDetalle.postValue("ID de prenda inválido.")
            _prendaSeleccionadaDetalle.postValue(null)
            return
        }

        _prendaSeleccionadaDetalle.postValue(null)
        _errorDetalle.postValue(null)

        viewModelScope.launch {
            try {
                Log.d("PrendasViewModel", "Buscando prenda con ID: $prendaId para usuario $userId")
                val documentSnapshot = withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(userId)
                        .collection("prendas")
                        .document(prendaId)
                        .get()
                        .await()
                }

                if (documentSnapshot.exists()) {
                    val prenda = documentSnapshot.toObject(Prenda::class.java)
                    Log.d("PrendasViewModel", "Prenda encontrada: ${prenda?.nombre}, ID: ${prenda?.id}")
                    _prendaSeleccionadaDetalle.postValue(prenda) // Publicar prenda encontrada
                } else {
                    Log.w("PrendasViewModel", "No se encontró prenda con ID: $prendaId")
                    _errorDetalle.postValue("No se encontró la prenda seleccionada.")
                }
            } catch (e: Exception) {
                Log.e("PrendasViewModel", "Error al obtener detalle de prenda $prendaId: ${e.message}", e)
                _errorDetalle.postValue("Error al cargar detalles: ${e.message}")
            } finally {
            }
        }
    }
}