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
import com.google.firebase.storage.FirebaseStorage
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
    private val storage = FirebaseStorage.getInstance()

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> get() = _operacionExitosa

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

    private val _ultimaPrendaModificada = MutableLiveData<Prenda?>()
    val ultimaPrendaModificada: LiveData<Prenda?> get() = _ultimaPrendaModificada

    private val _nuevaUrlImagen = MutableLiveData<String?>()
    val nuevaUrlImagen: LiveData<String?> get() = _nuevaUrlImagen

    private val _nuevoIdGenerado = MutableLiveData<String?>()
    val nuevoIdGenerado: LiveData<String?> get() = _nuevoIdGenerado

    fun obtenerUsuarioActualId(): String? {
        return auth.currentUser?.uid
    }

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

    fun registrarNuevaPrenda(prendaOriginal: Prenda, imagenUri: Uri?) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _errorMensaje.postValue("Usuario no autenticado.")
            _operacionExitosa.postValue(false)
            return
        }

        _isLoading.postValue(true)
        viewModelScope.launch {
            var imagenStorageRefPath: String? = null // Para borrar en caso de fallo
            var prendaParaGuardar = prendaOriginal.copy(userId = currentUser.uid)
            try {

                if (imagenUri != null) {
                    val nombreArchivo = "prendas_imagenes/${currentUser.uid}/${System.currentTimeMillis()}"
                    val imagenEnStorageRef = storage.reference.child(nombreArchivo)
                    imagenStorageRefPath = imagenEnStorageRef.path // Guardar path para posible borrado

                    Log.d("PrendasViewModel", "Subiendo imagen a: $imagenStorageRefPath")
                    val downloadUrl = withContext(Dispatchers.IO) {
                        imagenEnStorageRef.putFile(imagenUri).await() // Subir
                        imagenEnStorageRef.downloadUrl.await() // Obtener URL
                    }
                    prendaParaGuardar = prendaParaGuardar.copy(imagenUrl = downloadUrl.toString())
                    _nuevaUrlImagen.postValue(downloadUrl.toString())
                    Log.d("PrendasViewModel", "Imagen subida, URL: ${prendaParaGuardar.imagenUrl}")
                } else {
                    prendaParaGuardar = prendaParaGuardar.copy(imagenUrl = "") // Sin imagen o imagen vacía
                    _nuevaUrlImagen.postValue("")
                }

                // 1. Guardar prenda en subcolección del usuario y obtener el ID generado
                val userPrendaDocRef = withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(currentUser.uid)
                        .collection("prendas")
                        .add(prendaParaGuardar) // Firestore genera el ID aquí
                        .await()
                }
                val prendaIdGenerado = userPrendaDocRef.id
                prendaParaGuardar = prendaParaGuardar.copy(id = prendaIdGenerado) // Asignar el ID al objeto
                Log.d("PrendasViewModel", "Prenda guardada en /usuarios/... con ID: $prendaIdGenerado")


                // 2. Opcional: Guardar prenda en colección raíz "prendas" USANDO EL MISMO ID
                //    Esta parte depende de tu arquitectura. Si la colección raíz "prendas"
                //    es solo una copia o un índice, asegúrate de usar el ID correcto.
                //    Si no necesitas esta colección raíz, puedes eliminar este bloque.
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("prendas")
                        .document(prendaIdGenerado) // Usar el ID generado anteriormente
                        .set(prendaParaGuardar) // Usar set() con un ID específico
                        .await()
                }
                Log.d("PrendasViewModel", "Prenda guardada/actualizada en /prendas con ID: $prendaIdGenerado")


                Log.d("PrendasViewModel", "Prenda registrada con éxito total.")
                _ultimaPrendaModificada.postValue(prendaParaGuardar) // Devolver la prenda con su ID y URL de imagen
                _operacionExitosa.postValue(true)

            } catch (e: Exception) {
                Log.e("PrendasViewModel", "Error al registrar prenda: ${e.message}", e)
                _errorMensaje.postValue("Error al registrar prenda: ${e.message}")
                _operacionExitosa.postValue(false)

                // Si la imagen se subió pero Firestore falló, intentar borrar la imagen de Storage
                imagenStorageRefPath?.let { path ->
                    Log.w("PrendasViewModel", "Error en Firestore, intentando borrar imagen subida: $path")
                    storage.reference.child(path).delete()
                        .addOnSuccessListener { Log.i("PrendasViewModel", "Imagen $path borrada por fallo en Firestore.") }
                        .addOnFailureListener { Log.e("PrendasViewModel", "Error al borrar imagen $path tras fallo en Firestore.", it)}
                }
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun actualizarPrenda(prendaAActualizarOriginal: Prenda, nuevaImagenUri: Uri?, imagenUrlOriginalParaBorrar: String?) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _errorMensaje.postValue("Usuario no autenticado.")
            _operacionExitosa.postValue(false)
            return
        }
        if (prendaAActualizarOriginal.id == null) {
            _errorMensaje.postValue("ID de prenda es null, no se puede actualizar.")
            _operacionExitosa.postValue(false)
            return
        }

        _isLoading.postValue(true)
        viewModelScope.launch {
            var prendaConCambios = prendaAActualizarOriginal.copy(userId = currentUser.uid) // Asegurar userId
            var nuevaImagenStorageRefPath: String? = null

            try {
                if (nuevaImagenUri != null) {
                    // 1. Subir la nueva imagen
                    val nombreArchivo = "prendas_imagenes/${currentUser.uid}/${System.currentTimeMillis()}_edit"
                    val nuevaImagenRef = storage.reference.child(nombreArchivo)
                    nuevaImagenStorageRefPath = nuevaImagenRef.path

                    Log.d("PrendasViewModel", "Subiendo NUEVA imagen para edición a: $nuevaImagenStorageRefPath")
                    val downloadUrl = withContext(Dispatchers.IO) {
                        nuevaImagenRef.putFile(nuevaImagenUri).await()
                        nuevaImagenRef.downloadUrl.await()
                    }
                    prendaConCambios = prendaConCambios.copy(imagenUrl = downloadUrl.toString())
                    _nuevaUrlImagen.postValue(downloadUrl.toString())
                    Log.d("PrendasViewModel", "Nueva imagen subida, URL: ${prendaConCambios.imagenUrl}")

                    // 2. Borrar la imagen antigua de Storage si existía y es diferente
                    if (!imagenUrlOriginalParaBorrar.isNullOrEmpty() && imagenUrlOriginalParaBorrar != prendaConCambios.imagenUrl) {
                        Log.d("PrendasViewModel", "Intentando borrar imagen antigua: $imagenUrlOriginalParaBorrar")
                        borrarImagenDeStorageSilenciosamente(imagenUrlOriginalParaBorrar)
                    }
                } else {
                    // No se seleccionó nueva imagen, mantener la original (ya debería estar en prendaAActualizarOriginal.imagenUrl)
                    // Aseguramos que se mantenga si no se pasó nuevaImagenUri
                    prendaConCambios = prendaConCambios.copy(imagenUrl = imagenUrlOriginalParaBorrar ?: "")
                    _nuevaUrlImagen.postValue(imagenUrlOriginalParaBorrar) // Devolver la URL original o la que tenía
                }

                // 3. Actualizar datos en Firestore (en la subcolección del usuario)
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(currentUser.uid)
                        .collection("prendas")
                        .document(prendaConCambios.id!!) // Usar el ID existente
                        .set(prendaConCambios) // set() sobrescribe todo el documento
                        .await()
                }
                Log.d("PrendasViewModel", "Prenda actualizada en /usuarios/... con ID: ${prendaConCambios.id}")

                // 4. Opcional: Actualizar en la colección raíz "prendas"
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("prendas")
                        .document(prendaConCambios.id!!) // Usar el mismo ID
                        .set(prendaConCambios)
                        .await()
                }
                Log.d("PrendasViewModel", "Prenda actualizada en /prendas con ID: ${prendaConCambios.id}")


                Log.d("PrendasViewModel", "Prenda actualizada con éxito total.")
                _ultimaPrendaModificada.postValue(prendaConCambios)
                _operacionExitosa.postValue(true)

            } catch (e: Exception) {
                Log.e("PrendasViewModel", "Error al actualizar prenda: ${e.message}", e)
                _errorMensaje.postValue("Error al actualizar prenda: ${e.message}")
                _operacionExitosa.postValue(false)

                // Si se subió una nueva imagen pero Firestore falló, intentar borrar la nueva imagen
                nuevaImagenStorageRefPath?.let { path ->
                    Log.w("PrendasViewModel", "Error en Firestore durante actualización, intentando borrar NUEVA imagen subida: $path")
                    storage.reference.child(path).delete()
                        .addOnSuccessListener { Log.i("PrendasViewModel", "Nueva imagen $path borrada por fallo en Firestore.") }
                        .addOnFailureListener { Log.e("PrendasViewModel", "Error al borrar nueva imagen $path tras fallo en Firestore.", it)}
                }
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun borrarImagenDeStorageSilenciosamente(imageUrl: String?) {
        if (imageUrl.isNullOrEmpty() || imageUrl.isBlank()) {
            Log.d("PrendasViewModel", "URL de imagen para borrar es vacía o nula, no se borra.")
            return
        }
        // Verificar si es una URL de Firebase Storage
        if (!imageUrl.startsWith("gs://") && !imageUrl.startsWith("https://firebasestorage.googleapis.com/")) {
            Log.w("PrendasViewModel", "URL de imagen no parece ser de Firebase Storage, no se borrará: $imageUrl")
            return
        }

        try {
            val storageRef = storage.getReferenceFromUrl(imageUrl)
            storageRef.delete()
                .addOnSuccessListener { Log.i("PrendasViewModel", "Imagen $imageUrl borrada de Storage exitosamente.") }
                .addOnFailureListener { e ->
                    // Es común que falle si el archivo no existe (ej. ya fue borrado, o la URL era incorrecta)
                    // Por eso es "silencioso", no queremos que esto bloquee al usuario si la prenda ya se actualizó.
                    Log.w("PrendasViewModel", "Fallo al intentar borrar imagen $imageUrl de Storage (puede que no existiera o problema de permisos).", e)
                }
        } catch (e: IllegalArgumentException) {
            Log.e("PrendasViewModel", "URL de imagen inválida para getReferenceFromUrl: $imageUrl", e)
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