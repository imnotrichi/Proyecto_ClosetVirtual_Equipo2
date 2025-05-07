package mx.edu.itson.clothhangerapp.dataclases

import com.google.firebase.firestore.DocumentId

data class Prenda(
    @DocumentId
    var id: String? = null,
    var imagenUrl: String = "",
    var userId: String? = null,
    var nombre: String = "",
    var categoria: String = "",
    var estampado: Boolean = false,
    var colorHex: String = "",
    var etiquetas: MutableList<String> = mutableListOf(),
    var usosMensuales: Int = 0,
    var usosTotales: Int = 0
) {
    constructor() : this(
        id = null,
        imagenUrl = "",
        userId = null,
        nombre = "",
        categoria = "",
        estampado = false,
        colorHex = "",
        etiquetas = mutableListOf(),
        usosMensuales = 0,
        usosTotales = 0
    )
}
