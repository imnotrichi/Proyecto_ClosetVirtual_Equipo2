package mx.edu.itson.clothhangerapp.dataclases

data class Etiqueta(var nombre: String = "") {

    fun toMap() : Map<String, String> {
        return mapOf("nombre" to nombre)
    }
}
