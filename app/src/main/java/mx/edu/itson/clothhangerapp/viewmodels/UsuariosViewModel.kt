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
        val userId = auth.currentUser?.uid // Get user ID from Auth

        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Get user data from Firestore
                val doc = withContext(Dispatchers.IO) {
                    if (userId != null) { // Check if userId is not null
                        firestoreDb.collection("usuarios")
                            .document(userId)
                            .get()
                            .await()
                    } else {
                        null // Handle the case where the user is not authenticated
                    }
                }

                if (doc?.exists() == true) { // Use the safe call operator ?.
                    val nombre = doc.getString("nombre") ?: "" // Get name, default to ""
                    val email = auth.currentUser?.email ?: "" // Get email from Auth, default to ""
                    var usuario = Usuario()
                    usuario.nombre = nombre
                    usuario.email = email
                    _usuario.postValue(usuario)
                    Log.e("UsuariosViewModel", "Nombre de usuario obtenido correctamente: ${usuario.nombre}")
                    Log.e("UsuariosViewModel", "Email de usuario obtenido correctamente: ${usuario.email}")
                } else {
                    // Handle the case where the document doesn't exist or user is not logged in
                    val email = auth.currentUser?.email ?: ""
                    _usuario.postValue(Usuario("", email)) // Or set an error message
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

    /**
     * Actualiza el nombre del usuario en Firestore.
     * The email is updated in the ConfiguracionActivity, not here.
     */
    fun actualizarDatosUsuario(nuevoNombre: String, nuevoEmail: String) { // Added nuevoEmail, but not used.
        val userId = auth.currentUser?.uid

        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Actualizar datos en Firestore
                withContext(Dispatchers.IO) {
                    firestoreDb.collection("usuarios")
                        .document(userId!!) //handled null in cargarDatosUsuario
                        .update(
                            mapOf(
                                "nombre" to nuevoNombre,
                                // "email" to nuevoEmail  // Email is handled by Firebase Auth
                            )
                        ).await()
                }

                // Update the current Usuario object.  Keep email from auth.
                val currentEmail = auth.currentUser?.email ?: ""
                _usuario.postValue(Usuario(nuevoNombre, currentEmail))

            } catch (e: Exception) {
                Log.e("UsuariosViewModel", "Error al actualizar usuario: ${e.message}", e)
                _errorMensaje.postValue("Error al actualizar los datos. Es posible que necesites reautenticación.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Reautentica al usuario con su contraseña actual (necesario para actualizar email o contraseña).
     */
    fun reautenticarUsuario(email: String, contraseñaActual: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val usuario = auth.currentUser
        val credential = EmailAuthProvider.getCredential(email, contraseñaActual)

        usuario?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error de reautenticación")
                }
            }
    }

    /**
     * Cambia la contraseña del usuario en Firebase Authentication.
     */
    fun cambiarContraseña(nuevaContraseña: String) {
        val usuario = auth.currentUser

        _isLoading.value = true
        viewModelScope.launch {
            try {
                usuario?.updatePassword(nuevaContraseña)?.await()
                Log.d("UsuariosViewModel", "Contraseña actualizada correctamente")
            } catch (e: Exception) {
                Log.e("UsuariosViewModel", "Error al cambiar la contraseña: ${e.message}", e)
                _errorMensaje.postValue("No se pudo cambiar la contraseña. Intenta reautenticarte.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}

