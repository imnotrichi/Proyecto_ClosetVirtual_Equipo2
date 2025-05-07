package mx.edu.itson.clothhangerapp.dataclases

data class Prenda(
    var id: String = "",
    var imagen: String = "",
    var nombre: String = "",
    var categoria: Categoria? = null,
    var estampado: Boolean = false,
    var colorHex: String = "",
    var etiquetas: List<Etiqueta>? = null,
    var usosMensuales: Int = 0,
    var usosTotales: Int = 0
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "imagen" to imagen,
            "nombre" to nombre,
            "categoria" to categoria!!.nombre,
            "estampado" to estampado,
            "colorHex" to colorHex,
            "etiquetas" to etiquetas!!.map { it.nombre },
            "usosMensuales" to usosMensuales,
            "usosTotales" to usosTotales
        )
    }
}
