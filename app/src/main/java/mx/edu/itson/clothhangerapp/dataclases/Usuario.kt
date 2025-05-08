package mx.edu.itson.clothhangerapp.dataclases

class Usuario(
    var id: String = "",
    var nombre: String = "",
    var email: String = ""
) {
    override fun toString(): String {
        return nombre
    }
}