package mx.edu.itson.clothhangerapp.dataclases

data class Prenda(
    var id: String = "1",
    var imagen: String = "",
    var nombre: String = "",
    var categoria: String = "",
    var estampado: Boolean = false,
    var colorHex: String = "",
    var etiquetas: MutableList<String> = mutableListOf(),
    var usosMensuales: Int = 0,
    var usosTotales: Int = 0
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "imagen" to imagen,
            "nombre" to nombre,
            "categoria" to categoria,
            "estampado" to estampado,
            "colorHex" to colorHex,
            "etiquetas" to etiquetas,
            "usosMensuales" to usosMensuales,
            "usosTotales" to usosTotales
        )
    }
}
