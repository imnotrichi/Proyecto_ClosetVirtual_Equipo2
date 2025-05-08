package mx.edu.itson.clothhangerapp.dataclases

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import com.google.firebase.Timestamp

data class Outfit(
    @DocumentId
    var id: String? = null,
    var userId: String? = null,
    var fecha: Timestamp = Timestamp.now(),
    var top: Prenda? = null,
    var bottom: Prenda? = null,
    var bodysuit: Prenda? = null,
    var zapatos: Prenda? = null,
    var accesorio1: Prenda? = null,
    var accesorio2: Prenda? = null,
    var accesorio3: Prenda? = null,

    @ServerTimestamp
    var fechaCreacion: Date? = null
) {
    constructor() : this(
        id = null, userId = null, fecha = Timestamp.now(),
        top = null, bottom = null, bodysuit = null, zapatos = null,
        accesorio1 = null, accesorio2 = null, accesorio3 = null,
        fechaCreacion = null
    )
}

