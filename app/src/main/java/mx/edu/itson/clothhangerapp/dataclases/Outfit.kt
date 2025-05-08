package mx.edu.itson.clothhangerapp.dataclases

import com.google.firebase.Timestamp

data class Outfit(
    var userId: String? = null,
    var fecha: Timestamp,
    var top: Prenda?,
    var bodysuit: Prenda?,
    var bottom: Prenda?,
    var zapatos: Prenda?,
    var accesorio1: Prenda?,
    var accesorio2: Prenda?,
    var accesorio3: Prenda?
) {
    constructor() : this(
        userId = null,
        fecha = Timestamp.now(),
        top = null,
        bodysuit = null,
        bottom = null,
        zapatos = null,
        accesorio1 = null,
        accesorio2 = null,
        accesorio3 = null
    )
}

