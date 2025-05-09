package mx.edu.itson.clothhangerapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import mx.edu.itson.clothhangerapp.dataclases.Usuario

class UsuariosViewModel : ViewModel() {

    private val firestoreDb = Firebase.firestore
    private val auth = FirebaseAuth.getInstance() // Get Firebase Auth instance

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    private val _errorMensaje = MutableLiveData<String?>()
    val errorMensaje: LiveData<String?> = _errorMensaje

    fun cargarDatosUsuario() {
        val userId = auth.currentUser?.uid // Obtener UID desde Auth

        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Obtener datos del usuario desde Firestore
                val doc = withContext(Dispatchers.IO) {
                    if (userId != null) {
                        firestoreDb.collection("usuarios")
                            .document(userId)
                            .get()
                            .await()
                    } else {
                        null
                    }
                }

                if (doc?.exists() == true) {
                    val nombre = doc.getString("nombre") ?: ""
                    val email = doc.getString("email") ?: "" // Obtener email desde Firestore

                    val usuario = Usuario().apply {
                        this.nombre = nombre
                        this.email = email
                    }

                    _usuario.postValue(usuario)
                    Log.e("UsuariosViewModel", "Nombre de usuario obtenido correctamente: ${usuario.nombre}")
                    Log.e("UsuariosViewModel", "Email de usuario obtenido correctamente: ${usuario.email}")
                } else {
                    _usuario.postValue(Usuario("", ""))
                    _errorMensaje.postValue("Usuario no encontrado en la base de datos.")
                }

            } catch (e: Exception) {
                Log.e("UsuariosViewModel", "Error al cargar usuario: ${e.message}", e)
                _errorMensaje.postValue("Error al cargar datos del usuario: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun actualizarDatosUsuario(nuevoNombre: String, nuevoEmail: String, nuevoHashContrasenia: String) {
        val userId = auth.currentUser?.uid

        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Actualizar datos en Firestore
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(userId!!) // Ya se controla null al obtener el UID
                        .update(
                            mapOf(
                                "nombre" to nuevoNombre,
                                "email" to nuevoEmail,
                                "contraseniaHash" to nuevoHashContrasenia
                            )
                        ).await()
                }

                // Actualizar el objeto Usuario en LiveData
                _usuario.postValue(Usuario(nuevoNombre, nuevoEmail))

            } catch (e: Exception) {
                Log.e("UsuariosViewModel", "Error al actualizar usuario: ${e.message}", e)
                _errorMensaje.postValue("Error al actualizar los datos. Es posible que necesites reautenticación.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

}

