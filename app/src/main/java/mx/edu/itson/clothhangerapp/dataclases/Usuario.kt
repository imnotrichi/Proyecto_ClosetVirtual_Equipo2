package mx.edu.itson.clothhangerapp.dataclases

import com.google.firebase.firestore.DocumentId

class Usuario(
    @DocumentId
    var id: String = "",
    var nombre: String = "",
    var email: String = ""
) {
    override fun toString(): String {
        return nombre
    }
}