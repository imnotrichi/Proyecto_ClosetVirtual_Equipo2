package mx.edu.itson.clothhangerapp.dataclases

data class Outfit(
    var fecha: android.icu.util.Calendar,
    var top: Articulo?,
    var bodysuit: Articulo?,
    var bottom: Articulo?,
    var zapatos: Articulo?,
    var accesorio1: Articulo?,
    var accesorio2: Articulo?,
    var accesorio3: Articulo?)
